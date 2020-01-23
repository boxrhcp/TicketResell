package eit.tub.ec.TicketResellBackend.Ticket;

import org.springframework.data.repository.CrudRepository;

public interface TicketRepository extends CrudRepository<Ticket, Long> {
    Iterable<Ticket> findByEventIdAndOnSale(Long eventId, Boolean onSale);
}