<template>
  <div class="events-table">
    <table class="table table-hover">
      <thead>
        <tr>
          <th scope="col">Event ID</th>
          <th scope="col">Name</th>
          <th scope="col">Date</th>
          <th scope="col">Venue</th>
          <th scope="col">Maximum Tickets</th>
          <th scope="col">Ticket Price</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="concert in concerts" v-bind:key="concert.id">
          <th scope="row">{{ concert.id }}</th>
          <td>
            <a href="#" v-bind:id="concert.id" @click="openEvent($event)">{{ concert.name }}</a>
          </td>
          <td>{{ concert.date | toLocaleDateString }} {{ concert.date | toLocaleTimeString }}</td>
          <td>{{ concert.place }}</td>
          <td>{{ concert.ntickets }}</td>
          <td>{{ concert.price }}</td>
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
            <div v-if="soldTickets.length > 0 || unsoldTickets.length > 0" class="ticketowners-table">
              <table class="table table-hover">
                <thead>
                  <tr>
                    <th scope="col">ID</th>
                    <th scope="col">Name</th>
                    <th scope="col">Sold</th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-for="ticket in soldTickets" v-bind:key="ticket.id">
                    <th scope="row">{{ ticket.id }}</th>
                    <td>{{ getOwnerName(ticket.ownerId) }}</td>
                    <td>{{ !ticket.onSale }}</td>
                  </tr>
                  <tr v-for="ticket in unsoldTickets" v-bind:key="ticket.id">
                    <th scope="row">{{ ticket.id }}</th>
                    <td>{{ ticket.ownerId }}</td>                    
                    <td>{{ !ticket.onSale }}</td>
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
import { Concert } from "../models/Concert";
import { User } from "../models/User";
import { Ticket } from "@/models/Ticket";
import moment from "moment";

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
  private concerts: Concert[] = [];
  private selectedEvent: Concert = new Concert();
  private selectedEventOrganizer: User = new User();
  private soldTickets: Ticket[] = [];
  private unsoldTickets: Ticket[] = [];
  private ticketOwners: User[] = [];

  mounted() {
    Concert.Retrieve().then(e => {
      e.forEach(c => {
        if(c.id && c.date) {
          this.concerts.push(c);
        }
      });
    });
  }

  private openEvent(event: any): void {
    let eventId = event.target.id;
    Concert.RetrieveById(eventId).then(c => {
      this.selectedEvent = c;
      User.RetrieveById(c.organizerId).then(o => {
        this.selectedEventOrganizer = o;
        $("#eventModal").modal();
        Ticket.RetrieveSold(eventId)
          .then(t => {
            t.forEach(t => {
              User.RetrieveById(t.ownerId).then(o => this.ticketOwners.push(o));
            });
            this.soldTickets = t;
          })
          .catch(t => {
            alert("Could not fetch sold tickets :( \n" + t);
          });
        Ticket.RetrieveUnSold(eventId)
          .then(t => {
            t.forEach(t => {
              User.RetrieveById(t.ownerId).then(o => this.ticketOwners.push(o));
            });
            this.unsoldTickets = t;
          })
          .catch(t => {
            alert("Could not fetch unsold tickets :( \n" + t);
          });
      });
    });
  }

  private getOwnerName(ownerId: Number): String {
    let ownerName : String = '';
    ownerName = this.ticketOwners.filter(t => t.id == ownerId)[0].name;
    return ownerName;
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.events-table {
  margin-top: 75px;
}
</style>
