<template>
  <header class="navbar-top py-3">
    <div class="container d-flex flex-wrap justify-content-between align-items-center">
      <!-- Logo -->
      <div class="d-flex align-items-center">
        <img src="vue-project\public\Img\logo.png" width="40" class="me-2" />
        <strong class="fs-5 text-white">NHÀ THUỐC TÂM AN</strong>
      </div>

      <!-- Tìm kiếm -->
      <div class="d-flex align-items-center flex-grow-1 mx-3" style="max-width: 500px">
        <div class="flex-grow-1">
          <TimKiem />
        </div>
      </div>

      <!-- Tài khoản & Giỏ hàng -->
      <div class="d-flex align-items-center gap-3">
        <router-link v-if="!userInfo" to="/dang-nhap" class="btn-login">
          <i class="bi bi-person"></i> Đăng nhập
        </router-link>

        <div class="dropdown" v-else>
          <button
            class="btn btn-account dropdown-toggle d-flex align-items-center gap-2"
            type="button"
            data-bs-toggle="dropdown"
          >
            <img
              :src="getFullImageUrl(userInfo?.hinhAnh)"
              width="36"
              height="36"
              class="rounded-circle"
            />
            <span class="fw-semibold d-none d-md-inline text-white">{{ userInfo.hoTen }}</span>
          </button>
          <ul class="dropdown-menu dropdown-menu-end mt-2">
            <li>
              <router-link class="dropdown-item" to="/tai-khoan/thong-tin-ca-nhan">
                <i class="bi bi-person me-2"></i> Thông tin cá nhân
              </router-link>
            </li>
            <li><hr class="dropdown-divider" /></li>
            <li>
              <a class="dropdown-item text-danger" href="#" @click.prevent="dangXuat">
                <i class="bi bi-box-arrow-right me-2"></i> Đăng xuất
              </a>
            </li>
          </ul>
        </div>

        <router-link to="/gio-hang" class="btn-cart">
          <i class="bi bi-cart3"></i> Giỏ hàng
          <span class="cart-badge">{{ CartStore.cartCount }}</span>
        </router-link>
      </div>
    </div>
  </header>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import TimKiem from './TimKiem.vue'
import CartStore from './CartStore.js'

const serverUrl = 'http://localhost:8080'
const defaultAvatar = 'https://cdn-icons-png.flaticon.com/512/149/149071.png'

const userInfo = ref(null)

onMounted(() => {
  const stored = localStorage.getItem('userInfo')
  if (stored) {
    userInfo.value = JSON.parse(stored)
  }
  CartStore.updateCount()
})

function getFullImageUrl(path) {
  return path ? `${serverUrl}${path}` : defaultAvatar
}

function dangXuat() {
  localStorage.removeItem('userInfo')
  location.reload()
}
</script>

<style scoped>
.navbar-top {
  background-color: #43a047;
  color: white;
}
.btn-login {
  font-size: 1.1rem;
  color: white;
  padding: 8px 16px;
  border-radius: 30px;
  background-color: transparent;
  display: flex;
  align-items: center;
  gap: 6px;
  text-decoration: none; /* ✅ Thêm dòng này */
}

.btn-cart {
  position: relative;
  background-color: rgba(0, 100, 0, 0.4);
  color: white;
  border-radius: 30px;
  padding: 8px 16px;
  display: flex;
  align-items: center;
  gap: 8px;
  text-decoration: none; /* ✅ Thêm dòng này */
}

.cart-badge {
  background-color: orange;
  color: white;
  font-size: 0.7rem;
  border-radius: 50%;
  padding: 2px 6px;
  position: absolute;
  top: -6px;
  right: -6px;
}
</style>
