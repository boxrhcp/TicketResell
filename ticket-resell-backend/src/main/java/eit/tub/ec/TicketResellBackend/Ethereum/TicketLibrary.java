package eit.tub.ec.TicketResellBackend.Ethereum;

import eit.tub.ec.TicketResellBackend.Contracts.Tickets;
import org.web3j.abi.EventEncoder;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.FastRawTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.tx.response.PollingTransactionReceiptProcessor;

import java.math.BigInteger;
import java.util.ArrayList;

public class TicketLibrary {

    private Tickets conn;
    private String account;

    /**
     * This class provides access to the TicketLibrary in Ethereum. Ticket Library is the smart contract that will
     * provide the function to create tickets, from this contract we will be able to track who is owner of what ticket
     *
     * @param ticketLibraryAddress the address of the Ticket Library contract
     * @param url                  the url that we use to connect to Ethereum
     * @param userPK               the private key of the user that wants to create or manage a ticket inside TicketLibrary.
     */
    public TicketLibrary(String ticketLibraryAddress, String url, String userPK) {
        Web3j web3j = Web3j.build(new HttpService(url));
        PollingTransactionReceiptProcessor processor = new PollingTransactionReceiptProcessor(web3j, 5000l
                , 5);
        Credentials credentials = Credentials.create(userPK);
        account = credentials.getAddress();
        TransactionManager txManager = new FastRawTransactionManager(web3j, credentials, processor);
        conn = Tickets.load(ticketLibraryAddress, web3j, txManager,
                new DefaultGasProvider() {
                    @Override
                    public BigInteger getGasPrice(String contractFunc) {
                        switch (contractFunc) {
                            case Tickets.FUNC_APPROVE:
                                return BigInteger.valueOf(22_000_000_000L);
                            case Tickets.FUNC_CREATETICKET:
                                return BigInteger.valueOf(44_000_000_000L);
                            default:
                                return BigInteger.valueOf(30_000_000_000L);
                        }
                    }

                    @Override
                    public BigInteger getGasLimit(String contractFunc) {
                        switch (contractFunc) {
                            case Tickets.FUNC_APPROVE:
                                return BigInteger.valueOf(4_300_000);
                            case Tickets.FUNC_CREATETICKET:
                                return BigInteger.valueOf(5_300_000);
                            default:
                                return BigInteger.valueOf(4_800_000);
                        }
                    }
                });
    }

    /**
     * This function will create a ticket in TicketLibrary. The user that sends this transaction is the
     * owner of the ticket
     *
     * @param uri uri with the ticket metadata
     * @return BigInteger id of the ticket created
     * @throws Exception if transaction call goes wrong
     */
    public BigInteger createTicket(String uri) throws Exception {
        TransactionReceipt receipt = conn.createTicket(uri).send();
        if (!receipt.isStatusOK()) throw new Exception("Transaction did not return OK.");

        // Check in the events the created ticket id
        final EthFilter ethFilter = new EthFilter(DefaultBlockParameterName.LATEST, DefaultBlockParameterName.LATEST,
                conn.getContractAddress()).addSingleTopic(EventEncoder.encode(Tickets.CREATE_EVENT))
                .addOptionalTopics(account);
        final ArrayList<BigInteger> results = new ArrayList<>();
        conn.createEventFlowable(ethFilter)
                .subscribe(event -> {
                    final String owner = event.owner;
                    if (owner.equals(account)) {
                        results.add(event.tokenId);
                    }
                });

        return results.get(0);
    }

    /**
     * This function will approve another address to transferOwnership of the ticket. This address in our case
     * will be the ownercontract address which will take care of the selling process.
     *
     * @param ownerContract the address of the ownercontract created to sell the ticket
     * @param ticketId      the ticket id inside the Ticket Library
     * @return boolean if the transaction has been committed
     * @throws Exception if the call goes wrong
     */
    public boolean approve(String ownerContract, BigInteger ticketId) throws Exception {
        return conn.approve(ownerContract, ticketId).send().isStatusOK();
    }

    /**
     * Get owner of the ticket specified.
     *
     * @param ticketId id of the ticket en Ethereum
     * @return the owner address
     * @throws Exception if call goes wrong
     */
    public String getOwner(BigInteger ticketId) throws Exception {
        return conn.ownerOf(ticketId).send();
    }

}
