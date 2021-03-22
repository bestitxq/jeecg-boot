import Vue from 'vue'
import * as userApi from '@/api/userApi'

const user = {
  state: {
    // 用户登录后的返回基本信息
    userInfo: '',
    // 验证码
    captcha: '',
    // 用户权限信息
    userPermissionList: [],
  },
  mutations: {
    mUserInfo: (state, bool) => {
      state.userInfo = bool
    },
    mCaptcha: (state, bool) => {
      state.captcha = bool
    },
    mUserPreList: (state, bool) => {
      state.userPermissionList = bool
    }
  },
  actions: {
    //用户登录
    aUserInfo({ commit, state }, { bool}) {
      // 存储用户登录信息
      commit('mLogin', bool)
    },
    // 获取验证码
    async aLoginCaptcha ({ commit, state }, {paramets, resHandle, cbParamets={}}) {
      const result = await userApi.loginCaptcha(null,paramets)
      const imageData = result.result
      commit('mCaptcha', imageData)
      resHandle && resHandle(...cbParamets)
    },
    // 用户权限
    aUserPreList ({ commit, state }, {bool}) {
      commit('mUserPreList', bool)
    },
  }
}

export default user;