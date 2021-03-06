package eit.tub.ec.TicketResellBackend.Event;

import eit.tub.ec.TicketResellBackend.Contract.ContractService;
import eit.tub.ec.TicketResellBackend.Contract.Exception.BlockchainTicketCreationException;
import eit.tub.ec.TicketResellBackend.Ticket.Ticket;
import eit.tub.ec.TicketResellBackend.Ticket.TicketRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class EventService {
    private ContractService contractService;
    private EventRepository eventRepository;
    private TicketRepository ticketRepository;

    public EventService (
            ContractService contractService,
            EventRepository eventRepository,
            TicketRepository ticketRepository) {
        this.contractService = contractService;
        this.eventRepository = eventRepository;
        this.ticketRepository = ticketRepository;
    }

    @Transactional
    public Event createEvent(Event event) {

        Event savedEvent = eventRepository.save(event);

        Ticket ticket;
        for (int i = 0; i < event.getNTickets(); i++) {
            ticket = createTicket(event.getId(), event.getOrganizerId(), event.getPrice());
            contractService.offerTicketForSale(ticket);
            ticketRepository.save(ticket);
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
        ticket.setOnSale(Boolean.TRUE);
        ticket.setEthUri(String.valueOf(ticket.hashCode()));

        ticket = ticketRepository.save(ticket);
        ticket = contractService.createTicket(ticket);

        return ticketRepository.save(ticket);
    }
}
