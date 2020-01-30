<template>
  <div id="app">
    <nav class="navbar navbar-expand-lg navbar-dark bg-primary">
      <a class="navbar-brand" href="#">Ticket Resell</a>
      <div class="collapse navbar-collapse" id="navbarColor01">
        <ul class="navbar-nav mr-auto">
          <li class="nav-item">
            <router-link class="nav-link" to="/">Home</router-link>
          </li>
          <li class="nav-item">
            <router-link class="nav-link" to="/about">About</router-link>
          </li>          
        </ul>
        <div class="my-2 my-lg-0">
            <router-link class="nav-link" to="/">{{ this.$store.state.loggedUser.name }}</router-link>
          </div>
      </div>
    </nav>
    <router-view />
  </div>
</template>
