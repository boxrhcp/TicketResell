import Vue from 'vue'
import Vuex from 'vuex'
import { User } from '@/models/User'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    loggedOrganizer : new User(),
    isLoggedIn : false
  },
  mutations: {
    setLoggedOrganizerAction(state,  payload) {
      User.RetrieveById(Number(payload)).then(o => state.loggedOrganizer = o);
      state.isLoggedIn = true;
    }
  },
  actions: {
  },
  modules: {
  }
})
