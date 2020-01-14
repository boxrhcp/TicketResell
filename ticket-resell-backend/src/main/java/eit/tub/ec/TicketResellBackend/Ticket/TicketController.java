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
    public ResponseEntity<?> getTickets() {

        return ResponseEntity.status(HttpStatus.OK).body(ticketRepository.findAll());
    }

    @RequestMapping(value = "/tickets/{id}", method = RequestMethod.PATCH)
    public ResponseEntity<?> updateTicketOnSale(@PathVariable Long id, @RequestBody Ticket ticketUpdate) {

        if (ticketUpdate.getId() == null || !id.equals(ticketUpdate.getId())) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new APIError(HttpStatus.BAD_REQUEST, "The field ticketId can't be null and has to equal the ID in the URL path."));
        }

        Optional<Ticket> ticketOptional = ticketRepository.findById(ticketUpdate.getId());
        Ticket ticket = ticketOptional.orElse(null);

        if (ticket == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new APIError(HttpStatus.NOT_FOUND, "No ticket was found with the ticketId provided."));
        }

        ticket.setOnSale(ticketUpdate.isOnSale());
        ticket = ticketRepository.save(ticket);

        return ResponseEntity.status(HttpStatus.OK).body(ticket);
    }
}