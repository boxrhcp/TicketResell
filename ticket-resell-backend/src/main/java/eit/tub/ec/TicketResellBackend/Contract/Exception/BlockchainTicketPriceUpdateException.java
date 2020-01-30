package eit.tub.ec.TicketResellBackend.Contract.Exception;

public class BlockchainTicketPriceUpdateException extends RuntimeException {
    public BlockchainTicketPriceUpdateException(String message, Long ticketId, Float price) {
        super(message + ". Could not update the ticket " + ticketId + " price to " + price + ".");
    }
}
