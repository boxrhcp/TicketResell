package eit.tub.ec.TicketResellBackend.Ethereum.Exception;

public class BlockchainTicketPublishingException extends RuntimeException {

    public BlockchainTicketPublishingException(String message, Long id) {
        super(message + ". Could not put up on sale the ticket " + id);
    }
}
