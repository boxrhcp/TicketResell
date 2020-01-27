package eit.tub.ec.TicketResellBackend.Ticket;

import eit.tub.ec.TicketResellBackend.Utils.APIError;
import eit.tub.ec.TicketResellBackend.Utils.APIPatch;
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
    public ResponseEntity<?> patchTicket(@PathVariable Long id, @RequestBody Ticket ticketUpdate) {
        Optional<Ticket> ticketOptional = ticketRepository.findById(id);
        Ticket ticket = null;

        if (!ticketOptional.isPresent()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new APIError(HttpStatus.NOT_FOUND, "No ticket was found with the id provided."));
        } else {
            ticket = ticketOptional.get();
        }

        APIPatch.merge(ticketUpdate, ticket);

        ticket = ticketRepository.save(ticket);

        return ResponseEntity.status(HttpStatus.OK).body(ticket);
    }
}