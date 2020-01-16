<template>
  <div class="organizers-table">
    <table class="table table-hover">
  <thead>
    <tr>
      <th scope="col">Organizer ID</th>
      <th scope="col">Name</th>
      <th scope="col">Login</th>
    </tr>
  </thead>
  <tbody>
    <tr v-for="organizer in organizers" v-bind:key="organizer.id">
      <th scope="row">{{ organizer.id }}</th>
      <td>{{ organizer.name }}</td>
      <td><button type="button" v-bind:id="organizer.id" class="btn btn-success" @click="login($event)">Login</button></td>
    </tr>
  </tbody>
</table> 
  </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import {Organizer} from '@/models/Organizer';
import store from '../store';

@Component
export default class OrganizersTable extends Vue {
  private organizers: Organizer[] = [];

  mounted() {
    Organizer.Retrieve().then(d => this.organizers = d );
  }

  private login(event: any) : void {
    let organizerId = event.target.id;
    store.commit("setLoggedOrganizerAction", organizerId);
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.organizers-table {
  margin-top: 75px;
}
</style>
