import Vue from 'vue'
import VueRouter from 'vue-router'
import store from "@/store";

Vue.use(VueRouter)

const routes = [

    {
        path: '/',
        name: 'Manage',
        component: () => import('../views/Manage.vue'),
        redirect: "/home",
        children: [
            {path: 'home', name: 'Home', component: () => import('../views/Home.vue')},
            {path: 'user', name: 'User', component: () => import('../views/User.vue')},
            {path: 'person', name: 'Person', component: () => import('../views/Person.vue')},
            {path: 'file', name: 'File', component: () => import('../views/File.vue')},
            {path: 'neo4j', name: 'Neo4j', component: () => import('../views/Neo4jGraph.vue')},
            {path: 'neo4jDetail', name: 'Neo4j Detail', component: () => import('../views/Neo4jDetail.vue')},
        ]
    },
    {
        path: '/login',
        name: 'Login',
        component: () => import( '../views/Login.vue')
    },
    {
        path: '/register',
        name: 'Register',
        component: () => import( '../views/Register.vue')
    },
    {
        path: '/about',
        name: 'About',
        // route level code-splitting
        // this generates a separate chunk (about.[hash].js) for this route
        // which is lazy-loaded when the route is visited.
        component: () => import(/* webpackChunkName: "about" */ '../views/AboutView.vue')
    }
]

const router = new VueRouter({
    mode: 'history',
    base: process.env.BASE_URL,
    routes
})
// 路由守卫
router.beforeEach((to, from, next) => {
    localStorage.setItem("currentPathName", to.name)  //设置当前的路由名称
    store.commit("setPath")  //触发store的数据更新
    next()  //放行路由
})

export default router
