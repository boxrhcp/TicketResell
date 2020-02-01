<template>
  <div class="events-table">
    <table class="table table-hover">
      <thead>
        <tr>
          <th scope="col">Event ID</th>
          <th scope="col">Name</th>
          <th scope="col">Venue</th>
          <th scope="col">Date</th>
          <th scope="col">Resell</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="ticket in eventTickets" v-bind:key="ticket.ticketId">
          <th scope="row">{{ ticket.ticketId }}</th>
          <td>{{ ticket.concert.name }}</td>
          <td>{{ ticket.concert.place }}</td>
          <td>{{ ticket.concert.date | toLocaleDateString }} {{ ticket.concert.date | toLocaleTimeString }}</td>
          <td>
            <div class="form-group">
              <div class="input-group my-1">
                <div class="input-group-prepend">
                  <span class="input-group-text">ETH</span>
                </div>
                <input type="number" class="form-control" aria-label="Amount (to the nearest dollar)" v-model="ticket.price" />
                <div class="input-group-append">
                  <button
                    type="button"
                    class="btn btn-success"
                    v-bind:id="ticket.ticketId"
                    @click="resellTicket($event)"
                  >Resell</button>
                </div>
              </div>
            </div>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from "vue-property-decorator";
import store from "../store";
import moment from "moment";
import { Concert } from "../models/Concert";
import { Ticket } from "../models/Ticket";

@Component({
  name: "EventTable",
  filters: {
    toLocaleDateString(timeStamp: string) {
      return moment(timeStamp).format("MMM Do YYYY");
    },
    toLocaleTimeString(timeStamp: string) {
      return moment(timeStamp).format("LT");
    }
  }
})
export default class EventTable extends Vue {
  private eventTickets: EventTicket[] = [];
  @Prop() private userId!: Number; // = store.state.loggedUser.id;

  mounted() {
    this.updateTicketByOwners();
  }

  private updateTicketByOwners() {
    Ticket.RetrieveByOwner(this.userId).then(e => {
      this.eventTickets = [];
      e.forEach(t => {
        let eventTicket = new EventTicket();
        eventTicket.ticketId = t.id;
        Concert.RetrieveById(t.eventId).then(c => {
          eventTicket.concert = c;
          this.eventTickets.push(eventTicket);
        });
      });
    });
  }

  private resellTicket(event: any): void {
    const ticketId = event.target.id;
    const eventTicket = this.eventTickets.filter(
      e => (e.ticketId = ticketId)
    )[0];
    let confirmBuy = confirm(
      "Are you sure you want put the ticket to " +
        eventTicket.concert.name +
        " up for resell for the price of " + eventTicket.price + "ETH ? "
    );
    if (confirmBuy) {
      Ticket.Resell(ticketId, eventTicket.price).then(result => {
        if (result.success) {
          alert("Your ticket has been marked for resell.");
        } else {
          alert("Could not resell your ticket 😢\n" + result.message);
        }
      });
    } else {
      alert("Okay. Hope you have fun at the event! 🎉🎉");
    }
  }
}

class EventTicket {
  ticketId!: Number;
  price!: Number;
  concert!: Concert;
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
</style>
