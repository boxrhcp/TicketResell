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
          <td>{{ concert.datetime | toLocaleDateString }} {{ concert.datetime | toLocaleTimeString }}</td>
          <td>{{ concert.availableTickets | showAvailableTickets }}</td>
          <td>{{ concert.price }}</td>
          <td><button type="button" :disabled="concert.availableTickets === 0" class="btn btn-success" v-bind:id="concert.id" @click="buyTicket($event)">Buy</button></td>
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
    },
    showAvailableTickets(availableTickets: number) {
      return availableTickets > 0 ? availableTickets : 'Sold out';
    }
  }
})
export default class EventTable extends Vue {
  private concerts: Concert[] = [];

  mounted() {
    Concert.Retrieve().then(e => {
        this.concerts = e;
    });
  }

  private buyTicket(event : any) : void {
    let ticket = new Ticket();
    ticket.eventId = event.target.id;
    ticket.ownerId = store.state.loggedUser.id;

    const selectedEvent = this.concerts.filter(e => e.id == event.target.id)[0];

    let confirmBuy = confirm("Are you sure you want to buy " + selectedEvent.name + "?");
    if(confirmBuy) {
      alert("Bought");
    }
    else {
      alert("K didnt buy");
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.events-table {
  margin-top: 75px;
}
</style>
