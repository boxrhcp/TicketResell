package eit.tub.ec.TicketResellBackend.Organizer;

import eit.tub.ec.TicketResellBackend.Utils.APIError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class OrganizerController {
    private OrganizerRepository organizerRepository;

    public OrganizerController(OrganizerRepository organizerRepository) {
        this.organizerRepository = organizerRepository;
    }

    @RequestMapping(value = {"/organizers"}, method = {RequestMethod.GET})
    public ResponseEntity<?> getOrganizers() {
        return ResponseEntity.status(HttpStatus.OK).body(organizerRepository.findAll());
    }

    @RequestMapping(value = {"/organizers/{id}"}, method = {RequestMethod.GET})
    public ResponseEntity<?> getOrganizerById(@PathVariable Long id) {
        Optional<Organizer> organizer = organizerRepository.findById(id);

        if (organizer.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(organizer.get());
        } else {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new APIError(HttpStatus.NOT_FOUND, "No organizer was found with the path ID provided."));
        }
    }
}


