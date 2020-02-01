<template>
  <div class="event-create-form">
    <form>
      <legend>Create a new Event</legend>
      <br />
      <fieldset>
        <div class="form-group">
          <label for="eventName">Event Name</label>
          <input
            type="text"
            class="form-control"
            id="eventName"
            v-model="newEvent.name"
            placeholder="Taylor Swift World Tour"
          />
        </div>
        <div class="form-group">
          <label for="eventYear">Date and Time</label>
          <div class="input-group">
            <input type="datetime-local" aria-label="Date" class="form-control" v-model="newEvent.date" />
          </div>
        </div>
        <div class="form-group">
          <label for="eventVenue">Venue</label>
          <input
            type="text"
            class="form-control"
            id="eventVenue"
            v-model="newEvent.place"
            placeholder="Berlin"
          />
        </div>
        <div class="form-group">
          <label for="eventTickets">Available Tickets</label>
          <input
            type="number"
            class="form-control"
            id="eventTickets"
            v-model="newEvent.ntickets"
            placeholder="1500"
          />
        </div>
        <div class="form-group">
          <label class="control-label">Ticket Prices</label>
          <div class="form-group">
            <div class="input-group mb-3">
              <div class="input-group-prepend">
                <span class="input-group-text">ETH</span>
              </div>
              <input
                type="number"
                class="form-control"
                v-model="newEvent.price"
                aria-label="Amount (to the nearest dollar)"
              />
            </div>
          </div>
        </div>
        <div class="form-group">
          <button type="button" class="btn btn-primary" @click="createEvent">Create</button>
        </div>
      </fieldset>
    </form>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import { Concert } from "@/models/Concert";
import store from "../store";
import moment from "moment";

@Component
export default class OrganizersTable extends Vue {
  private newEvent: Concert = new Concert();

  private createEvent(): void {
    this.newEvent.organizerId = store.state.loggedOrganizer.id;
    this.newEvent.price = Number(this.newEvent.price);
    this.newEvent.ntickets = Number(this.newEvent.price);
    Concert.Save(this.newEvent).then(result => {
      if (result.success) {
        alert("Saved the event : " + this.newEvent.name);
        this.resetForm();
      } else {
        alert("Could not save the event :( \n" + result.message);
      }
    }).catch(result => {
      alert("Could not save the event :( \n" + result.message);
    });
  }

  private resetForm(): void {
    this.newEvent.name = "";
    this.newEvent.place = "";
    this.newEvent.ntickets = 0;
    this.newEvent.price = 0;
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.event-create-form {
  margin-top: 75px;
}
</style>
