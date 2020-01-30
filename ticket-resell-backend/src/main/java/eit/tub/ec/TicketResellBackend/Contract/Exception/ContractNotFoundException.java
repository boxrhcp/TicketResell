package eit.tub.ec.TicketResellBackend.Contract.Exception;

public class ContractNotFoundException extends RuntimeException {
    public ContractNotFoundException(Long id) {
        super("Could not find contract " + id);
    }
}
