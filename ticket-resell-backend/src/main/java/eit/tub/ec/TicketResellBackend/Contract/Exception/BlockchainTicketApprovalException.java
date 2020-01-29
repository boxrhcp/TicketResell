package eit.tub.ec.TicketResellBackend.Contract.Exception;

public class BlockchainTicketApprovalException extends RuntimeException {

    public BlockchainTicketApprovalException(String message, Long id) {
        super(message + ". Could not approve the ticket " + id + " in the blockchain.");
    }
}
