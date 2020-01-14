package eit.tub.ec.TicketResellBackend.Home;

import eit.tub.ec.TicketResellBackend.Utils.APIError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class HomeController {
    private HomeRepository homeRepository;

    public HomeController(HomeRepository homeRepository) {
        this.homeRepository = homeRepository;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity<?> index() {
        return ResponseEntity.status(HttpStatus.OK)
                .body("Greetings from Spring Boot!. Check the /homes and /homes/{id} endpoints!");
    }

    @RequestMapping(value = "/homes", method = RequestMethod.GET)
    public ResponseEntity<?> getHomes() {
        return ResponseEntity.status(HttpStatus.OK).body(homeRepository.findAll());
    }

    @RequestMapping(value = "/homes/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getHomeById(@PathVariable Long id) {
        Optional<Home> home = homeRepository.findById(id);

        if (home.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(home.get());
        } else {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new APIError(HttpStatus.NOT_FOUND, "No home was found with the path ID provided."));
        }
    }
}