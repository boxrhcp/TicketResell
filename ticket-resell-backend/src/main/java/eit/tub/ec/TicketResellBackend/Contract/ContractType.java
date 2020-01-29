package eit.tub.ec.TicketResellBackend.Contract;

public enum ContractType {
    TICKET_LIBRARY(1L, "ticket_library"),
    TICKET_OWNER(2L, "ticket_owner");

    private Long id;
    private String type;

    ContractType(Long id, String type) {
        this.id = id;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public String getType() {
        return type;
    }
}