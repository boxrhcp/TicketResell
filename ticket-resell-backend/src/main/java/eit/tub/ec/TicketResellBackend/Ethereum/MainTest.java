package eit.tub.ec.TicketResellBackend.Ethereum;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Future;

import eit.tub.ec.TicketResellBackend.Contracts.OwnerContract;
import eit.tub.ec.TicketResellBackend.Contracts.Tickets;
import eit.tub.ec.TicketResellBackend.Ticket.Ticket;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.protocol.http.HttpService;
import org.web3j.tuples.generated.Tuple3;
import org.web3j.tx.FastRawTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.tx.response.NoOpProcessor;
import org.web3j.tx.response.PollingTransactionReceiptProcessor;
import org.web3j.utils.Convert;

public class MainTest {

    public static void main(String[] args) {
        System.out.println("Connecting to Ethereum ...");
        String url = "http://18.194.30.246:8545";
        System.out.println("Successfuly connected to Ethereum");
        String libraryAddress = "0x0d2139319b5473d7b5bcbd6eae5d92a982532ad8";

        try {
            String pk = "0xde59ac089c1221236b6441dd9c53d0514c9d6c38a0446b01cfb32dc4f62c4f8d"; // Add a private key here
            //deployLibrary(url,pk);
            createTicket(url, pk);
            //System.out.println(ticketId);
            String sellContract = PublishTicket.deploy(pk, url, libraryAddress, new BigInteger("30"));
            System.out.println(sellContract);

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

