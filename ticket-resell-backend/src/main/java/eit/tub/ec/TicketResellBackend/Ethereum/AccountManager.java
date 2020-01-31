package eit.tub.ec.TicketResellBackend.Ethereum;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

public class AccountManager {

    private Web3j web3j;

    /**
     * @param url to connect to ethereum
     */
    public AccountManager(String url) {
        web3j = Web3j.build(new HttpService(url));
    }

    /**
     * Get the account balance in Ether
     *
     * @param account user we want to check balance from
     * @return user balance in ETHER
     * @throws IOException if call goes wrong
     */
    public BigDecimal getAccountBalance(String account) throws IOException {
        return Convert.fromWei(new BigDecimal(web3j.ethGetBalance(
                account, DefaultBlockParameterName.LATEST).send().getBalance()),
                Convert.Unit.ETHER);
    }

}
