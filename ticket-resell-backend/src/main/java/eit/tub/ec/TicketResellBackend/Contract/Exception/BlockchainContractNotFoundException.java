package eit.tub.ec.TicketResellBackend.Contract.Exception;

public class BlockchainContractNotFoundException extends RuntimeException {
    public BlockchainContractNotFoundException(Long id) {
        super("Could not find contract " + id);
    }
}
