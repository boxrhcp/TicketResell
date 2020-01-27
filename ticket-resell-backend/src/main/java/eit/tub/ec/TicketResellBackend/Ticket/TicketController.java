package eit.tub.ec.TicketResellBackend.Ticket;

import eit.tub.ec.TicketResellBackend.Utils.APIError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class TicketController {
    private TicketRepository ticketRepository;

    public TicketController(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @RequestMapping(value = "/tickets", method = RequestMethod.GET)
    public ResponseEntity<?> getTickets(
            @RequestParam(required = false) Long eventId,
            @RequestParam(required = false) Boolean onSale) {

        Iterable<Ticket> tickets;
        if (eventId != null && onSale != null) {
            tickets = ticketRepository.findByEventIdAndOnSale(eventId, onSale);
        } else {
            tickets = ticketRepository.findAll();
        }

        return ResponseEntity.status(HttpStatus.OK).body(tickets);
    }

    @RequestMapping(value = "/tickets/{id}", method = RequestMethod.PATCH)
    public ResponseEntity<?> patchTicket(@PathVariable Long id, @RequestBody Ticket ticket) {

        if (ticket.getId() == null || !id.equals(ticket.getId())) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new APIError(HttpStatus.BAD_REQUEST, "The field ticketId can't be null and has to equal the ID in the URL path."));
        }

        if (!ticketRepository.existsById(ticket.getId())) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new APIError(HttpStatus.NOT_FOUND, "No ticket was found with the ticketId provided."));
        }

        ticket = ticketRepository.save(ticket);

        return ResponseEntity.status(HttpStatus.OK).body(ticket);
    }
}