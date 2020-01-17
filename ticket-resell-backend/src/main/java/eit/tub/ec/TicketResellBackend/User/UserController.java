package eit.tub.ec.TicketResellBackend.User;

import eit.tub.ec.TicketResellBackend.User.User;
import eit.tub.ec.TicketResellBackend.User.UserRepository;
import eit.tub.ec.TicketResellBackend.Utils.APIError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;


@RestController
public class UserController {
    private UserRepository userRepository;

    public UserController(UserRepository userRepository) { this.userRepository = userRepository; }

    @RequestMapping(
            value = {"/User"},
            method = {RequestMethod.GET})
    public ResponseEntity<?> getEvents() {
        return ResponseEntity.status(HttpStatus.OK).body(this.userRepository.findAll()); }

        @RequestMapping(
                value = {"/User/{id}"},
                method = {RequestMethod.GET}
        )
        public ResponseEntity<?> getEventsById(@PathVariable Long id) {
            Optional<User> userOptional = this.userRepository.findById(id);
            User user = userOptional.orElse(null);

            if (user == null) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(new APIError(HttpStatus.NOT_FOUND, "No user was found with the eventId provided."));
            }
            return ResponseEntity.status(HttpStatus.OK).body(user);
        }
    }


