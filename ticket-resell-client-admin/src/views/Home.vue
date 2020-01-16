<template>
  <div class="container home">
    <div v-if="this.$store.state.isLoggedIn !== true">
      <OrganizersTable />
    </div>
    <div v-else>
    </div>
  </div>
</template>

<script>
// @ is an alias to /src
import OrganizersTable from "@/components/OrganizersTable.vue";

export default {
  name: "home",
  components: {
    OrganizersTable
  }
};
</script>
