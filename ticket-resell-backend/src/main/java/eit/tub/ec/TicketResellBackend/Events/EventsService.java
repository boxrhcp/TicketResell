package eit.tub.ec.TicketResellBackend.Events;

import eit.tub.ec.TicketResellBackend.Ticket.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class EventsService {

    @Autowired
    EventsRepository eventsRepository;
}
