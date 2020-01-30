package eit.tub.ec.TicketResellBackend.Ticket;

import eit.tub.ec.TicketResellBackend.Ticket.Exception.BlockchainTicketUpdateException;
import eit.tub.ec.TicketResellBackend.Utils.APIError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class TicketController {
    private TicketService ticketService;
    private TicketRepository ticketRepository;

    public TicketController(TicketService ticketService, TicketRepository ticketRepository) {
        this.ticketService = ticketService;
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
        Ticket ticket;

        if (!ticketOptional.isPresent()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new APIError(HttpStatus.NOT_FOUND, "No ticket was found with the id provided."));
        } else {
            ticket = ticketOptional.get();
        }

        ResponseEntity<?> response;
        try {
            ticketService.updateTicket(ticketUpdate, ticket);
            ticket = ticketRepository.save(ticket);
            response = ResponseEntity.status(HttpStatus.OK).body(ticket);
        } catch (BlockchainTicketUpdateException e) {
            response = ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIError(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
        }

        return response;
    }
}