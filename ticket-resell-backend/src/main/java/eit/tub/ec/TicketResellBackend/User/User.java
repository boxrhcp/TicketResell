package eit.tub.ec.TicketResellBackend.User;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private String name;
    private Long eth_account;
    private boolean organizer = false;

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

    public Long getEth_account() {
        return eth_account;
    }

    public void setEth_account(Long eth_account) {
        this.eth_account = eth_account;
    }

    public boolean isOrganizer() {
        return organizer;
    }

    public void setOrganizer(boolean organizer) {
        this.organizer = organizer;
    }
}
