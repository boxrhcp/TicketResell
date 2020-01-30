package eit.tub.ec.TicketResellBackend.Ticket;

import eit.tub.ec.TicketResellBackend.Contract.ContractService;
import eit.tub.ec.TicketResellBackend.Ticket.Exception.BlockchainTicketUpdateException;
import eit.tub.ec.TicketResellBackend.Ticket.Exception.TicketNotFoundException;
import eit.tub.ec.TicketResellBackend.Utils.APIPatch;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class TicketService {
    private ContractService contractService;
    private TicketRepository ticketRepository;

    public TicketService(ContractService contractService, TicketRepository ticketRepository) {
        this.contractService = contractService;
        this.ticketRepository = ticketRepository;
    }

    public Ticket findById(Long ticketId) {
        Optional<Ticket> ticketOptional = ticketRepository.findById(ticketId);
        return ticketOptional.orElseThrow(
                () -> new TicketNotFoundException(ticketId));
    }

    public void update(Ticket ticketUpdate, Ticket ticket) {

        if (ticketUpdate.getPrice() != null && !ticketUpdate.getPrice().equals(ticket.getPrice())) {
            // TODO ticketOnSale==true? change contract price : change ticket price
        }

        if (ticketUpdate.isOnSale() != null && (ticketUpdate.isOnSale() != ticket.isOnSale())) {
            // TODO updateOnSale==true? deploy contract : destroy contract
        }

        APIPatch.merge(ticketUpdate, ticket);
    }
}
