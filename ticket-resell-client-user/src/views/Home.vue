<template>
  <div class="home">
    <div v-if="this.$store.state.isLoggedIn !== true" class="container">
      <UsersTable />
    </div>
    <div v-else class="container-fluid">
      <div class="row">
        <div class="col">
          <EventTable />
        </div>
      </div>
    </div>
  </div>
</template>

<script>
// @ is an alias to /src
import UsersTable from '@/components/UsersTable.vue';
import EventTable from "@/components/EventsTable.vue";

export default {
  name: 'home',
  components: {
    UsersTable,
    EventTable
  }
}
</script>
