package eit.tub.ec.TicketResellBackend.Transaction.Exception;

public class BlockchainTransactionErrorException extends RuntimeException {
    public BlockchainTransactionErrorException() {
        super("Blockchain payment transaction could not be carried out.");
    }
}
