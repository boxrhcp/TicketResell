<template>
  <div class="home">
    <div v-if="this.$store.state.isLoggedIn !== true" class="container">
      <UsersTable />
    </div>
    <div v-else class="container-fluid">
      <HelloWorld />
    </div>
  </div>
</template>

<script>
// @ is an alias to /src
import HelloWorld from '@/components/HelloWorld.vue';
import UsersTable from '@/components/UsersTable.vue';

export default {
  name: 'home',
  components: {
    HelloWorld,
    UsersTable
  }
}
</script>
