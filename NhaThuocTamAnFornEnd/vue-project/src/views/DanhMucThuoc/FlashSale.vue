<template>
  <section class="container my-5" v-if="topProducts.length">
    <div class="flash-sale-section">
      <div class="d-flex justify-content-between align-items-center mb-3">
        <h3 class="fw-bold mb-0">⚡ Sản phẩm bán chạy</h3>
        <div>
          <button class="btn-carousel" data-bs-target="#flashSaleCarousel" data-bs-slide="prev">
            <i class="bi bi-chevron-left"></i>
          </button>
          <button class="btn-carousel" data-bs-target="#flashSaleCarousel" data-bs-slide="next">
            <i class="bi bi-chevron-right"></i>
          </button>
        </div>
      </div>

      <div id="flashSaleCarousel" class="carousel slide" data-bs-ride="carousel">
        <div class="carousel-inner">
          <div
            class="carousel-item"
            v-for="(slide, i) in chunkedProducts"
            :key="i"
            :class="{ active: i === 0 }"
          >
            <div class="row row-cols-2 row-cols-md-5 g-3">
              <div class="col" v-for="item in slide" :key="item.maThuoc">
                <div class="card border-0 shadow-sm h-100">
                  <img
                    :src="getImageUrl(item.hinhAnhChinh)"
                    @error="onImageError"
                    class="card-img-top"
                    :alt="item.tenThuoc"
                  />
                  <div class="card-body p-2">
                    <h6 class="card-title small mb-2 text-truncate-2">{{ item.tenThuoc }}</h6>
                    <p class="mb-3">
                      <span class="text-success fw-semibold"></span>
                      <span class="text-success fw-bold ms-1">{{ formatPrice(item.giaBan) }}</span>
                    </p>
                    <router-link
                      :to="`/chi-tiet/${item.maThuoc}`"
                      class="btn btn-buy w-100 text-center"
                    >
                      Mua ngay
                    </router-link>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </section>
</template>

<script>
export default {
  data() {
    return {
      topProducts: [],
    }
  },
  computed: {
    chunkedProducts() {
      const chunkSize = 5
      const chunks = []
      for (let i = 0; i < this.topProducts.length; i += chunkSize) {
        chunks.push(this.topProducts.slice(i, i + chunkSize))
      }
      return chunks
    },
  },
  methods: {
    async fetchTopBanChay() {
      try {
        const res = await fetch('http://localhost:8080/api/thuoc/top-ban-chay')
        this.topProducts = await res.json()
      } catch (err) {
        console.error('❌ Lỗi tải sản phẩm bán chạy:', err)
      }
    },
    getImageUrl(path) {
      if (!path) return '/images/default.jpg'
      if (path.startsWith('http')) return path
      if (!path.startsWith('uploads/')) path = 'uploads/' + path
      return `http://localhost:8080/${path}`
    },
    onImageError(e) {
      e.target.src = '/images/default.jpg'
    },
    formatPrice(value) {
      if (!value) return '0₫'
      return Number(value).toLocaleString('vi-VN', {
        style: 'currency',
        currency: 'VND',
        minimumFractionDigits: 0,
      })
    },
  },
  mounted() {
    this.fetchTopBanChay()
  },
}
</script>

<style scoped>
.flash-sale-section {
  background-color: #4caf50;
  color: white;
  padding: 24px;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 100, 0, 0.2);
}

.flash-sale-section .card {
  border-radius: 12px;
  overflow: hidden;
  border: 1px solid transparent;
  transition:
    transform 0.3s ease,
    box-shadow 0.3s ease,
    border-color 0.3s ease;
  box-shadow: 0 0 8px rgba(0, 0, 0, 0.05);
}
.flash-sale-section .card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 28px rgba(46, 125, 50, 0.4);
  border-color: #2e7d32;
}

.flash-sale-section .card-img-top {
  height: 140px;
  object-fit: contain;
  border-top-left-radius: 12px;
  border-top-right-radius: 12px;
}

.text-truncate-2 {
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
  line-height: 1.4;
  max-height: 2.8em;
}

.btn-carousel {
  background-color: white;
  color: #2e7d32;
  border: 1px solid #a5d6a7;
  width: 40px;
  height: 40px;
  border-radius: 12px;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
  display: inline-flex;
  align-items: center;
  justify-content: center;
  padding: 0;
  margin: 0 4px;
  transition:
    background-color 0.3s ease,
    transform 0.2s ease;
}
.btn-carousel:hover {
  background-color: #dcedc8;
  transform: scale(1.05);
}
.btn-carousel i {
  font-size: 1.2rem;
}

/* ✅ Nút xem chi tiết */
.btn-buy {
  background-color: #2e7d32;
  color: white;
  border: none;
  font-weight: 500;
  border-radius: 10px;
  padding: 8px 0;
  font-size: 15px;
}
.btn-buy:hover {
  background-color: #256429;
  color: #fff;
}
</style>
