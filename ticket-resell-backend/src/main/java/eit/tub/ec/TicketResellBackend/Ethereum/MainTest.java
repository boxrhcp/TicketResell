package eit.tub.ec.TicketResellBackend.Ethereum;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import eit.tub.ec.TicketResellBackend.Ethereum.Contracts.TicketResell;
import eit.tub.ec.TicketResellBackend.Ticket.Ticket;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.FastRawTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.tx.response.NoOpProcessor;
import org.web3j.tx.response.PollingTransactionReceiptProcessor;
import org.web3j.utils.Convert;

public class MainTest {

    public static void main(String[] args) {
        System.out.println("Connecting to Ethereum ...");
        Web3j web3j = Web3j.build(new HttpService("http://localhost:8545"));
        System.out.println("Successfuly connected to Ethereum");

        try {
            /*// web3_clientVersion returns the current client version.
            Web3ClientVersion clientVersion = web3j.web3ClientVersion().send();

            // eth_blockNumber returns the number of most recent block.
            EthBlockNumber blockNumber = web3j.ethBlockNumber().send();

            // eth_gasPrice, returns the current price per gas in wei.
            EthGasPrice gasPrice = web3j.ethGasPrice().send();
            EthGetBalance balanceWei = web3j.ethGetBalance("0x1dfA623df28d8aeC808E534Eb48A84F1bC2bE6F2",
                    DefaultBlockParameterName.LATEST).send();
            System.out.println("balance in wei: " + balanceWei.getBalance());

            //BigDecimal balanceInEther = Convert.fromWei(balanceWei.getBalance().toString(), Unit.ETHER);

            // Print result
            System.out.println("Client version: " + clientVersion.getWeb3ClientVersion());
            System.out.println("Block number: " + blockNumber.getBlockNumber());
            System.out.println("Gas price: " + gasPrice.getGasPrice());*/

            // Load the JSON encryted wallet
            String pk = "0c98cb8029bc1a2b970b322604cde7d2106d90c170573eb0e0306ac4bd0a2351"; // Add a private key here

            // Decrypt and open the wallet into a Credential object
            Credentials credentials = Credentials.create(pk);
            // Get the account address
            PollingTransactionReceiptProcessor processor = new PollingTransactionReceiptProcessor(web3j,5000l,5);
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
            String contract = token.getContractAddress();
            System.out.println(contract);
            System.out.println(token.getTransactionReceipt());

            // create transaction transfer token to receiver
            BigInteger value = new BigInteger("30");
            TransactionReceipt receipt = token.createTicket(
                    "test", value).send();
            // get transaction result
            System.out.println(receipt.getTransactionHash());
            receipt = token.getTicketInfo("test").send();
            System.out.println(receipt.getTransactionHash());
            //loadTest(contract, pk);
        } catch (IOException ex) {
            throw new RuntimeException("Error whilst sending json-rpc requests", ex);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadTest(String contract, String userPK){
        Web3j web3j = Web3j.build(new HttpService("http://localhost:8545"));
        PollingTransactionReceiptProcessor processor = new PollingTransactionReceiptProcessor(web3j,5000l
                ,5);
        Credentials credentials = Credentials.create(userPK);
        TransactionManager txManager = new FastRawTransactionManager(web3j, credentials, processor);
        TicketResell token = TicketResell.load(contract, web3j, txManager,
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
        //TransactionReceipt receipt = token.getTicketInfo("test").send();
        //System.out.println(receipt.setStatus());
    }
}