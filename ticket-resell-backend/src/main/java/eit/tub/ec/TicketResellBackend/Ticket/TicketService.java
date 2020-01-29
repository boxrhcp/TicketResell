package eit.tub.ec.TicketResellBackend.Ticket;

import eit.tub.ec.TicketResellBackend.Utils.APIPatch;
import org.springframework.stereotype.Service;


@Service
public class TicketService {

    public TicketService() {

    }

    public void update(Ticket ticketUpdate, Ticket ticket) {

        if (ticketUpdate.getPrice() != null && !ticketUpdate.getPrice().equals(ticket.getPrice())) {
            // TODO ticketOnSale==true? change contract price : change ticket price
        }

        if (ticketUpdate.isOnSale() != null && (ticketUpdate.isOnSale() != ticket.isOnSale())) {
            // TODO updateOnSale==true? deploy contract : destroy contract
        }

        APIPatch.merge(ticketUpdate, ticket);
    }
}
