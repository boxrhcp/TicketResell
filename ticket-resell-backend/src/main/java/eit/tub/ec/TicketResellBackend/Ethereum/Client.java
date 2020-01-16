package eit.tub.ec.TicketResellBackend.Ethereum;

import eit.tub.ec.TicketResellBackend.Ethereum.Contracts.TicketResell;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tuples.generated.Tuple3;
import org.web3j.tx.FastRawTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.tx.response.PollingTransactionReceiptProcessor;
import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.math.BigInteger;

public class Client {

    private TicketResell token;

    /**
     * @param contract the contract address that must have been deployed
     * @param userPK   the primary key of the user that has to execute a transaction
     * @param url      the url of the ethereum RPC server
     */
    public Client(String contract, String userPK, String url) {
        Web3j web3j = Web3j.build(new HttpService(url));
        PollingTransactionReceiptProcessor processor = new PollingTransactionReceiptProcessor(web3j, 5000l
                , 5);
        Credentials credentials = Credentials.create(userPK);
        TransactionManager txManager = new FastRawTransactionManager(web3j, credentials, processor);
        token = TicketResell.load(contract, web3j, txManager,
                new DefaultGasProvider() {
                    @Override
                    public BigInteger getGasPrice(String contractFunc) {
                        switch (contractFunc) {
                            case TicketResell.FUNC_BUYTICKET:
                                return BigInteger.valueOf(22_000_000_000L);
                            case TicketResell.FUNC_CREATETICKET:
                                return BigInteger.valueOf(44_000_000_000L);
                            default:
                                return BigInteger.valueOf(30_000_000_000L);
                        }
                    }

                    @Override
                    public BigInteger getGasLimit(String contractFunc) {
                        switch (contractFunc) {
                            case TicketResell.FUNC_BUYTICKET:
                                return BigInteger.valueOf(4_300_000);
                            case TicketResell.FUNC_CREATETICKET:
                                return BigInteger.valueOf(5_300_000);
                            default:
                                return BigInteger.valueOf(4_800_000);
                        }
                    }
                });
    }

    /**
     * This method creates a Ticket in the blockchain. The owner of the ticket is the user creating the ticket
     *
     * @param ticket the ticket hash
     * @param price  the price of the ticket
     * @return call success
     * @throws Exception
     */
    public boolean createTicket(String ticket, int price) throws Exception {
        BigDecimal value = new BigDecimal(price);
        BigDecimal currency = Convert.toWei(value, Convert.Unit.ETHER);
        TransactionReceipt receipt = token.createTicket(ticket, currency.toBigInteger()).send();
        return receipt.isStatusOK();
    }

    /**
     * This method executes the payment of the user calling it to buy the specified ticket.
     *
     * @param ticket ticket that user wants to buy
     * @param price price that user wants to pay
     * @return call success
     * @throws Exception
     */
    public boolean buyTicket(String ticket, int price) throws Exception {
        BigDecimal value = Convert.toWei(new BigDecimal(price), Convert.Unit.ETHER);
        TransactionReceipt receipt = token.buyTicket(ticket, value.toBigInteger()).send();
        return receipt.isStatusOK();
    }

    /**
     * This method retrieves the info of the specified ticket
     *
     * @param ticket ticket the user wants to know about
     * @return returns the ticket information
     * @throws Exception
     */
    public String getTicketInfo(String ticket) throws Exception {
        Tuple3<BigInteger, String, BigInteger> receipt = token.getTicketInfo(ticket).send();
        return receipt.component1() + ":" + receipt.component2() + ":" + receipt.component3();
    }
}
