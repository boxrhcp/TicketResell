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
            <button
              type="button"
              class="btn btn-success"
              v-bind:id="ticket.ticketId"
              @click="resellTicket($event)"
            >Resell</button>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
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

  mounted() {
    let userId = store.state.loggedUser.id;
    Ticket.RetrieveByOwner(userId).then(e => {
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
        " up for resell ? "
    );
    if (confirmBuy) {
      Ticket.Resell(ticketId).then(result => {
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
  concert!: Concert;
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
</style>
