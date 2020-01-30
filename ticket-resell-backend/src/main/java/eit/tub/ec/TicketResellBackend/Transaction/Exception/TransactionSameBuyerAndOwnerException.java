package eit.tub.ec.TicketResellBackend.Transaction.Exception;

public class TransactionSameBuyerAndOwnerException extends RuntimeException {
    public TransactionSameBuyerAndOwnerException() {
        super("Can't purchase from oneself. Owner and buyer have to be different users.");
    }
}
