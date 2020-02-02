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

public class OwnerContractManager {

    private OwnerContract conn;
    private String account;

    /**
     * This class will load the owner contract created to sell the ticket to manage it. With it the owner will
     * be able to change the price of the ticket, pause the selling of the ticket, unpause it, and withdraw from
     * the contract the money collected from the transfer.
     *
     * @param ownerContractAddress the owner contract address published
     * @param url                  the url to Ethereum
     * @param ownerPK              the owner private key
     */
    public OwnerContractManager(String ownerContractAddress, String url, String ownerPK) {
        Web3j web3j = Web3j.build(new HttpService(url));
        PollingTransactionReceiptProcessor processor = new PollingTransactionReceiptProcessor(web3j, 15000l
                , 7);
        Credentials credentials = Credentials.create(ownerPK);
        account = credentials.getAddress();
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
     * With this function ONLY the owner of the ticket and contract will be able to change the price in ETHER
     *
     * @param newPrice the new price of the ticket
     * @return status of transaction
     * @throws Exception if call goes wrong
     */
    public boolean setNewPrice(float newPrice) throws Exception {
        BigDecimal value = Convert.toWei(new BigDecimal(newPrice), Convert.Unit.ETHER);
        return conn.setCurrentPrice(value.toBigInteger()).send().isStatusOK();
    }

    /**
     * With this function ONLY the owner of the ticket and contract will be able to pause the contract
     * to stop selling the ticket
     *
     * @return status of transaction
     * @throws Exception if call goes wrong
     */
    public boolean stopSellingTicket() throws Exception {
        return conn.pause().send().isStatusOK();
    }

    /**
     * With this function ONLY the owner of the ticket and contract will be able to unpause the contract to
     * resume selling the ticket
     *
     * @return status of transaction
     * @throws Exception if call goes wrong
     */
    public boolean resumeSellingTicket() throws Exception {
        return conn.unpause().send().isStatusOK();
    }

    /**
     * RECOMMENDED TO USE DESTROYCONTRACT() INSTEAD
     * With this function ONLY the owner of the ticket and contract will be able to withdraw the money earned
     * in the contract.
     *
     * @return status of transaction
     * @throws Exception if call goes wrong
     */
    public boolean getMoney() throws Exception {
        return conn.withdraw(account).send().isStatusOK();
    }

    /**
     * With this function ONLY the owner can destroy the contract and receive the money from it. Useful
     * when the ticket has been already sold and the contract has no more utility.
     *
     * @return status of transaction
     * @throws Exception if call goes wrong
     */
    public boolean destroyContract() throws Exception {
        return conn.destroyContract(account).send().isStatusOK();
    }
}
