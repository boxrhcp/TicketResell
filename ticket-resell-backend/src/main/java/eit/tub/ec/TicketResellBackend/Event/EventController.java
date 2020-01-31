package eit.tub.ec.TicketResellBackend.Event;

import eit.tub.ec.TicketResellBackend.Contract.Exception.BlockchainTicketApprovalException;
import eit.tub.ec.TicketResellBackend.Contract.Exception.BlockchainTicketCreationException;
import eit.tub.ec.TicketResellBackend.Contract.Exception.BlockchainTicketPublishingException;
import eit.tub.ec.TicketResellBackend.User.User;
import eit.tub.ec.TicketResellBackend.User.UserService;
import eit.tub.ec.TicketResellBackend.Utils.APIError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class EventController {
    private EventRepository eventRepository;
    private EventService eventService;
    private UserService userService;

    public EventController(EventRepository eventRepository, EventService eventService, UserService userService) {
        this.eventRepository = eventRepository;
        this.eventService = eventService;
        this.userService = userService;
    }

    @RequestMapping(value = {"/events"}, method = {RequestMethod.GET})
    public ResponseEntity<?> getEvents() {
        return ResponseEntity.status(HttpStatus.OK).body(this.eventRepository.findAll());
    }

    @RequestMapping(value = "/events/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getEventById(@PathVariable Long id) {
        Optional<Event> event = eventRepository.findById(id);

        if (event.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(event.get());
        } else {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new APIError(HttpStatus.NOT_FOUND, "No event was found with the path ID provided."));
        }
    }

    @RequestMapping(value = "/events", method = RequestMethod.POST)
    public ResponseEntity<?> postEvent(@RequestBody Event event) {

        if (event.getName() == null
                || event.getDate() == null
                || event.getPlace() == null
                || event.getPrice() == null
                || event.getNTickets() == null
                || event.getOrganizerId() == null) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new APIError(HttpStatus.BAD_REQUEST, "The fields name, date, place, price, ntickets, or organizerId can't be null."));
        }

        User organizer = userService.findById(event.getOrganizerId());
        if (!organizer.isOrganizer()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new APIError(HttpStatus.BAD_REQUEST, "Only organizers can create events."));
        }


        Event createdEvent;
        ResponseEntity<?> response;
        try {
            createdEvent = eventService.createEvent(event);
            response = ResponseEntity.status(HttpStatus.CREATED).body(createdEvent);
        } catch (BlockchainTicketCreationException
                | BlockchainTicketPublishingException
                | BlockchainTicketApprovalException e) {
            response = ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIError(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
        }

        return response;
    }
}
