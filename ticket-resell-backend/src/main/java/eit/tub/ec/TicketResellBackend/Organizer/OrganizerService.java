package eit.tub.ec.TicketResellBackend.Organizer;

import eit.tub.ec.TicketResellBackend.Ticket.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrganizerService {

    @Autowired
    OrganizerRepository organizerRepository;
}