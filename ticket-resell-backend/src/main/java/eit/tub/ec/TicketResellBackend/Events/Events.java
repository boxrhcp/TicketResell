package eit.tub.ec.TicketResellBackend.Events;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class Events {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private String name;
    private String localization;
    private Long ntickets;
    private Float amount;
    private Long organizerID;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public String getLocalization() { return localization; }

    public void setLocalization() { this.localization = localization;}

    public Long getnTickets() { return ntickets; }

    public void setNtickets() {this.ntickets = ntickets;}

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public Long getOrganizerID(){return organizerID;}

    public void setOrganizerID(){this.organizerID = organizerID;}
}
