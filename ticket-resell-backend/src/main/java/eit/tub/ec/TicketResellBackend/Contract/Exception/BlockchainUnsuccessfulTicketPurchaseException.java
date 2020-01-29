package eit.tub.ec.TicketResellBackend.Contract.Exception;

public class BlockchainUnsuccessfulTicketPurchaseException extends RuntimeException {
    public BlockchainUnsuccessfulTicketPurchaseException(Long ticketId, Long buyerId) {
        super("Unsuccessful ticket purchase transaction. Buyer " + buyerId + " could not buy ticket " + ticketId);
    }
}
