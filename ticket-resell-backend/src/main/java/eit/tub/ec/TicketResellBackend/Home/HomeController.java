package eit.tub.ec.TicketResellBackend.Home;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class HomeController {

    /**
     * To make use of services and repositories it's enough to annotate the class with "@Autowired". By doing that
     * it's not necessary to initialize anything, just call the methods of the class.
     */
    @Autowired
    HomeRepository homeRepository;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index() {
        return "Greetings from Spring Boot!. Check the /homes and /homes/{id} endpoints!";
    }

    @RequestMapping(value = "/homes", method = RequestMethod.GET)
    public Iterable<Home> getHomes(@RequestParam(value="id", defaultValue="1") Long id) {
        Iterable<Home> homes = homeRepository.findAll();
        return homes;
    }

    @RequestMapping(value = "/homes/{id}", method = RequestMethod.GET)
    public Home getHomeById(@PathVariable Long id) {
        Optional<Home> home = homeRepository.findById(id);
        return home.orElseGet(Home::new);
    }
}