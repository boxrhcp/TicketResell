package eit.tub.ec.TicketResellBackend.Home;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class HomeController {

    /**
     * To make use of services and repositories it's enough to annotate the class with "@Autowired". By doing that
     * it's not necessary to initialize anything, just call the methods of the class.
     */
    @Autowired
    private HomeRepository homeRepository;

    private static final String template = "Hello, %s!";

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index() {
        return "Greetings from Spring Boot!";
    }

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public Home home(@RequestParam(value="id", defaultValue="1") Long id) {
        Optional<Home> home = homeRepository.findById(id);
        return home.get();
    }
}