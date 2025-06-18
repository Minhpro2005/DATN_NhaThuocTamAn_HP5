<template>
  <div class="container-fluid">
    <!-- Overlay mobile -->
    <div v-if="isMobile && isSidebarOpen" class="overlay" @click="isSidebarOpen = false"></div>

    <div class="row">
      <!-- Sidebar -->
      <div
        class="admin-sidebar col-md-3 col-lg-2 p-0"
        :class="{ collapsed: isCollapsed, 'd-none': isMobile && !isSidebarOpen }"
      >
        <aside class="p-3 d-flex flex-column">
          <!-- Nút thu gọn -->
          <div class="text-end mb-3">
            <button class="btn btn-sm btn-light" @click="toggleSidebar">
              <i class="bi" :class="isCollapsed ? 'bi-list' : 'bi-x-lg'"></i>
            </button>
          </div>

          <!-- Logo -->
          <div class="text-center mb-4">
            <img :src="logoUrl" :style="{ width: isCollapsed ? '40px' : '80px' }" />
            <div v-if="!isCollapsed" class="mt-2 fw-bold" style="font-size: 14px">
              Nhà thuốc Tâm An
            </div>
          </div>

          <!-- Menu -->
          <ul class="nav flex-column gap-2 sidebar-menu">
            <li>
              <router-link to="/admin" class="nav-link" @click="autoCloseSidebar">
                <i class="bi bi-speedometer2 me-2"></i>
                <span v-if="!isCollapsed">Dashboard</span>
              </router-link>
            </li>

            <!-- Người dùng -->
            <li>
              <button
                class="nav-link d-flex justify-content-between"
                @click="toggleSubmenu('user')"
              >
                <span
                  ><i class="bi bi-people me-2"></i
                  ><span v-if="!isCollapsed">Người dùng</span></span
                >
                <i
                  class="bi"
                  :class="showSubmenu.user ? 'bi-chevron-down' : 'bi-chevron-right'"
                  v-if="!isCollapsed"
                ></i>
              </button>
              <transition name="fade-slide">
                <ul v-show="showSubmenu.user && !isCollapsed" class="nav flex-column submenu">
                  <li>
                    <router-link
                      to="/admin/nhan-vien"
                      class="nav-link small"
                      @click="autoCloseSidebar"
                    >
                      <i class="bi bi-circle-fill me-2"></i>Nhân viên
                    </router-link>
                  </li>
                  <li>
                    <router-link
                      to="/admin/khach-hang"
                      class="nav-link small"
                      @click="autoCloseSidebar"
                    >
                      <i class="bi bi-circle-fill me-2"></i>Khách hàng
                    </router-link>
                  </li>
                </ul>
              </transition>
            </li>

            <!-- Quản lý thuốc -->
            <li>
              <button
                class="nav-link d-flex justify-content-between"
                @click="toggleSubmenu('thuoc')"
              >
                <span
                  ><i class="bi bi-capsule me-2"></i
                  ><span v-if="!isCollapsed">Quản lý thuốc</span></span
                >
                <i
                  class="bi"
                  :class="showSubmenu.thuoc ? 'bi-chevron-down' : 'bi-chevron-right'"
                  v-if="!isCollapsed"
                ></i>
              </button>
              <transition name="fade-slide">
                <ul v-show="showSubmenu.thuoc && !isCollapsed" class="nav flex-column submenu">
                  <li>
                    <router-link to="/admin/san-pham" class="nav-link small"
                      ><i class="bi bi-circle-fill me-2"></i>Sản phẩm</router-link
                    >
                  </li>
                  <li>
                    <router-link to="/admin/bien-the" class="nav-link small"
                      ><i class="bi bi-circle-fill me-2"></i>Biến thể</router-link
                    >
                  </li>
                  <li>
                    <router-link to="/admin/anh-san-pham" class="nav-link small"
                      ><i class="bi bi-circle-fill me-2"></i>Ảnh</router-link
                    >
                  </li>
                  <li>
                    <router-link to="/admin/danh-muc" class="nav-link small"
                      ><i class="bi bi-circle-fill me-2"></i>Danh mục</router-link
                    >
                  </li>
                  <li>
                    <router-link to="/admin/nha-cung-cap" class="nav-link small"
                      ><i class="bi bi-circle-fill me-2"></i>Nhà cung cấp</router-link
                    >
                  </li>
                </ul>
              </transition>
            </li>

            <!-- Menu khác -->
            <li>
              <router-link to="/admin/nhap-hang" class="nav-link"
                ><i class="bi bi-truck me-2"></i
                ><span v-if="!isCollapsed">Nhập hàng</span></router-link
              >
            </li>
            <li>
              <router-link to="/admin/don-hang" class="nav-link"
                ><i class="bi bi-bag-check me-2"></i
                ><span v-if="!isCollapsed">Đơn hàng</span></router-link
              >
            </li>
            <li>
              <router-link to="/admin/don-thuoc" class="nav-link"
                ><i class="bi bi-file-earmark-medical me-2"></i
                ><span v-if="!isCollapsed">Đơn thuốc</span></router-link
              >
            </li>
            <!-- <li>
              <router-link to="/admin/hoa-don" class="nav-link"
                ><i class="bi bi-receipt-cutoff me-2"></i
                ><span v-if="!isCollapsed">Hóa đơn</span></router-link
              >
            </li> -->
            <li>
              <router-link to="/admin/kho-hang" class="nav-link"
                ><i class="bi bi-box me-2"></i
                ><span v-if="!isCollapsed">Kho hàng</span></router-link
              >
            </li>
            <li>
              <router-link to="/admin/khuyen-mai" class="nav-link"
                ><i class="bi bi-tags me-2"></i
                ><span v-if="!isCollapsed">Khuyến mãi</span></router-link
              >
            </li>
            <li>
              <router-link to="/admin/dang-bai" class="nav-link"
                ><i class="bi bi-file-earmark-text me-2"></i
                ><span v-if="!isCollapsed">Tin tức</span></router-link
              >
            </li>
            <li>
              <router-link to="/admin/danh-gia" class="nav-link"
                ><i class="bi bi-star me-2"></i
                ><span v-if="!isCollapsed">Đánh giá</span></router-link
              >
            </li>
          </ul>
        </aside>
      </div>

      <!-- Main content -->
      <div class="admin-main col-md-9 col-lg-10 p-0">
        <!-- Header -->
        <header
          class="admin-header d-flex justify-content-between align-items-center p-3 border-bottom bg-white shadow-sm"
        >
          <div class="d-flex align-items-center gap-3">
            <button class="btn btn-outline-secondary d-md-none" @click="toggleMobileSidebar">
              <i class="bi bi-list"></i>
            </button>
            <h5 class="mb-0">Admin Panel</h5>
          </div>
          <div class="d-flex align-items-center gap-3">
            <img
              :src="user?.avatar || defaultAvatar"
              class="rounded-circle"
              style="width: 38px; height: 38px; object-fit: cover"
            />
            <div class="text-end">
              <div class="fw-semibold text-dark">{{ user?.hoTen || 'Admin' }}</div>
            </div>
            <button class="btn btn-sm btn-outline-danger ms-2" @click="logout">
              <i class="bi bi-box-arrow-right me-1"></i> Đăng xuất
            </button>
          </div>
        </header>

        <!-- Nội dung -->
        <main class="p-4 bg-light" style="min-height: 80vh">
          <div class="content-wrapper mx-auto px-3 px-md-4">
            <router-view />
          </div>
        </main>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const isCollapsed = ref(false)
