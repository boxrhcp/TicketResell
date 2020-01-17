package eit.tub.ec.TicketResellBackend.Events;

import eit.tub.ec.TicketResellBackend.Home.Home;
import org.springframework.data.repository.CrudRepository;
import java.util.List;


public interface EventsRepository extends CrudRepository<Events, Long> {}

