package eit.tub.ec.TicketResellBackend.Event;

import eit.tub.ec.TicketResellBackend.Ticket.Ticket;
import eit.tub.ec.TicketResellBackend.Ticket.TicketRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class EventService {
    private EventRepository eventRepository;
    private TicketRepository ticketRepository;

    public EventService (EventRepository eventRepository, TicketRepository ticketRepository) {
        this.eventRepository = eventRepository;
        this.ticketRepository = ticketRepository;
    }

    @Transactional
    public Event createEvent(Event event) {

        Event savedEvent = eventRepository.save(event);

        Ticket ticket;
        for (int i = 0; i < event.getNTickets(); i++) {
            ticket = new Ticket();
            ticket.setEventId(event.getId());
            ticket.setPrice(event.getPrice());
            ticket.setOwnerId(event.getOrganizerId());
            ticketRepository.save(ticket);
        }

        return savedEvent;
    }
}
