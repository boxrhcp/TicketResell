package eit.tub.ec.TicketResellBackend.Ticket.Exception;

public class TicketNotOnSaleException extends RuntimeException {
    public TicketNotOnSaleException(Long id) {
        super("The ticket " + id + " is not on sale.");
    }
}
