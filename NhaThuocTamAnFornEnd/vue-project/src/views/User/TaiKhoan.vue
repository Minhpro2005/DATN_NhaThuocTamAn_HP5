<template>
  <div>
    <Header />
    <Navbar />

    <div class="container my-5">
      <div class="row">
        <!-- Sidebar -->
        <div class="col-md-3 mb-4 mb-md-0">
          <div class="sidebar-card shadow-sm p-3 rounded">
            <!-- Avatar + Info -->
            <div class="user-profile text-center mb-4">
              <img
                :src="getFullImageUrl(userInfo?.hinhAnh)"
                alt="Avatar"
                class="rounded-circle mb-2"
                width="80"
                height="80"
              />
              <h6 class="fw-bold mb-0">{{ userInfo?.hoTen || 'Khách hàng' }}</h6>
              <small class="text-muted">{{ userInfo?.email || '' }}</small>
            </div>

            <!-- Menu -->
            <nav class="nav flex-column">
              <router-link
                to="/tai-khoan/thong-tin-ca-nhan"
                class="nav-item-link"
                :class="{ active: isActive('/tai-khoan/thong-tin-ca-nhan') }"
              >
                <i class="bi bi-person-circle"></i>
                <span>Thông tin cá nhân</span>
              </router-link>

              <router-link
                to="/tai-khoan/don-hang-cua-ban"
                class="nav-item-link"
                :class="{ active: isActive('/tai-khoan/don-hang-cua-ban') }"
              >
                <i class="bi bi-box-seam"></i>
                <span>Đơn hàng của bạn</span>
              </router-link>

              <router-link
                to="/tai-khoan/doi-mat-khau"
                class="nav-item-link"
                :class="{ active: isActive('/tai-khoan/doi-mat-khau') }"
              >
                <i class="bi bi-key"></i>
                <span>Đổi mật khẩu</span>
              </router-link>

              <!-- Logout -->
              <button class="btn btn-outline-danger w-100 mt-3" @click="logout">
                <i class="bi bi-box-arrow-right me-2"></i> Đăng xuất
              </button>
            </nav>
          </div>
        </div>

        <!-- Nội dung tab -->
        <div class="col-md-9">
          <router-view />
        </div>
      </div>
    </div>

    <Footer />
  </div>
</template>

<script setup>
import Header from '../User/Header.vue'
import Navbar from '../User/Navbar.vue'
import Footer from './Footer.vue'
import { useRoute, useRouter } from 'vue-router'
import { UserStore } from './UserStore.js'

const route = useRoute()
const router = useRouter()

const serverUrl = 'http://localhost:8080'
const defaultAvatar = 'https://cdn-icons-png.flaticon.com/512/3135/3135715.png'

const userInfo = UserStore.userInfo

function isActive(path) {
  return route.path === path
}

function getFullImageUrl(path) {
  if (!path) return defaultAvatar
  return path.startsWith('http') ? path : `${serverUrl}${path}`
}

function logout() {
  UserStore.clearUser()
  router.push('/login')
}
</script>

<style scoped>
.sidebar-card {
  background: #fff;
  border-radius: 12px;
  border: 1px solid #eee;
}

.user-profile img {
  object-fit: cover;
  border: 2px solid #2e7d32;
}

.nav-item-link {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px 16px;
  margin-bottom: 8px;
  border-radius: 8px;
  color: #333;
  font-weight: 500;
  text-decoration: none;
  transition: all 0.2s ease-in-out;
}

.nav-item-link i {
  font-size: 1.4rem;
  color: #2e7d32;
}

.nav-item-link:hover {
  background: #f1f8f4;
  color: #2e7d32;
  transform: translateX(3px);
}

.nav-item-link.active {
  background-color: #2e7d32;
  color: #fff;
  box-shadow: 0 3px 6px rgba(0, 0, 0, 0.1);
}

.nav-item-link.active i {
  color: #fff;
}
</style>
