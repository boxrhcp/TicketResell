package eit.tub.ec.TicketResellBackend.Ethereum;

import eit.tub.ec.TicketResellBackend.Ethereum.Contracts.Tickets;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Contract;
import org.web3j.tx.FastRawTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.tx.response.PollingTransactionReceiptProcessor;

import java.math.BigInteger;

public class PublishTicketLibrary {

    /**
     * This function deploys the ticket library contract
     *
     * @param url     the url to connect to ethereum
     * @param ownerPK the private key of the deployer of this contract
     * @return the contract created address
     * @throws Exception if call goes wrong
     */
    public static String deployLibrary(String url, String ownerPK) throws Exception {
        Web3j web3j = Web3j.build(new HttpService(url));
        Credentials credentials = Credentials.create(ownerPK);
        PollingTransactionReceiptProcessor processor = new PollingTransactionReceiptProcessor(web3j, 15000l, 10);

        TransactionManager txManager = new FastRawTransactionManager(web3j, credentials, processor);
        //deploy new contract
        RemoteCall<Tickets> request = Tickets.deploy(web3j, txManager,
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
        Tickets token = request.send();
        // load existing contract by address
        String address = token.getContractAddress();
        System.out.println(address);
        return address;
    }
}