const isMobile = ref(false)
const isSidebarOpen = ref(true)

const logoUrl =
  'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRiqUlj1LB8gUERYiFqhQZfT6YFZa2VRvqfwA&s'
const defaultAvatar = 'https://cdn-icons-png.flaticon.com/512/149/149071.png'
const user = JSON.parse(localStorage.getItem('user'))

const showSubmenu = ref({ user: false, thuoc: false })

const toggleSubmenu = (key) => {
  showSubmenu.value[key] = !showSubmenu.value[key]
}
const toggleSidebar = () => {
  isCollapsed.value = !isCollapsed.value
}
const toggleMobileSidebar = () => {
  isSidebarOpen.value = !isSidebarOpen.value
}
const autoCloseSidebar = () => {
  if (isMobile.value) isSidebarOpen.value = false
}
const checkMobile = () => {
  isMobile.value = window.innerWidth < 768
  isSidebarOpen.value = !isMobile.value
  isCollapsed.value = false
}
onMounted(() => {
  checkMobile()
  window.addEventListener('resize', checkMobile)
})
const logout = () => {
  localStorage.removeItem('user')
  router.push('/dang-nhap')
}
</script>

<style scoped>
.admin-sidebar {
  background-color: #1b5e20;
  color: white;
  min-height: 100vh;
  transition: all 0.3s;
}
.admin-sidebar.collapsed {
  width: 80px !important;
  flex: none;
  overflow: hidden;
}
.nav-link {
  color: white;
  padding: 10px 14px;
  border-radius: 6px;
  display: flex;
  align-items: center;
  transition: 0.2s;
}
.nav-link.router-link-exact-active,
.nav-link:hover {
  background-color: #388e3c;
  font-weight: bold;
}
.submenu {
  padding-left: 20px;
}
.submenu .nav-link {
  font-size: 14px;
  padding-left: 30px;
}
.fade-slide-enter-active,
.fade-slide-leave-active {
  transition: all 0.3s ease;
}
.fade-slide-enter-from,
.fade-slide-leave-to {
  max-height: 0;
  opacity: 0;
}
.fade-slide-enter-to,
.fade-slide-leave-from {
  max-height: 300px;
  opacity: 1;
}
.content-wrapper {
  max-width: 1200px;
  width: 100%;
}
.overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  z-index: 1050;
}
</style>
