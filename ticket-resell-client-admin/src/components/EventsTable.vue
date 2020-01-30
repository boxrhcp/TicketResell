<template>
  <div class="events-table">
    <table class="table table-hover">
      <thead>
        <tr>
          <th scope="col">Event ID</th>
          <th scope="col">Name</th>
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
          <td>{{ concert.venue }}</td>
          <td>{{ concert.maxTickets }}</td>
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
              <p>Venue : {{ selectedEvent.venue }}</p>
              <p>Organizer : {{ selectedEventOrganizer.name }}</p>
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
import { User } from '../models/User';

@Component
export default class EventTable extends Vue {
  private concerts: Concert[] = [];
  private selectedEvent: Concert = new Concert();
  private selectedEventOrganizer: User = new User();

  mounted() {
    Concert.Retrieve().then(e => (this.concerts = e));
  }

  private openEvent(event: any): void {
    let eventId = event.target.id;
    Concert.RetrieveById(eventId).then(c => {
        this.selectedEvent = c;
        User.RetrieveById(c.organizerId).then(o => {
            this.selectedEventOrganizer = o;
            $('#eventModal').modal();
        });        
    });    
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.events-table {
  margin-top: 75px;
}
</style>
