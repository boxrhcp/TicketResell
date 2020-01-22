package eit.tub.ec.TicketResellBackend.Ethereum;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.FastRawTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.tx.response.PollingTransactionReceiptProcessor;

import java.math.BigInteger;

public class ContractDeploy {
    /*
    public static String deploy(String userPK, String url) throws Exception {
        Web3j web3j = Web3j.build(new HttpService(url));
        Credentials credentials = Credentials.create(userPK);
        // Get the account address
        PollingTransactionReceiptProcessor processor = new PollingTransactionReceiptProcessor(web3j, 5000l, 5);
        //deploy new contract
        TransactionManager txManager = new FastRawTransactionManager(web3j, credentials, processor);

        RemoteCall<TicketResell> request = TicketResell.deploy(web3j, txManager,
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
        TicketResell token = request.send();
        // load existing contract by address
        return token.getContractAddress();
    }

     */

}
