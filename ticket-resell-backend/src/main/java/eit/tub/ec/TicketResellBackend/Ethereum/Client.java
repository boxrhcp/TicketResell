package eit.tub.ec.TicketResellBackend.Ethereum;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

public class Client {

    private Web3j web3j;

    public Client(){
        this.web3j = Web3j.build(new HttpService("http://localhost:8545"));
    }
}
