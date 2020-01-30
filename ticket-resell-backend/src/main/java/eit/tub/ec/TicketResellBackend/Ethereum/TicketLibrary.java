package eit.tub.ec.TicketResellBackend.Ethereum;

import eit.tub.ec.TicketResellBackend.Ethereum.Contracts.Tickets;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.FastRawTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.tx.response.PollingTransactionReceiptProcessor;

import java.math.BigInteger;

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
        PollingTransactionReceiptProcessor processor = new PollingTransactionReceiptProcessor(
                web3j, 5000L, 5);
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
     * @return if the transaction has been committed
     * @throws Exception if transaction call goes wrong
     */
    public boolean createTicket(String uri) throws Exception {
        return conn.createTicket(uri).send().isStatusOK();
    }

    /**
     * Get the last ticket id of the blockchain
     *
     * @return the last id
     * @throws Exception if transaction call goes wrong
     */
    public BigInteger getLastTicketId() throws Exception {
        return conn.getLastId().send();
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

    /**
     * Get an owner ticketId by index of tickets hold by the owner. Useful when creating tickets to get the
     * newly created IDs
     *
     * @param owner the owner user we want to get the information from
     * @param index the ticket index within the owner ticket list
     * @return ticketId found
     * @throws Exception if call goes wrong
     */
    public BigInteger getOwnerTicketByIndex(String owner, BigInteger index) throws Exception {
        return conn.tokenOfOwnerByIndex(owner, index).send();
    }

}
