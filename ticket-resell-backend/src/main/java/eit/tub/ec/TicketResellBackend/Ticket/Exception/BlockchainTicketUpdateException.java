package eit.tub.ec.TicketResellBackend.Ticket.Exception;

public class BlockchainTicketUpdateException extends RuntimeException {
    public BlockchainTicketUpdateException() {
        super("Blockchain ticket update did not finish successfully.");
    }
}
