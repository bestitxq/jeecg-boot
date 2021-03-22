import Vue from 'vue'
import { USER_INFO } from "@/store/mutation-types"

const getters = {
    userInfo: state => {state.userInfo = Vue.ls.get(USER_INFO); return state.userInfo},

}

export default getters
