import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    loggedUser : new Number(),
    isLoggedIn : false
  },
  mutations: {
    setLoggedUserAction(state, payload) {
      state.loggedUser = 1;
      state.isLoggedIn = true;
    }
  },
  actions: {
  },
  modules: {
  }
})
