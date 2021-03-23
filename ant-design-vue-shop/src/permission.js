import Vue from 'vue'
import router from './router'
import { ACCESS_TOKEN } from '@/store/mutation-types'

const whiteList = ['/login', '/register', '/registerResult'] // no redirect whitelist
const perPathList = ['/home'] // no redirect whitelist

router.beforeEach((to, from, next) => {

  if (Vue.ls.get(ACCESS_TOKEN)) {
    /* has token */
    if (to.path === whiteList[0]) {
      // 有token的情况访问登录页直接跳转首页即可
      next({ path: perPathList[0] })
      console.log(1);
    } else {
      next()
      console.log(2);
    }
  } else {
    if (whiteList.indexOf(to.path) !== -1) {
      console.log(3);
      // 在免登录白名单，直接进入
      next()
    } else {
      console.log(4);
      next({ path: whiteList[0], query: { redirect: to.fullPath } })
    }
  }
})
