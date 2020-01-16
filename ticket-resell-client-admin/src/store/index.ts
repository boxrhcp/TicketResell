import Vue from 'vue'
import Vuex from 'vuex'
import { Organizer } from '@/models/Organizer'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    loggedOrganizer : new Organizer(),
    isLoggedIn : false
  },
  mutations: {
    setLoggedOrganizerAction(state,  payload) {
      Organizer.RetrieveById(Number(payload)).then(o => state.loggedOrganizer = o);
      state.isLoggedIn = true;
    }
  },
  actions: {
  },
  modules: {
  }
})
