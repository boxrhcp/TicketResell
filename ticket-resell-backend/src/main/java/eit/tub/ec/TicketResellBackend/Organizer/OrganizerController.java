package eit.tub.ec.TicketResellBackend.Organizer;

import eit.tub.ec.TicketResellBackend.Events.EventsRepository;
import eit.tub.ec.TicketResellBackend.Organizer.Organizer;
import eit.tub.ec.TicketResellBackend.Organizer.OrganizerRepository;
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

        public OrganizerController(OrganizerRepository organizerRepository) { this.organizerRepository = organizerRepository; }

        @RequestMapping(
                value = {"/Organizer"},
                method = {RequestMethod.GET}
        )
        public ResponseEntity<?> getOrganizer() {
            return ResponseEntity.status(HttpStatus.OK).body(this.organizerRepository.findAll());
        }

        @RequestMapping(
                value = {"/Organizer/{id}"},
                method = {RequestMethod.GET}
        )
        public ResponseEntity<?> getOrganizerById(@PathVariable Long id) {
            Optional<Organizer> organizerOptional = this.organizerRepository.findById(id);
            Organizer organizer = organizerOptional.orElse(null);

            if (organizer == null) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(new APIError(HttpStatus.NOT_FOUND, "No event was found with the eventId provided."));
            }
            return ResponseEntity.status(HttpStatus.OK).body(organizer);
        }
    }


