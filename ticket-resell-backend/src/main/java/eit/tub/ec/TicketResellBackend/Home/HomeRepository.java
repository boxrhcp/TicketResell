package eit.tub.ec.TicketResellBackend.Home;

import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface HomeRepository extends CrudRepository<Home, Long> {

    List<Home> findByFirstName(String name);

    List<Home> findByLastName(String lastName);
}