<template>
  <div class="events-table">
    <table class="table table-hover">
      <thead>
        <tr>
          <th scope="col">Event ID</th>
          <th scope="col">Name</th>
          <th scope="col">Venue</th>
          <th scope="col">Date</th>
          <th scope="col">Available Tickets</th>
          <th scope="col">Ticket Price</th>
          <th scope="col">Buy</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="concert in concerts" v-bind:key="concert.id">
          <th scope="row">{{ concert.id }}</th>
          <td>{{ concert.name }}</td>
          <td>{{ concert.place }}</td>
          <td>{{ concert.date | toLocaleDateString }} {{ concert.date | toLocaleTimeString }}</td>
          <td>{{ concert.availableTickets | showAvailableTickets }}</td>
          <td>{{ concert.price }} ETH</td>
          <td>
            <button
              type="button"
              :disabled="concert.availableTickets === 0"
              class="btn btn-success"
              v-bind:id="concert.id"
              @click="openEvent($event)"
            >Buy</button>
          </td>
        </tr>
      </tbody>
    </table>
    <div
      class="modal fade"
      id="eventModal"
      tabindex="-1"
      role="dialog"
      aria-labelledby="eventModalLabel"
      aria-hidden="true"
    >
      <div class="modal-dialog" role="document">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title" id="eventModalLabel">{{ selectedEvent.name }}</h5>
            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
              <span aria-hidden="true">&times;</span>
            </button>
          </div>
          <div class="modal-body">
            <p>Date : {{ selectedEvent.date | toLocaleDateString }} {{ selectedEvent.date | toLocaleTimeString }}</p>
            <p>Venue : {{ selectedEvent.place }}</p>
            <p>Organizer : {{ selectedEventOrganizer.name }}</p>
            <div v-if="tickets.length > 0" class="ticketowners-table">
              <table class="table table-hover">
                <thead>
                  <tr>
                    <th scope="col">ID</th>
                    <th scope="col">Owner Name</th>
                    <th scope="col">Price</th>
                    <th scope="col">Buy</th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-for="ticket in tickets" v-bind:key="ticket.id">
                    <th scope="row" v-if="ticket.onSale === true">{{ ticket.id }}</th>
                    <td v-if="ticket.onSale === true">{{ getOwnerName(ticket.ownerId) }}</td>
                    <td v-if="ticket.onSale === true">{{ ticket.price }}</td>
                    <td v-if="ticket.onSale === true">
                      <button
                        type="button"
                        class="btn btn-success"
                        v-bind:id="ticket.id"
                        @click="buyTicket($event)"
                      >Buy</button>
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-primary" data-dismiss="modal">Close</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import store from "../store";
import moment from "moment";
import { Concert } from "../models/Concert";
import { Ticket } from "../models/Ticket";
import { User } from "../models/User";

@Component({
  name: "EventTable",
  filters: {
    toLocaleDateString(timeStamp: string) {
      return moment(timeStamp).format("MMM Do YYYY");
    },
    toLocaleTimeString(timeStamp: string) {
      return moment(timeStamp).format("LT");
    },
    showAvailableTickets(availableTickets: number) {
      return availableTickets > 0 ? availableTickets : "Sold out";
    }
  }
})
export default class EventTable extends Vue {
  private concerts: Concert[] = [];
  private ticketOwners: User[] = [];
  private tickets: Ticket[] = [];
  private selectedEvent: Concert = new Concert();
  private selectedEventOrganizer: User = new User();

  mounted() {
    Concert.Retrieve().then(e => {
      this.concerts = e;
    });
  }

  private buyTicket(event: any): void {
    let ticket = new Ticket();
    ticket.id = event.target.id;
    ticket.ownerId = store.state.loggedUser.id;

    let confirmBuy = confirm(
      "Are you sure you want to buy " + this.selectedEvent.name + "?"
    );
    if (confirmBuy) {
      Ticket.Buy(ticket.ownerId, ticket.id)
        .then(r => {
          if (r.success === true) {
            alert(
              "Successfully bought the ticket to " + this.selectedEvent.name
            );
            $("#eventModal").modal('hide');
          }
        })
        .catch(r => {
          alert(
            "bad stuff happned when trying to buy the ticket\n" + r.message
          );
        });
    }
  }

  private openEvent(event: any): void {
    let eventId = event.target.id;
    Concert.RetrieveById(eventId).then(c => {
      this.selectedEvent = c;
      User.RetrieveById(c.organizerId).then(o => {
        this.selectedEventOrganizer = o;
        $("#eventModal").modal();
        Ticket.RetrieveByEvent(eventId)
          .then(t => {
            t.forEach(t => {
              User.RetrieveById(t.ownerId).then(o => this.ticketOwners.push(o));
            });
            this.tickets = t;
          })
          .catch(t => {
            alert("Could not fetch sold tickets :( \n" + t);
          });
      });
    });
  }

  private getOwnerName(ownerId: Number): String {
    let ownerName: String = "";
    ownerName = this.ticketOwners.filter(t => t.id == ownerId)[0]!.name;
    return ownerName;
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
</style>
