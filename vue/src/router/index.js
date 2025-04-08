import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    { path: '/', redirect: '/manager/home' },
    { path: "/manager", component: () => import("../views/Manager.vue"),
      children: [
        {path: "home", meta:{ name: '主页' }, component: () => import("../views/Home.vue")},
        { path: 'admin', meta: { name: '员工信息' }, component: () => import('../views/Admin.vue'),},
        {path: "user", meta:{ name: '会员信息' }, component: () => import("../views/User.vue")},
        {path: "person", meta:{ name: '个人中心' }, component: () => import("../views/Person.vue")},
        {path: "updatePassword", meta:{ name: '修改密码' }, component: () => import("../views/UpdatePassword.vue")},
        {path: "notice", meta:{ name: '系统公告' }, component: () => import("../views/Notice.vue")},
        {path: "message", meta:{ name: '留言管理' }, component: () => import("../views/Message.vue")},
        {path: "category", meta:{ name: '分类管理' }, component: () => import("../views/Category.vue")},
        {path: "goods", meta:{ name: '商品管理' }, component: () => import("../views/Goods.vue")},
        {path: "supplier", meta:{ name: '供货商管理' }, component: () => import("../views/Supplier.vue")},
        {path: "orders", meta:{ name: '订单管理' }, component: () => import("../views/Orders.vue")},
      ]
    },
    { path: '/login', component: import('@/views/Login.vue') },
    { path: '/register', component: import('@/views/Register.vue') },
    { path: '/notFound', component: import('@/views/404.vue') },
    { path: '/:pathMatch(.*)', redirect: '/notFound' }
  ]
})

export default router