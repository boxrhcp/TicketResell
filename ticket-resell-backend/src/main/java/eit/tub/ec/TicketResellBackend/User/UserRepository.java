package eit.tub.ec.TicketResellBackend.User;


import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
    Iterable<User> findByOrganizer(Boolean organizer);
}
