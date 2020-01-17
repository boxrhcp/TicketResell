package eit.tub.ec.TicketResellBackend.User;


import eit.tub.ec.TicketResellBackend.Events.EventsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
}
