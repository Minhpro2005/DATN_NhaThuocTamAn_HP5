<template>
  <div>
    <Header />
    <Navbar />
  </div>

  <div class="container my-5">
    <div class="row">
      <!-- Sidebar -->
      <div class="col-md-3 mb-4 mb-md-0">
        <div class="list-group shadow-sm">
          <router-link
            to="/tai-khoan/thong-tin-ca-nhan"
            class="list-group-item list-group-item-action"
            :class="{ active: isActive('/tai-khoan/thong-tin-ca-nhan') }"
          >
            <i class="bi bi-person-circle me-2"></i>Thông tin cá nhân
          </router-link>
          <router-link
            to="/tai-khoan/don-hang-cua-ban"
            class="list-group-item list-group-item-action"
            :class="{ active: isActive('/tai-khoan/don-hang-cua-ban') }"
          >
            <i class="bi bi-box-seam me-2"></i>Đơn hàng của bạn
          </router-link>
          <router-link
            to="/tai-khoan/lich-su-mua-hang"
            class="list-group-item list-group-item-action"
            :class="{ active: isActive('/tai-khoan/lich-su-mua-hang') }"
          >
            <i class="bi bi-clock-history me-2"></i>Lịch sử mua hàng
          </router-link>
          <router-link
            to="/tai-khoan/doi-mat-khau"
            class="list-group-item list-group-item-action"
            :class="{ active: isActive('/tai-khoan/doi-mat-khau') }"
          >
            <i class="bi bi-key me-2"></i>Đổi mật khẩu
          </router-link>
        </div>
      </div>

      <!-- Nội dung tab -->
      <div class="col-md-9">
        <router-view />
      </div>
    </div>
  </div>
</template>

<script setup>
import Header from '../User/Header.vue'
import Navbar from '../User/Navbar.vue'

import { useRoute } from 'vue-router'
const route = useRoute()

function isActive(path) {
  return route.path === path
}
</script>

<style scoped>
.list-group-item.active {
  background-color: #2e7d32;
  border-color: #2e7d32;
  color: white;
  font-weight: 500;
}
</style>
