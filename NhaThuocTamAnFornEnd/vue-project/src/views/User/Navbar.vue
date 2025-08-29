<template>
  <!-- ✅ MENU NAV -->
  <nav class="menu-nav">
    <div class="container d-flex justify-content-center gap-3 flex-wrap">
      <!-- Render danh mục cha -->
      <div v-for="dm in danhMucList" :key="dm.maDM" class="dropdown">
        <!-- Link cha -->
        <router-link
          class="dropdown-toggle"
          :to="{ name: 'product-list-by-category', params: { maDM: dm.maDM } }"
        >
          {{ dm.tenDanhMuc }}
        </router-link>

        <!-- Dropdown con -->
        <ul v-if="dm.children?.length" class="dropdown-menu">
          <li v-for="child in dm.children" :key="child.maDM" class="dropdown-submenu">
            <router-link
              class="dropdown-item"
              :to="{ name: 'product-list-by-category', params: { maDM: child.maDM } }"
            >
              {{ child.tenDanhMuc }}
            </router-link>

            <!-- Nếu có cháu thì lồng tiếp -->
            <ul v-if="child.children?.length" class="dropdown-menu sub-menu">
              <li v-for="sub in child.children" :key="sub.maDM">
                <router-link
                  class="dropdown-item"
                  :to="{ name: 'product-list-by-category', params: { maDM: sub.maDM } }"
                >
                  {{ sub.tenDanhMuc }}
                </router-link>
              </li>
            </ul>
          </li>
        </ul>
      </div>
    </div>
  </nav>
</template>

<script>
import axios from 'axios'

export default {
  name: 'Navbar',
  data() {
    return {
      danhMucList: [], // ✅ chứa tree categories
    }
  },
  async mounted() {
    try {
      const res = await axios.get('http://localhost:8080/api/danhmuc/tree')
      this.danhMucList = res.data
    } catch (err) {
      console.error('Lỗi khi tải danh mục:', err)
    }
  },
}
</script>

<style scoped>
.menu-nav {
  background-color: #66bb6a;
  margin-bottom: 10px;
}
.menu-nav a {
  color: white;
  font-weight: 500;
  padding: 10px 15px;
  text-decoration: none;
  display: block;
}
.dropdown-menu {
  background-color: #e8f5e9;
  position: absolute;
  display: none;
}
.dropdown-menu a {
  color: #2e7d32;
}
.dropdown:hover > .dropdown-menu {
  display: block;
}
.dropdown-submenu {
  position: relative;
}
.dropdown-submenu:hover > .sub-menu {
  display: block;
  left: 100%;
  top: 0;
}
.sub-menu {
  background-color: #f1f8e9;
  min-width: 180px;
}
</style>
