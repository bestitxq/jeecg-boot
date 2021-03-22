/** init domain config */
import Vue from 'vue'
// 基础配置配置
// import * as setting from './defaultSettings'
// 导入使用到的component包
// import * as use from './component_use'

//设置全局API_BASE_URL
Vue.prototype.API_BASE_URL = process.env.VUE_APP_API_BASE_URL
window._CONFIG = {};
window._CONFIG['baseURL'] = Vue.prototype.API_BASE_URL