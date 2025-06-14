<template>
  <!-- ✅ MENU NAV -->
  <nav class="menu-nav">
    <div class="container d-flex gap-3 flex-wrap">
      <div class="dropdown">
        <a class="dropdown-toggle" href="#" data-bs-toggle="dropdown">Thực phẩm chức năng</a>
        <ul class="dropdown-menu">
          <li><a class="dropdown-item" href="#">Tăng sức đề kháng</a></li>
          <li><a class="dropdown-item" href="#">Vitamin tổng hợp</a></li>
          <li><a class="dropdown-item" href="#">Omega 3, Canxi</a></li>
        </ul>
      </div>
      <div class="dropdown">
        <a class="dropdown-toggle" href="#" data-bs-toggle="dropdown">Mỹ phẩm</a>
        <ul class="dropdown-menu">
          <li><a class="dropdown-item" href="#">Chăm sóc da</a></li>
          <li><a class="dropdown-item" href="#">Trang điểm</a></li>
        </ul>
      </div>
      <div class="dropdown">
        <a class="dropdown-toggle" href="#" data-bs-toggle="dropdown">Dược phẩm</a>
        <ul class="dropdown-menu">
          <li><a class="dropdown-item" href="#">Thuốc cảm cúm</a></li>
          <li><a class="dropdown-item" href="#">Kháng sinh</a></li>
        </ul>
      </div>
      <a href="#">Mẹ & Bé</a>
      <a href="#">Bao cao su</a>
      <a href="#">Bệnh học</a>
      <a href="#">Giới thiệu</a>
      <a href="#">Dược thư</a>
      <a href="#">Cỏ may mắn</a>
    </div>
  </nav>
</template>

<style scoped>
/* Navbar */
.navbar-top {
  background-color: #43a047;
  color: white;
  padding-top: 16px;
  padding-bottom: 16px;
}

.navbar-top a {
  color: white;
  text-decoration: none;
}

.menu-nav {
  background-color: #66bb6a;
  margin-bottom: 10px; /* 👈 Cách banner 10px */
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
}
.dropdown-menu a {
  color: #2e7d32;
}
.dropdown:hover .dropdown-menu {
  display: block;
}

.section-title {
  margin-top: 60px;
  margin-bottom: 30px;
}
</style>
