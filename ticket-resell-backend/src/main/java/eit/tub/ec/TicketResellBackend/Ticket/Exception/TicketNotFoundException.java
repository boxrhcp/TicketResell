package eit.tub.ec.TicketResellBackend.Ticket.Exception;

public class TicketNotFoundException extends RuntimeException {
    public TicketNotFoundException(Long id) {
        super("Could not find ticket " + id);
    }
}
