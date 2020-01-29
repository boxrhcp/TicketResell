package eit.tub.ec.TicketResellBackend.Contract.Exception;

public class BlockchainTicketOwnerContractDestructionException extends RuntimeException {
    public BlockchainTicketOwnerContractDestructionException(String message, Long contractId, Long ownerId) {
        super(message + ". Could not destroy the OwnerContract " + contractId + ". " +
                "Therefore, the owner/seller " + ownerId + " did not receive its money.");
    }
}
