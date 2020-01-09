package eit.tub.ec.TicketResellBackend.Home;

import org.springframework.stereotype.Service;

@Service
public class HomeService {
    /**
     * This kind of class is just to implement the business logic.
     * It is useful to avoid having a lot of business logic in the Controller class, which should be for
     * things related to the endpoint.
     * Usually services call the repositories and do stuff with the data, retrieve it process it and return it to the
     * controller or the other way around, receive data from the Controller, process it, and store it using the
     * repository.
     */
}