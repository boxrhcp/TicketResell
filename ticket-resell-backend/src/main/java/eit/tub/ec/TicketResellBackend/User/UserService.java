package eit.tub.ec.TicketResellBackend.User;

import eit.tub.ec.TicketResellBackend.User.Exception.UserNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class UserService {
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findById(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        return userOptional.orElseThrow(
                () -> new UserNotFoundException(userId));
    }
}
