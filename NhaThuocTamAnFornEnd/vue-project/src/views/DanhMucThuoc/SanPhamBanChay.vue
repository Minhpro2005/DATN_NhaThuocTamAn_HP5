<template>
  <section>
    <div class="container">
      <h2 class="section-title">
        <i class="bi bi-fire text-success me-2"></i>
        Sản phẩm bán chạy
      </h2>
      <div class="row">
        <div class="col-2-4 mb-4" v-for="item in products" :key="item.id">
          <router-link :to="`/product-detail/${item.id}`" class="text-decoration-none">
            <div class="card h-100">
              <img :src="item.img" class="card-img-top" :alt="item.name" />
              <div class="card-body">
                <h5 class="card-title">{{ item.name }}</h5>
                <p class="card-text text-success fw-bold">Giá: {{ formatCurrency(item.price) }}</p>
                <button class="btn btn-green w-100">Mua ngay</button>
              </div>
            </div>
          </router-link>
        </div>
      </div>
    </div>
  </section>
</template>

<script setup>
import { ref } from 'vue'

const products = ref([
  {
    id: '1',
    name: 'Bio Acimin Fiber',
    price: 147000,
    img: 'vue-project\public\Img\anhthuoc1.jpg',
  },
  {
    id: '2',
    name: 'Prospan Syrup',
    price: 120000,
    img: 'vue-project\public\Img\anhthuoc2.jpg',
  },
  {
    id: '3',
    name: 'Biovital Multivitamin',
    price: 135000,
    img: 'vue-project\public\Img\anhthuoc3.jpg',
  },
  {
    id: '4',
    name: 'Dizigone Nano Bạc',
    price: 125000,
    img: 'vue-project\public\Img\anhthuoc4.webp',
  },
  {
    id: '5',
    name: 'Vitamin B Complex',
    price: 110000,
    img: 'vue-project\public\Img\anhthuoc5.webp',
  },
])

function formatCurrency(value) {
  return value.toLocaleString('vi-VN', { style: 'currency', currency: 'VND' })
}
</script>

<style scoped>
.col-2-4 {
  flex: 0 0 auto;
  width: 20%;
}
@media (max-width: 768px) {
  .col-2-4 {
    width: 50%;
  }
}
@media (max-width: 576px) {
  .col-2-4 {
    width: 100%;
  }
}
.card {
  border-radius: 12px;
  overflow: hidden;
  transition:
    transform 0.3s ease,
    box-shadow 0.3s ease;
  box-shadow: 0 0 8px rgba(0, 0, 0, 0.05);
}
.card:hover {
  transform: translateY(-5px);
  box-shadow: 0 6px 24px rgba(0, 100, 0, 0.4);
}
.card-img-top {
  border-top-left-radius: 12px;
  border-top-right-radius: 12px;
  object-fit: cover;
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
.section-title {
  margin-top: 20px;
  margin-bottom: 20px;
  font-weight: bold;
}
</style>
