package eit.tub.ec.TicketResellBackend.Ethereum;

import eit.tub.ec.TicketResellBackend.Ethereum.Contracts.OwnerContract;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Contract;
import org.web3j.tx.FastRawTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.tx.response.PollingTransactionReceiptProcessor;
import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.math.BigInteger;

public class TicketPaymentManager {

    private OwnerContract conn;

    /**
     * This class will load the owner contract published by a ticket owner to sell his ticket. With this class
     * the buyer can check the price and buy the ticket
     *
     * @param ownerContractAddress the address of the contract of the owner
     * @param url                  the url to connect to Ethereum
     * @param buyerPK              the private key of the buyer
     */
    public TicketPaymentManager(String ownerContractAddress, String url, String buyerPK) {
        Web3j web3j = Web3j.build(new HttpService(url));
        PollingTransactionReceiptProcessor processor = new PollingTransactionReceiptProcessor(web3j, 15000l
                , 10);
        Credentials credentials = Credentials.create(buyerPK);
        TransactionManager txManager = new FastRawTransactionManager(web3j, credentials, processor);
        conn = OwnerContract.load(ownerContractAddress, web3j, txManager,
                new DefaultGasProvider() {
                    @Override
                    public BigInteger getGasPrice(String contractFunc) {
                        switch (contractFunc) {
                            default:
                                return Contract.GAS_PRICE;
                        }
                    }

                    @Override
                    public BigInteger getGasLimit(String contractFunc) {
                        switch (contractFunc) {
                            default:
                                return Contract.GAS_LIMIT;
                        }
                    }
                });

    }

    /**
     * Function to get the current ticket price.
     *
     * @param ticketId the id of the ticket
     * @return the price in ETHER of the ticket
     * @throws Exception if call goes wrong
     */
    public BigDecimal getTicketPrice(BigInteger ticketId) throws Exception {
        return Convert.fromWei(new BigDecimal(conn.currentPrice().send()), Convert.Unit.ETHER);
    }

    /**
     * Function to buy the ticket
     *
     * @param ticketId the id of the ticket the user wants to buy
     * @param amount   the amount of ETHER the user wants to pay
     * @return state of the transaction
     * @throws Exception if call goes wrong
     */
    public boolean buyTicket(BigInteger ticketId, float amount) throws Exception {
        BigDecimal value = Convert.toWei(new BigDecimal(amount), Convert.Unit.ETHER);
        return conn.buyTicket(ticketId, value.toBigInteger()).send().isStatusOK();
    }

}
