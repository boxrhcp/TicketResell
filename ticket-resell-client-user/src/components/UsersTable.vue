<template>
  <div class="users-table">
    <table class="table table-hover">
  <thead>
    <tr>
      <th scope="col">User ID</th>
      <th scope="col">Name</th>
      <th scope="col">Login</th>
    </tr>
  </thead>
  <tbody>
    <tr v-for="user in users" v-bind:key="user.id">
      <th scope="row">{{ user.id }}</th>
      <td>{{ user.name }}</td>
      <td><button type="button" v-bind:id="user.id" class="btn btn-success" @click="login($event)">Login</button></td>
    </tr>
  </tbody>
</table> 
  </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import { User } from '@/models/User';
import store from '../store';

@Component
export default class UsersTable extends Vue {
  private users: User[] = [];

  mounted() {
    User.Retrieve().then(u => {
        this.users = u;
    } );
  }

  private login(event: any) : void {
    let userId = event.target.id;
    store.commit("setLoggedUserAction", userId);
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.users-table {
  margin-top: 75px;
}
</style>
