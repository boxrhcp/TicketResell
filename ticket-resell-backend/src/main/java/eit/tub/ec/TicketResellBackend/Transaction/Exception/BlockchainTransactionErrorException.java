package eit.tub.ec.TicketResellBackend.Transaction.Exception;

public class BlockchainTransactionErrorException extends RuntimeException {
    public BlockchainTransactionErrorException(String message) {
        super(message + ". Blockchain payment transaction could not be carried out.");
    }
}
