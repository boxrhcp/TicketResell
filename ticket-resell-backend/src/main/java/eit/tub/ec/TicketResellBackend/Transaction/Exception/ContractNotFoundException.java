package eit.tub.ec.TicketResellBackend.Transaction.Exception;

public class ContractNotFoundException extends RuntimeException {
    public ContractNotFoundException(Long id) {
        super("Could not find contract " + id);
    }
}
