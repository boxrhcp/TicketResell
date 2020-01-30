import axios from "axios";

export class Ticket {
    id !: Number;
    eventId !: Number;
    price !: Number;
    onSale !: Boolean;
    ownerId !: Number;
}