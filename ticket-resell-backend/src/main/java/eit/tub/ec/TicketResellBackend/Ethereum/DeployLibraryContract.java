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

public class DeployLibraryContract {
    /**
     * Follow the README.md instructions to correctly assign the variables BLOCKCHAIN_URL and ADMIN_PK
     * and run the class to deploy and obtain the ticket library contract address that should be configured in the
     * configuration file application.properties.
     */
    private static final String BLOCKCHAIN_URL = "http://127.0.0.1:8545";
    private static final String ADMIN_PK = "1f0b2af9ab09c0213268e2c6e3fe6829c9c80ddbff450de2861b4519ef551f5d";

    public static void main(String[] args) {
        System.out.println("Deploying Ticket Library Contract to Ethereum ...");

        try {
            String address = PublishTicketLibrary.deployLibrary(BLOCKCHAIN_URL, ADMIN_PK);
            System.out.println("TicketLibrary Contract Address: " + address);

        } catch (IOException ex) {
            throw new RuntimeException("Error whilst sending json-rpc requests", ex);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

