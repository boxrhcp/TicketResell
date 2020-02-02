package eit.tub.ec.TicketResellBackend.Ethereum;

import eit.tub.ec.TicketResellBackend.Ethereum.Contracts.OwnerContract;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Contract;
import org.web3j.tx.FastRawTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.tx.response.PollingTransactionReceiptProcessor;
import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.math.BigInteger;

public class PublishTicket {

    /**
     * This function will deploy a Owner Contract in Ethereum. This Owner Contract is deployed by an owner of
     * a ticket to publish it for selling.
     *
     * @param ownerPK              the primary key of the user that wants to sell the ticket
     * @param url                  the url of the blockchain
     * @param ticketLibraryAddress the address to the TicketLibrary contract
     * @param price                the amount of ETHER that the owner wants to receive for the ticket
     * @return the contract published address
     * @throws Exception if the call to deploy goes wrong
     */
    public static String deploy(String ownerPK, String url, String ticketLibraryAddress,
                                float price) throws Exception {
        Web3j web3j = Web3j.build(new HttpService(url));
        Credentials credentials = Credentials.create(ownerPK);
        PollingTransactionReceiptProcessor processor = new PollingTransactionReceiptProcessor(web3j, 15000l, 10);

        TransactionManager txManager = new FastRawTransactionManager(web3j, credentials, processor);
        BigDecimal value = Convert.toWei(new BigDecimal(price), Convert.Unit.ETHER);
        //deploy new contract
        RemoteCall<OwnerContract> request = OwnerContract.deploy(web3j, txManager,
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
                }, ticketLibraryAddress, value.toBigInteger());
        OwnerContract token = request.send();
        // load existing contract by address
        return token.getContractAddress();
    }


}
