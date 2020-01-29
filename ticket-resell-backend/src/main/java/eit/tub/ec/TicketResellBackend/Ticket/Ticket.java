package eit.tub.ec.TicketResellBackend.Ticket;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Ticket {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private Long eventId;
    private Long ownerId;
    private Float price;
    private Boolean onSale = true;
    private String ethUri;
    private Long ethId;
    private Long sellContractId;

    public Ticket() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Boolean isOnSale() {
        return onSale;
    }

    public void setOnSale(Boolean onSale, Float price) {
        this.onSale = onSale;
    }

    public String getEthUri() {
        return ethUri;
    }

    public void setEthUri(String ethUri) {
        this.ethUri = ethUri;
    }

    public Long getEthId() {
        return ethId;
    }

    public void setEthId(Long ethId) {
        this.ethId = ethId;
    }

    public Long getSellContractId() {
        return sellContractId;
    }

    public void setSellContractId(Long sellContractId) {
        this.sellContractId = sellContractId;
    }
}
