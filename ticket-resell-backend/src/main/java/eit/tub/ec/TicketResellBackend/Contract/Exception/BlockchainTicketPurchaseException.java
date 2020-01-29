package eit.tub.ec.TicketResellBackend.Contract.Exception;

public class BlockchainTicketPurchaseException extends RuntimeException {
    public BlockchainTicketPurchaseException(String message, Long ticketId, Long buyerId) {
        super(message + ". Could not complete the ticket purchase transaction. Buyer " + buyerId + " could not buy ticket " + ticketId);
    }
}
