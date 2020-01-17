package eit.tub.ec.TicketResellBackend.Events;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//


import eit.tub.ec.TicketResellBackend.Home.Home;
import eit.tub.ec.TicketResellBackend.Ticket.Ticket;
import eit.tub.ec.TicketResellBackend.Utils.APIError;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EventsController {
    private EventsRepository eventsRepository;

    public EventsController(EventsRepository eventsRepository) { this.eventsRepository = eventsRepository; }

    @RequestMapping(
            value = {"/Events"},
            method = {RequestMethod.GET}
    )
    public ResponseEntity<?> getEvents() {
        return ResponseEntity.status(HttpStatus.OK).body(this.eventsRepository.findAll());
    }

    @RequestMapping(
            value = {"/Events/{id}"},
            method = {RequestMethod.GET}
    )
    public ResponseEntity<?> getEventsById(@PathVariable Long id) {
        Optional<Events> eventsOptional = this.eventsRepository.findById(id);
        Events events = eventsOptional.orElse(null);

        if (events == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new APIError(HttpStatus.NOT_FOUND, "No event was found with the eventId provided."));
        }
        return ResponseEntity.status(HttpStatus.OK).body(events);
}
}
