import Vue from 'vue'
import Router from 'vue-router'
import { userRouterMap } from '@/router/router.config'
Vue.use(Router)

export default new Router({
  mode: 'history',
  routes: userRouterMap
})