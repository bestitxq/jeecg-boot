import Vue from 'vue'
import Vuex from 'vuex'

import getters from './ls-getters'
import user from './user/user'
Vue.use(Vuex)

export default new Vuex.Store({
  modules: {
    user
  },
  state: {
  },
  mutations: {
  },
  actions: {
  },
  getters
})
