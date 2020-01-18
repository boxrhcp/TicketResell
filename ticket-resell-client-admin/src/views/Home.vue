<template>
  <div class="home">
    <div v-if="this.$store.state.isLoggedIn !== true" class="container">
      <OrganizersTable />
    </div>
    <div v-else class="container-fluid">
      <div class="row">
        <div class="col">
          <EventCreateForm />
        </div>
        <div class="col-8">
          <EventTable />
        </div>
      </div>
    </div>
  </div>
</template>

<script>
// @ is an alias to /src
import OrganizersTable from "@/components/OrganizersTable.vue";
import EventCreateForm from "@/components/EventCreateForm.vue";
import EventTable from "@/components/EventsTable.vue";

export default {
  name: "home",
  components: {
    OrganizersTable,
    EventCreateForm,
    EventTable
  }
};
</script>
