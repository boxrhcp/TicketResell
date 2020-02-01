<template>
  <div class="home">
    <div v-if="this.$store.state.isLoggedIn !== true" class="container">
      <UsersTable />
    </div>
    <div v-else class="container-fluid">
      <ul class="nav nav-tabs" style="margin-top: 75px;">
        <li class="nav-item">
          <a class="nav-link active" data-toggle="tab" href="#home">Buy</a>
        </li>
        <li class="nav-item">
          <a class="nav-link" data-toggle="tab" href="#profile">Owned</a>
        </li>
      </ul>
      <div id="myTabContent" class="tab-content">
        <div class="tab-pane fade active show" id="home">
          <EventTable />
        </div>
        <div class="tab-pane fade" id="profile">
          <div v-if="this.$store.state.loggedUser.id !== undefined">
            <OwnedEventsTable :userId="this.$store.state.loggedUser.id" />
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
// @ is an alias to /src
import UsersTable from "@/components/UsersTable.vue";
import EventTable from "@/components/EventsTable.vue";
import OwnedEventsTable from "@/components/OwnedEventsTable.vue";

export default {
  name: "home",
  components: {
    UsersTable,
    EventTable,
    OwnedEventsTable
  }
};
</script>
