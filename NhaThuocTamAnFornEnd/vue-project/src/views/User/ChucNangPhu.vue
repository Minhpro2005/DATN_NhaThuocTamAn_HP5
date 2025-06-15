<template>
  <div class="container py-4">
    <div class="row g-3 justify-content-center">
      <div class="col-md-3" v-for="item in items" :key="item.title">
        <router-link :to="item.link" class="text-decoration-none">
          <div
            class="bg-light-green text-dark rounded-3 d-flex align-items-center gap-3 p-3 h-100 hover-card"
          >
            <img :src="item.icon" alt="icon" width="40" height="40" />
            <div class="fw-semibold">{{ item.title }}</div>
          </div>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script setup>
const items = [
  {
    title: 'Gợi ý mua thuốc theo triệu trứng bệnh',
    icon: 'https://cdn-icons-png.flaticon.com/128/2622/2622693.png',
    link: '/goi-y-mua-thuoc-theo-trieu-trung', // Đường dẫn Vue Router
  },
  {
    title: 'Đơn thuốc của tôi',
    icon: 'https://cdn-icons-png.flaticon.com/128/10298/10298814.png',
    link: '/don-thuoc',
  },
]
</script>

<style scoped>
.bg-light-green {
  background-color: #e8f5e9;
}

.hover-card {
  transition: 0.2s ease;
}
.hover-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

router-link {
  text-decoration: none;
}
</style>
