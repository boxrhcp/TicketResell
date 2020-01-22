package eit.tub.ec.TicketResellBackend.Organizer;

import eit.tub.ec.TicketResellBackend.Utils.APIError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @RequestMapping(value = "/organizers", method = RequestMethod.POST)
    public ResponseEntity<?> postOrganizer(@RequestBody Organizer organizer) {

        if (organizer.getName() == null) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new APIError(HttpStatus.BAD_REQUEST, " The field name can't be null"));
        }

        Organizer savedOrganizer = organizerRepository.save(organizer);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedOrganizer);
    }
}


