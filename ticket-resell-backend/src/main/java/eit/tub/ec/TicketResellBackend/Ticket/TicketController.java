package eit.tub.ec.TicketResellBackend.Ticket;

import eit.tub.ec.TicketResellBackend.Ticket.Exception.BlockchainTicketUpdateException;
import eit.tub.ec.TicketResellBackend.Utils.APIError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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
            @RequestParam(required = false) Long ownerId,
            @RequestParam(required = false) Long eventId,
            @RequestParam(required = false) Boolean onSale) {

        Iterable<Ticket> tickets = ticketRepository.findAll();

        List<Ticket> filteredTickets = StreamSupport
                .stream(tickets.spliterator(), false)
                .filter(ticket -> {
                    boolean pass = true;

                    if (ownerId != null)
                        pass =  pass && ticket.getOwnerId().equals(ownerId);

                    if (eventId != null)
                        pass = pass && ticket.getEventId().equals(eventId);

                    if (onSale != null)
                        pass = pass && (ticket.getOnSale() == onSale);

                    return pass;
                })
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(filteredTickets);
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