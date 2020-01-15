package eit.tub.ec.TicketResellBackend.Ethereum;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import eit.tub.ec.TicketResellBackend.Ethereum.Contracts.SimpleStorage;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.FastRawTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.tx.response.NoOpProcessor;
import org.web3j.tx.response.PollingTransactionReceiptProcessor;
import org.web3j.utils.Convert;

public class Connection {

    public static void main(String[] args) {
        System.out.println("Connecting to Ethereum ...");
        Web3j web3j = Web3j.build(new HttpService("http://52.29.8.175:8545"));
        System.out.println("Successfuly connected to Ethereum");

        try {
            // web3_clientVersion returns the current client version.
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
            System.out.println("Gas price: " + gasPrice.getGasPrice());

            // opening account
            String walletPassword = "eitdigital";
            String walletDirectory = "src/main/resources/wallets";
            String walletName = "UTC--2020-01-14T13-31-40.837617026Z--1dfa623df28d8aec808e534eb48a84f1bc2be6f2";

            // Load the JSON encryted wallet
            Credentials credentials = WalletUtils.loadCredentials(walletPassword, walletDirectory + "/" + walletName);

            // Get the account address
            PollingTransactionReceiptProcessor processor = new PollingTransactionReceiptProcessor(web3j,3000000l,3);
            //deploy new contract
            TransactionManager txManager = new FastRawTransactionManager(web3j, credentials, processor);

            RemoteCall<SimpleStorage> request = SimpleStorage.deploy(web3j, txManager,
                    new DefaultGasProvider() {
                        @Override
                        public BigInteger getGasPrice(String contractFunc) {
                            switch (contractFunc) {
                                case SimpleStorage.FUNC_GET:
                                    return BigInteger.valueOf(22_000_000_000L);
                                case SimpleStorage.FUNC_SET:
                                    return BigInteger.valueOf(44_000_000_000L);
                                default:
                                    return BigInteger.valueOf(44_000_000_000L);
                            }
                        }

                        @Override
                        public BigInteger getGasLimit(String contractFunc) {
                            switch (contractFunc) {
                                case SimpleStorage.FUNC_GET:
                                    return BigInteger.valueOf(4_300_000);
                                case SimpleStorage.FUNC_SET:
                                    return BigInteger.valueOf(5_300_000);
                                default:
                                    return BigInteger.valueOf(5_300_000);
                            }
                        }
                    });
            SimpleStorage token = request.send();
            // load existing contract by address


            // create transaction transfer token to receiver
            BigInteger value = new BigInteger("2");
            TransactionReceipt receipt = token.set(value).send();
            // get transaction result
            System.out.println(receipt.getTransactionHash());


        } catch (IOException ex) {
            throw new RuntimeException("Error whilst sending json-rpc requests", ex);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
