<template>
  <div>
    <Header />
    <Navbar />
    <BannerSlide />
    <Banner />
    <ChucNangPhu />
    <FlashSale />
    <SanPhamBanChay />
    <News />
    <Footer />
  </div>
</template>

<script setup>
import Header from './Header.vue'
import Navbar from './Navbar.vue'
import BannerSlide from './BannerSlide.vue'
import Banner from './Banner.vue'
import ChucNangPhu from './ChucNangPhu.vue'
import FlashSale from '../DanhMucThuoc/FlashSale.vue'
import SanPhamBanChay from '../DanhMucThuoc/SanPhamBanChay.vue'
import News from './News.vue'
import Footer from './Footer.vue'
</script>

<style scoped>
body {
  font-family: 'Segoe UI', sans-serif;
  background-color: #f8f9fa;
}
.section-title {
  margin-top: 60px;
  margin-bottom: 30px;
}
.btn-green {
  background-color: #2e7d32;
  color: white;
  border: none;
  border-radius: 8px;
  padding: 10px 20px;
  transition: background-color 0.3s ease;
}
.btn-green:hover {
  background-color: #1b5e20;
}
</style>
