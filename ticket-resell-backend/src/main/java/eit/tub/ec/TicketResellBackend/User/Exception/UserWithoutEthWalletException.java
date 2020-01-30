package eit.tub.ec.TicketResellBackend.User.Exception;

public class UserWithoutEthWalletException extends RuntimeException {
    public UserWithoutEthWalletException(Long userId) {
        super("The user " + userId + " does not have an Ethereum wallet.");
    }
}
