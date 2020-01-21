<template>
  <div class="home">
    <div v-if="this.$store.state.isLoggedIn !== true" class="container">
      <HelloWorld />
    </div>
    <div v-else class="container-fluid">
      <HelloWorld />
    </div>
  </div>
</template>

<script>
// @ is an alias to /src
import HelloWorld from '@/components/HelloWorld.vue'

export default {
  name: 'home',
  components: {
    HelloWorld
  }
}
</script>
