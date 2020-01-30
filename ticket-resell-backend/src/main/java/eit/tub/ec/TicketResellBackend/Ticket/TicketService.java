package eit.tub.ec.TicketResellBackend.Ticket;

import eit.tub.ec.TicketResellBackend.Contract.ContractService;
import eit.tub.ec.TicketResellBackend.Ticket.Exception.BlockchainTicketUpdateException;
import eit.tub.ec.TicketResellBackend.Ticket.Exception.TicketNotFoundException;
import eit.tub.ec.TicketResellBackend.Utils.APIPatch;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public void updateTicket(Ticket ticketUpdate, Ticket ticket) {

        if (ticketUpdate.getPrice() != null && !ticketUpdate.getPrice().equals(ticket.getPrice())) {
            if (ticket.getOnSale() && (ticketUpdate.getOnSale() != Boolean.FALSE)) {
                if (!contractService.changeTicketPrice(ticket)) {
                    throw new BlockchainTicketUpdateException();
                }
            } else {
                ticket.setPrice(ticketUpdate.getPrice());
            }
        }

        if (ticketUpdate.getOnSale() != null && (ticketUpdate.getOnSale() != ticket.getOnSale())) {
            if (ticketUpdate.getOnSale()) {
                contractService.offerTicketForSale(ticket);
            } else {
                contractService.cancelTicketForSale(ticket);
            }
        }

        APIPatch.merge(ticketUpdate, ticket);
    }
}
