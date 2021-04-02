import { axios } from '@/api/config/request'

//1、获取登录验证码
export function loginCaptcha(parameter,key) {
    return axios({
        url: '/sys/randomImage/'+key,
        method: 'get',
        params: parameter
    })
}
//2、用户登录
export function userLogin(parameter) {
    return axios({
        url: '/sys/receptionLogin',
        method: 'post',
        data: parameter
    })
}
//3、用户登出
export function userLogout(parameter) {
    return axios({
        url: '/sys/logout',
        method: 'post',
        data: parameter
    })
}
//3、校验用户是否已经存在
export function checkOnlyUser(parameter) {
    return axios({
        url: '/sys/user/checkOnlyUser',
        method: 'get',
        params: parameter
    })
}
//4、用户邀请码url生成
export function generateAdUrl(parameter) {
    return axios({
        url: '/sys/user/generateAdUrl',
        method: 'get',
        params: parameter
    })
}
//5、用户会员剩余天数
export function memberTime(parameter) {
    return axios({
        url: '/sys/userMember/memberTime',
        method: 'get',
        params: parameter
    })
}
//6、累计vip奖励展示
export function showVipRule(parameter) {
    return axios({
        url: '/sys/userRegisterRule/showVipRule',
        method: 'get',
        params: parameter
    })
}
//7、累计用户邀请注册人数
export function getInviteUserNum(parameter) {
    return axios({
        url: '/sys/userRegisterRecord/getInviteUserNum',
        method: 'get',
        params: parameter
    })
}
// 用户权限查询
export function queryPermissionsByUser(parameter) {
    return axios({
        url: '/sys/permission/getUserPermissionByToken',
        method: 'get',
        params: parameter
    })
}


