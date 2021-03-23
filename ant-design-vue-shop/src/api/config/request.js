import Vue from 'vue'
import axios from 'axios'
import store from '@/store'
import router from '../../router'
import { VueAxios } from './axios'
import {Modal, notification} from 'ant-design-vue'
import { ACCESS_TOKEN, USER_INFO, TENANT_ID } from "@/store/mutation-types"
import { userLogin, userLogout } from '@/api/userApi'

/**
 * 【指定 axios的 baseURL】
 * 如果手工指定 baseURL: '/jeecg-boot'
 * 则映射后端域名，通过 vue.config.js
 * @type {*|string}
 */
let baseUrl = window._CONFIG['baseURL'] || "/jeecg-boot";
// 创建 axios 实例
const axiosx = axios.create({
  baseURL: baseUrl, // api base_url
  timeout: 9000 // 请求超时时间
})
// 跨域携带cookie
axiosx.defaults.withCredentials = true
// 相应异常处理
const err = (error) => {
  if (error.response) {
    let data = error.response.data
    const token = Vue.ls.get(ACCESS_TOKEN)
    console.log("------异常响应------token:",token)
    console.log("------异常响应------状态:",error.response.status)
    switch (error.response.status) {
      case 403:
        notification.error({ message: '系统提示', description: '拒绝访问',duration: 4})
        break
      case 500:
        console.log("------error.response------",error.response)
        // update-begin- --- author:liusq ------ date:20200910 ---- for:处理Blob情况----
        let type=error.response.request.responseType;
        if(type === 'blob'){
          blobToJson(data);
          break;
        }
        // update-end- --- author:liusq ------ date:20200910 ---- for:处理Blob情况----
        //notification.error({ message: '系统提示', description:'Token失效，请重新登录!',duration: 4})
        if(token && data.message.includes("Token失效")){
          logoutTs('登录已过期', '很抱歉，登录已过期，请重新登录', '重新登录')
        }
        break
      case 404:
          notification.error({ message: '系统提示', description:'很抱歉，资源未找到!',duration: 4})
        break
      case 504:
        notification.error({ message: '系统提示', description: '网络超时'})
        break
      case 401:
        notification.error({ message: '系统提示', description:'未授权，请重新登录',duration: 4})
        if (token) {
          store.dispatch('Logout').then(() => {
            setTimeout(() => {
              window.location.reload()
            }, 1500)
          })
        }
        break
      default:
        notification.error({
          message: '系统提示',
          description: data.message,
          duration: 4
        })
        break
    }
  }
  return Promise.reject(error)
};

// request interceptor
axiosx.interceptors.request.use(config => {
  const token = Vue.ls.get(ACCESS_TOKEN)
  console.log(ACCESS_TOKEN, token);

  if (token) {
    config.headers[ 'X-Access-Token' ] = token // 让每个请求携带自定义 token 请根据实际情况自行修改
  }
  //update-begin-author:taoyan date:2020707 for:多租户
  let tenantid = Vue.ls.get(TENANT_ID)
  if (!tenantid) {
    tenantid = 0;
  }
  config.headers[ 'tenant_id' ] = tenantid
  //update-end-author:taoyan date:2020707 for:多租户
  if(config.method=='get'){
    if(config.url.indexOf("sys/dict/getDictItems")<0){
      config.params = {
        _t: Date.parse(new Date())/1000,
        ...config.params
      }
    }
  }
  return config
},(error) => {
  notification.error({
    message: '系统提示',
    description: '网络不给力,请稍后再试',
    duration: 4
  })
  return Promise.reject(error)
})

// response interceptor
axiosx.interceptors.response.use((response) => {
  if (response.data.code === 500) {
    notification.error({ message: '系统提示', description:response.data.message,duration: 4})
  }
  return response.data
}, err)

const installer = {
  vm: {},
  install (Vue, router = {}) {
    Vue.use(VueAxios, router, axiosx)
  }
}
/**
 * Blob解析
 * @param data
 */
function blobToJson(data) {
  let fileReader = new FileReader();
  let token = Vue.ls.get(ACCESS_TOKEN);
  fileReader.onload = function() {
    try {
      let jsonData = JSON.parse(this.result);  // 说明是普通对象数据，后台转换失败
      console.log("jsonData",jsonData)
      if (jsonData.status === 500) {
        console.log("token----------》",token)
        if(token && jsonData.message.includes("Token失效")){
          logoutTs('登录已过期', '很抱歉，登录已过期，请重新登录', '重新登录')
        }
      }
    } catch (err) {
      // 解析成对象失败，说明是正常的文件流
      console.log("blob解析fileReader返回err",err)
    }
  };
  fileReader.readAsText(data)
}

/**
 * 登录过期错误提示及退出登录接口调用
 */
function logoutTs(title, content, okText){
  Modal.error({
    title: title,
    content: content,
    okText: okText,
    mask: false,
    onOk: () => {
      console.log(5);
      logout()
    }
  })
}
export function logout(){
  userLogout().then((res)=>{
    console.log(6);
    Vue.ls.remove(ACCESS_TOKEN)
    Vue.ls.remove(USER_INFO)
    router.push({name: 'login'})
  }).catch((res) =>{
    console.log(7);
    console.log("重新登录错误",res);
    window.location.reload()
  })
}

export function login(paramets, redirectUrl){
  userLogin(paramets).then((res)=>{
    if(res.success){
      // 无需进行失败判断，在request.js中已经对返回数据进行处理了
      const token = res.result.token
      // 保持登录时间期限 一周7天
      const loginTime = 7 * 24 * 60 * 60 * 1000
      Vue.ls.set(ACCESS_TOKEN, token, loginTime)
      Vue.ls.set(USER_INFO, res.result.userInfo, loginTime)

      // 登录成功进行跳转
      if (redirectUrl) {
        //跳转到原页面
        router.push({ path: decodeURIComponent(redirectUrl) });
      } else {
        router.push({ name: "home" }); //正常登录流程进入的页面
      }
    }
})
}

export {
  installer as VueAxios,
  axiosx as axios
}