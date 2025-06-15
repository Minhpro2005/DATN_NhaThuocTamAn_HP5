<template>
  <div class="product-detail-bg">
    <Header />
    <Navbar />

    <div class="container py-4">
      <div class="product-card">
        <div class="row g-4 align-items-start" v-if="product">
          <!-- Ảnh chính -->
          <div class="col-md-5 text-center">
            <img :src="currentImage" class="product-img" alt="Ảnh chính" />
            <div v-if="product.thumbnails" class="d-flex product-thumbs mt-3">
              <img
                v-for="(thumb, index) in product.thumbnails"
                :key="index"
                :src="thumb"
                :alt="'ảnh phụ ' + (index + 1)"
                :class="{ active: currentImage === thumb }"
                @click="currentImage = thumb"
              />
            </div>
          </div>

          <!-- Thông tin -->
          <div class="col-md-7">
            <h5>
              <span class="badge-yeuthich">Yêu thích</span> {{ product.tenSP || product.name }}
            </h5>
            <div class="rating">
              ★★★★★ <small>{{ product.danhGia || 4 }} đánh giá</small>
            </div>
            <div class="price my-2">{{ formatCurrency(product.giaBan || product.price) }}</div>

            <ul class="list-unstyled" v-if="product.tenSP">
              <li><strong>Xuất xứ:</strong> {{ product.xuatXu }}</li>
              <li><strong>Quy cách:</strong> {{ product.quyCach }}</li>
              <li><strong>Thương hiệu:</strong> {{ product.thuongHieu }}</li>
              <li><strong>Chuyên mục:</strong> {{ product.chuyenMuc }}</li>
            </ul>

            <p v-html="product.moTa || ''"></p>

            <div class="d-flex flex-column flex-md-row gap-3">
              <button class="btn btn-cart flex-fill" @click="themVaoGio">
                <i class="bi bi-cart-plus"></i> Thêm vào giỏ
              </button>
              <button class="btn btn-buy flex-fill" @click="muaNgay">
                MUA NGAY<br /><small>Nhận hàng tại nhà, đổi trả trong 30 ngày</small>
              </button>
            </div>

            <div class="form-note d-flex align-items-center gap-2 mt-3">
              <input
                type="text"
                class="form-control"
                placeholder="Để lại số điện thoại, chúng tôi gọi lại ngay"
              />
              <button>GỬI</button>
            </div>

            <p class="text-success mt-3">
              ✅ Sản phẩm đang được chú ý, có <strong>4 người</strong> thêm vào giỏ hàng &
              <strong class="text-danger">10</strong> người đang xem
            </p>
          </div>
        </div>
      </div>

      <ProductIntroduction />
      <ProductRelated />
      <DanhGia />
    </div>
  </div>
</template>

<script setup>
import Header from './Header.vue'
import Navbar from './Navbar.vue'
import CartStore from './CartStore.js'
import ProductIntroduction from './ProductIntroduction.vue'
import ProductRelated from './ProductRelated.vue'
import DanhGia from './DanhGia.vue'
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()

const id = route.params.id
const product = ref(null)
const currentImage = ref('')

const products = [
  {
    id: '1',
    tenSP: 'Bio Acimin Fiber',
    giaBan: 147000,
    danhGia: 4,
    moTa: '<strong>Bioacimin Fiber</strong> giúp bổ sung chất xơ, cân bằng vi sinh đường ruột và phòng ngừa táo bón.',
    xuatXu: 'Việt Nam',
    quyCach: 'Hộp 30 gói x 4g',
    thuongHieu: 'Meracine (Dược Việt Đức)',
    chuyenMuc: 'Chất xơ',
    thumbnails: [
      'vue-project\public\Img\anhthuoc1.jpg',
      'vue-project\public\Img\anhthuoc1.2.jpg',
      'vue-project\public\Img\anhthuoc1.3.jpg',
    ],
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
]

onMounted(() => {
  product.value = products.find((p) => p.id === id)
  currentImage.value = product.value?.thumbnails?.[0] || product.value?.img || ''
})

function themVaoGio() {
  const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
  const maKH = userInfo?.maKH || null
  const storageKey = maKH ? `cart_${maKH}` : 'cart_temp'

  const cart = JSON.parse(localStorage.getItem(storageKey)) || []
  const exists = cart.find((item) => item.id === product.value.id)

  if (exists) {
    exists.soLuong += 1
    alert('🛒 Đã tăng số lượng trong giỏ hàng')
  } else {
    cart.push({ ...product.value, soLuong: 1 })
    alert('✅ Đã thêm sản phẩm vào giỏ hàng')
  }

  localStorage.setItem(storageKey, JSON.stringify(cart))
  CartStore.updateCount()
}

function muaNgay() {
  themVaoGio()
  router.push('/gio-hang')
}

function formatCurrency(v) {
  return v?.toLocaleString('vi-VN', { style: 'currency', currency: 'VND' }) || ''
}
</script>

<style scoped>
.product-detail-bg {
  background-color: #f5f5f5;
  min-height: 100vh;
  padding-bottom: 40px;
}
.product-card {
  background-color: white;
  border-radius: 16px;
  padding: 24px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  max-width: 960px;
  margin: auto;
}
.product-img {
  width: 100%;
  max-height: 350px;
  object-fit: contain;
  border-radius: 12px;
}
.product-thumbs {
  justify-content: center;
  margin-top: 12px;
  flex-wrap: wrap;
}
.product-thumbs img {
  width: 90px;
  height: 90px;
  object-fit: contain;
  border: 2px solid #ccc;
  border-radius: 6px;
  margin: 6px;
  cursor: pointer;
  transition:
    border 0.3s,
    transform 0.2s;
}
.product-thumbs img:hover {
  border-color: #66bb6a;
  transform: scale(1.05);
}
.product-thumbs img.active {
  border: 2px solid #43a047;
}
.price {
  color: red;
  font-size: 1.5rem;
  font-weight: bold;
}
.btn-buy {
  background-color: #ffa000;
  color: white;
  font-weight: bold;
  padding: 12px;
  font-size: 1.1rem;
  border: none;
  border-radius: 6px;
  transition: background-color 0.2s;
}
.btn-buy:hover {
  background-color: #ff8f00;
}
.btn-cart {
  background-color: #4caf50;
  color: white;
  font-weight: bold;
  padding: 12px;
  font-size: 1.1rem;
  border: none;
  border-radius: 6px;
  transition: background-color 0.2s;
}
.btn-cart:hover {
  background-color: #388e3c;
}
.badge-yeuthich {
  background-color: #43a047;
  color: white;
  padding: 4px 8px;
  font-size: 0.8rem;
  border-radius: 4px;
}
.rating {
  color: red;
  font-size: 1.1rem;
}
.form-note input {
  border: 1px solid #ccc;
  border-radius: 6px;
  padding: 8px;
}
.form-note button {
  background-color: #43a047;
  color: white;
  border-radius: 6px;
  padding: 8px 16px;
  border: none;
  font-weight: bold;
}
.form-note button:hover {
  background-color: #388e3c;
}
@media (max-width: 576px) {
  .product-thumbs {
    overflow-x: auto;
    flex-wrap: nowrap;
    justify-content: flex-start;
  }
  .product-thumbs img {
    width: 70px;
    height: 70px;
  }
}
</style>
