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
      <td>{{ concert.name }}</td>
      <td>{{ concert.venue }}</td>
      <td>{{ concert.maxTickets }}</td>
      <td>{{ concert.price }}</td>
    </tr>
  </tbody>
</table> 
  </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import store from '../store';
import { Concert } from '../models/Concert';

@Component
export default class OrganizersTable extends Vue {
  private concerts: Concert[] = [];

  mounted() {
    Concert.Retrieve().then(e => this.concerts = e );
  }

  private login(event: any) : void {
    let organizerId = event.target.id;
    store.commit("setLoggedOrganizerAction", organizerId);
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.events-table {
  margin-top: 75px;
}
</style>
