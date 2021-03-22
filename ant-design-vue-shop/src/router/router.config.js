/**
 * 走菜单，走权限控制
 */
export const asyncRouterMap = [
]

/**
 * 基础路由
 */
export const constantRouterMap = [

]

/**
 * 基础路由
 */
 export const userRouterMap = [
    {
        path: '/login',
        name: 'login',
        component: () => import('@/views/user/Login')
    },
    {
        path: '/',
        name: 'index',
        component: ()=> import('@/views/Home')
    },
    {
        path: '/home',
        name: 'home',
        component: ()=> import('@/views/Home')
    },
    {
        path: '/register',
        name: 'register',
        component: ()=> import('@/views/user/register/Register')
    },
    {
        path: '/registerResult',
        name: 'registerResult',
        component: ()=> import('@/views/user/register/RegisterResult')
    },
]
