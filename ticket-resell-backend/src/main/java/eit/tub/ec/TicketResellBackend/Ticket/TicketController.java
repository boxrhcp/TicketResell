package eit.tub.ec.TicketResellBackend.Ticket;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<?> updateTicketOnSale(@PathVariable Long id, @RequestBody Ticket ticket) {

        if (ticket.getId() == null || !id.equals(ticket.getId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        Ticket updatedTicket = ticketRepository.save(ticket);

        return ResponseEntity.status(HttpStatus.CREATED).body(updatedTicket);
    }
}