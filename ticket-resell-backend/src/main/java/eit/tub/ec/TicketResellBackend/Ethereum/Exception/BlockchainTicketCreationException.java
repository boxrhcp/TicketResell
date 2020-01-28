package eit.tub.ec.TicketResellBackend.Ethereum.Exception;

public class BlockchainTicketCreationException extends RuntimeException {

    public BlockchainTicketCreationException(String message, Long id, Long eventId, Long organizerId, Float price) {
        super(message + ". Could not create the ticket " +
                "{id:" + id + ", eventId:" + eventId + ", organizerId:" + organizerId + ", price:" + price + "} " +
                "in the blockchain.");
    }
}
