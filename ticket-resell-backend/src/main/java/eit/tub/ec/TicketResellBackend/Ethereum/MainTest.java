package eit.tub.ec.TicketResellBackend.Ethereum;

import eit.tub.ec.TicketResellBackend.Ethereum.Contracts.Tickets;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.FastRawTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.tx.response.PollingTransactionReceiptProcessor;

import java.io.IOException;
import java.math.BigInteger;

public class MainTest {

    public static void main(String[] args) {
        System.out.println("Connecting to Ethereum ...");
//        String url = "http://35.205.147.10:8501";
        String url = "http://127.0.0.1:8545";
        System.out.println("Successfuly connected to Ethereum");
        String libraryAddress = "0x0d2139319b5473d7b5bcbd6eae5d92a982532ad8";

        try {
//            String pk = "0xde59ac089c1221236b6441dd9c53d0514c9d6c38a0446b01cfb32dc4f62c4f8d"; // Add a private key here
            String pk = "1f0b2af9ab09c0213268e2c6e3fe6829c9c80ddbff450de2861b4519ef551f5d"; // Add a private key here

            deployLibrary(url,pk);
//            createTicket(url, pk);
//            System.out.println(ticketId);
//            String sellContract = PublishTicket.deploy(pk, url, libraryAddress, new BigInteger("30"));
//            System.out.println(sellContract);

        } catch (IOException ex) {
            throw new RuntimeException("Error whilst sending json-rpc requests", ex);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void approveSell(String url, String userPK, String sellContract, BigInteger ticketId) throws Exception {
        TicketLibrary lib = new TicketLibrary("0x0d2139319b5473d7b5bcbd6eae5d92a982532ad8", url,
                userPK);
        System.out.println(lib.approve(sellContract,ticketId));
    }

    public static void createTicket(String url, String userPK) throws Exception {
        TicketLibrary lib = new TicketLibrary("0x0d2139319b5473d7b5bcbd6eae5d92a982532ad8", url,
                userPK);
        lib.createTicket("https://game.example/item-id-8u5h2m.json");
    }


    public static void deployLibrary(String url, String ownerPK) throws Exception {
        Web3j web3j = Web3j.build(new HttpService(url));
        Credentials credentials = Credentials.create(ownerPK);
        PollingTransactionReceiptProcessor processor = new PollingTransactionReceiptProcessor(web3j, 5000l, 5);

        TransactionManager txManager = new FastRawTransactionManager(web3j, credentials, processor);
        //deploy new contract
        RemoteCall<Tickets> request = Tickets.deploy(web3j, txManager,
                new DefaultGasProvider() {
                    @Override
                    public BigInteger getGasPrice(String contractFunc) {
                        switch (contractFunc) {
                            case Tickets.FUNC_CREATETICKET:
                                return BigInteger.valueOf(22_000_000_000L);
                            default:
                                return BigInteger.valueOf(30_000_000_000L);
                        }
                    }

                    @Override
                    public BigInteger getGasLimit(String contractFunc) {
                        switch (contractFunc) {
                            case Tickets.FUNC_CREATETICKET:
                                return BigInteger.valueOf(4_300_000);
                            default:
                                return BigInteger.valueOf(4_800_000);
                        }
                    }
                });
        Tickets token = request.send();
        // load existing contract by address
        System.out.println(token.getContractAddress());
    }
}

