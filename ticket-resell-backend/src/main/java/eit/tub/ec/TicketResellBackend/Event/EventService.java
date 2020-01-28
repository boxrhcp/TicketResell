package eit.tub.ec.TicketResellBackend.Event;

import eit.tub.ec.TicketResellBackend.Ethereum.ContractRepository;
import eit.tub.ec.TicketResellBackend.Ethereum.ContractService;
import eit.tub.ec.TicketResellBackend.Ethereum.Exception.BlockchainTicketCreationException;
import eit.tub.ec.TicketResellBackend.Ticket.Ticket;
import eit.tub.ec.TicketResellBackend.Ticket.TicketRepository;
import eit.tub.ec.TicketResellBackend.User.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class EventService {
    private ContractService contractService;
    private EventRepository eventRepository;
    private TicketRepository ticketRepository;
    private UserRepository userRepository;
    private ContractRepository contractRepository;

    public EventService (
            ContractService contractService,
            EventRepository eventRepository,
            TicketRepository ticketRepository,
            UserRepository userRepository,
            ContractRepository contractRepository) {
        this.contractService = contractService;
        this.eventRepository = eventRepository;
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
        this.contractRepository = contractRepository;
    }

    @Transactional
    public Event createEvent(Event event) {

        Event savedEvent = eventRepository.save(event);

        Ticket ticket;
        for (int i = 0; i < event.getNTickets(); i++) {
            ticket = createTicket(event.getId(), event.getOrganizerId(), event.getPrice());
            ticket = contractService.offerTicketForSale(ticket);
            ticketRepository.save(ticket);

            contractService.approveTicketForModification(ticket);
        }

        return savedEvent;
    }

    @Transactional
    private synchronized Ticket createTicket(Long eventId, Long organizerId, Float price)
            throws BlockchainTicketCreationException {
        Ticket ticket = new Ticket();
        ticket.setEventId(eventId);
        ticket.setOwnerId(organizerId);
        ticket.setPrice(price);
        ticket.setEthUri(String.valueOf(ticket.hashCode()));

        ticket = ticketRepository.save(ticket);
        ticket = contractService.createTicket(ticket);

        return ticketRepository.save(ticket);
    }
}
