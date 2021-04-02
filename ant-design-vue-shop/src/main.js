// 基本配置 config/index.js
import './config'

/** init domain config */
import Vue from 'vue'
// 初始化页面
import App from './App.vue'
// 路由配置
import router from './router'
// vuex
import store from './store'
// localstore工具
import Storage from 'vue-ls'
// 基础配置配置
import config from './config/defaultSettings'
import './config/component_use'
// 路由拦截验证
import './permission'

Vue.use(router)
Vue.use(Storage, config.storageOptions )

new Vue({
  router,
  store,
  mounted () {
  },
  render: h => h(App)
}).$mount('#app')