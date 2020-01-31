package eit.tub.ec.TicketResellBackend.Ethereum;

import eit.tub.ec.TicketResellBackend.Ethereum.Contracts.OwnerContract;
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
import java.math.BigDecimal;
import java.math.BigInteger;

public class MainTest {

    public static void main(String[] args) {
        System.out.println("Connecting to Ethereum ...");
//        String url = "http://35.205.147.10:8501";
        String url = "http://127.0.0.1:8545";
        System.out.println("Successfuly connected to Ethereum");
//        String libraryAddress = "0x0d2139319b5473d7b5bcbd6eae5d92a982532ad8";

        try {
//            String pk = "0xde59ac089c1221236b6441dd9c53d0514c9d6c38a0446b01cfb32dc4f62c4f8d"; // Add a private key here
            String pk = "0c98cb8029bc1a2b970b322604cde7d2106d90c170573eb0e0306ac4bd0a2351"; // Add a private key here
            String pk2 = "02dc3c7369f16bb2bd1de6ef95ae3431e08cbb9d106a9eda7667d4da5218c95f";
            String pk3 = "5a583a7de9218e5bdb4cb4c9c1cc5ff65cb7dd66dc52977e2f74f5b3fd7d852d";
            //String libraryAddress = deployLibrary(url, pk);
            String libraryAddress = "0x4d7661b9b36ffdaed779f3e48f378311be9f413b";
            BigInteger ticket = createTicket(url, pk, libraryAddress);
            String ownerc = PublishTicket.deploy(pk, url, libraryAddress, 0.5f);
            approveSell(url, pk, ownerc, ticket, libraryAddress);
            buyTicket(pk2, 0.6f, url, ticket, ownerc);
            destroy(pk, url, ownerc);
            ownerc = PublishTicket.deploy(pk2, url, libraryAddress, 0.5f);
            approveSell(url, pk2, ownerc, ticket, libraryAddress);
            buyTicket(pk3, 1.4f, url, ticket, ownerc);
            destroy(pk2, url, ownerc);
//            System.out.println(ticketId);
//            String sellContract = PublishTicket.deploy(pk, url, libraryAddress, new BigInteger("30"));
//            System.out.println(sellContract);

        } catch (IOException ex) {
            throw new RuntimeException("Error whilst sending json-rpc requests", ex);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void destroy(String pk, String url, String ownerc) throws Exception {
        OwnerContractManager manager = new OwnerContractManager(ownerc, url, pk);
        manager.destroyContract();
    }

    public static void buyTicket(String pk, float amount, String url, BigInteger ticketid, String ownerc) throws Exception {
        TicketPaymentManager manager = new TicketPaymentManager(ownerc, url, pk);
        manager.buyTicket(ticketid, amount);
    }

    public static void approveSell(String url, String userPK, String sellContract, BigInteger ticketId, String ticketLibrary) throws Exception {
        TicketLibrary lib = new TicketLibrary(ticketLibrary, url,
                userPK);
        System.out.println(lib.approve(sellContract, ticketId));
    }

    public static BigInteger createTicket(String url, String userPK, String libraryAddress) throws Exception {
        TicketLibrary lib = new TicketLibrary(libraryAddress, url,
                userPK);
        lib.createTicket("https://game.example/item-id-8u5h2m.json");
        //Thread.sleep(2000);
        return lib.getLastTicketId();
    }


    public static String deployLibrary(String url, String ownerPK) throws Exception {
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
        return token.getContractAddress();
    }
}

