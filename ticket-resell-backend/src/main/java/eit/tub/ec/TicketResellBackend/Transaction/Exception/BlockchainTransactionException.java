package eit.tub.ec.TicketResellBackend.Transaction.Exception;

public class BlockchainTransactionException extends RuntimeException {
    public BlockchainTransactionException(String message) {
        super(message + ". Blockchain payment transaction could not be carried out.");
    }
}
