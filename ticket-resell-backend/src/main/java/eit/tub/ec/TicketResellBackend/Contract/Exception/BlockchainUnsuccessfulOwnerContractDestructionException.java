package eit.tub.ec.TicketResellBackend.Contract.Exception;

public class BlockchainUnsuccessfulOwnerContractDestructionException extends RuntimeException {
    public BlockchainUnsuccessfulOwnerContractDestructionException(Long contractId, Long ownerId) {
        super(". Could not destroy the OwnerContract " + contractId + ". " +
                "Therefore, the owner/seller " + ownerId + " did not receive its money.");
    }
}
