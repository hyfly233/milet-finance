import {createRouter, createWebHashHistory} from "vue-router";

const routes = [
    {
        path: '/',
        name: 'Login',
        component: () => import('../views/Login.vue'),
    },

    {
        path: '/403',
        component: () => import('../views/403.vue'),
    },
    {
        path: '/404',
        component: () => import('../views/404.vue'),
    },
]


export const router = createRouter({
    history: createWebHashHistory(),
    routes: routes
})

// 路由拦截器
router.beforeEach((to, from, next) => {
    if (to.meta.requireAuth) { // 判断该路由是否需要登录权限
        if (sessionStorage.getItem("uid")) { // 通过vuex state获取当前的user是否存在
            next();
        } else {
            next({
                path: '/',
            })
        }
    } else {
        next();
    }
})

export default router
