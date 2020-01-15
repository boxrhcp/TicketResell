package eit.tub.ec.TicketResellBackend.Ethereum;

import java.io.IOException;
import java.math.BigDecimal;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthBlockNumber;
import org.web3j.protocol.core.methods.response.EthGasPrice;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;

public class Connection {

    public static void main(String[] args) {
        System.out.println("Connecting to Ethereum ...");
        Web3j web3 = Web3j.build(new HttpService("http://52.29.8.175:8545"));
        System.out.println("Successfuly connected to Ethereum");

        try {
            // web3_clientVersion returns the current client version.
            Web3ClientVersion clientVersion = web3.web3ClientVersion().send();

            // eth_blockNumber returns the number of most recent block.
            EthBlockNumber blockNumber = web3.ethBlockNumber().send();

            // eth_gasPrice, returns the current price per gas in wei.
            EthGasPrice gasPrice = web3.ethGasPrice().send();
            EthGetBalance balanceWei = web3.ethGetBalance("0x1dfA623df28d8aeC808E534Eb48A84F1bC2bE6F2",
                    DefaultBlockParameterName.LATEST).send();
            System.out.println("balance in wei: " + balanceWei.getBalance());

            //BigDecimal balanceInEther = Convert.fromWei(balanceWei.getBalance().toString(), Unit.ETHER);

            // Print result
            System.out.println("Client version: " + clientVersion.getWeb3ClientVersion());
            System.out.println("Block number: " + blockNumber.getBlockNumber());
            System.out.println("Gas price: " + gasPrice.getGasPrice());

        } catch (IOException ex) {
            throw new RuntimeException("Error whilst sending json-rpc requests", ex);
        }
    }
}
