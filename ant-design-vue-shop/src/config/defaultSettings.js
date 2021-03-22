/**
 * 项目默认配置项
 * 
 * storageOptions: {} - Vue-ls 插件配置项 (localStorage/sessionStorage)
 *
 */

export default {
  // vue-ls options 基础配置
  storageOptions: {
    namespace: 'shop_pro_', // key键前缀
    name: 'ls', // 命名Vue变量.[ls]或this.[$ls],
    storage: 'local', // 存储名称: session, local, memory
  }
}