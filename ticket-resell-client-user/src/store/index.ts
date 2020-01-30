import Vue from 'vue'
import Vuex from 'vuex'
import { User } from '@/models/User';

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    loggedUser : new User(),
    isLoggedIn : false
  },
  mutations: {
    setLoggedUserAction(state, payload) {
      User.RetrieveById(Number(payload)).then(u => state.loggedUser = u);
      state.isLoggedIn = true;
    }
  },
  actions: {
  },
  modules: {
  }
})
