import Home from '@/views/User/Home.vue'
import ProductDetail from '@/views/User/ProductDetail.vue'
import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  { path: '/', redirect: '/home' },
  { path: '/home', name: 'home', component: Home },
  { path: '/product-detail/:id', name: 'product-detail', component: ProductDetail },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

export default router
