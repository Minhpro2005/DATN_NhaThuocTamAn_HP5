<template>
  <div class="product-detail-bg">
    <Header />
    <Navbar />

    <div class="container py-4">
      <div class="product-card" v-if="product">
        <div class="row g-4 align-items-start">
          <div class="col-md-5 text-center">
            <img :src="currentImage" class="product-img" alt="Ảnh chính" ref="productImageRef" />
            <div
              v-if="product.thumbnails?.length"
              class="product-thumbs-slider mt-3 position-relative"
            >
              <button class="btn-slider left" @click="scrollThumbnails('left')">‹</button>
              <div class="product-thumbs d-flex justify-content-center">
                <img
                  v-for="(thumb, index) in product.thumbnails"
                  :key="index"
                  :src="getImageUrl(thumb)"
                  :alt="'ảnh phụ ' + (index + 1)"
                  :class="{ active: currentThumbIndex === index }"
                  @click="setCurrentImage(index)"
                />
              </div>
              <button class="btn-slider right" @click="scrollThumbnails('right')">›</button>
            </div>
          </div>

          <div class="col-md-7">
            <h5><span class="badge-yeuthich">Yêu thích</span> {{ product.tenThuoc }}</h5>
            <div class="rating">
              ★★★★★ <small>{{ product.danhGia || 4 }} đánh giá</small>
            </div>
            <div class="price my-2">{{ formatCurrency(product.giaBan) }}</div>

            <ul class="list-unstyled">
              <li><strong>Xuất xứ:</strong> {{ product.xuatXu }}</li>
              <li><strong>Danh mục:</strong> {{ product.tenDanhMuc }}</li>
              <li><strong>Nhà cung cấp:</strong> {{ product.tenNhaCungCap }}</li>
              <li>
                <strong>Đơn vị tính:</strong> {{ selectedBienThe?.donViTinh || product.donViTinh }}
              </li>
              <li><strong>Quy cách:</strong> {{ selectedBienThe?.quyCach || product.quyCach }}</li>
              <li><strong>Dạng bào chế:</strong> {{ product.dangBaoChe }}</li>
              <li><strong>Thành phần:</strong> {{ product.thanhPhan }}</li>
              <li>
                <strong>Tồn kho:</strong> <span class="text-danger fw-bold">{{ soLuongTon }}</span>
              </li>
            </ul>

            <div class="mb-3">
              <label class="fw-bold">Chọn loại:</label>
              <div class="d-flex flex-wrap gap-2 mt-1">
                <div
                  class="variant-box"
                  :class="{ active: !selectedBienThe }"
                  @click="toggleBienThe(null)"
                >
                  {{ product.donViTinh }}<span v-if="!selectedBienThe" class="check-icon">✔</span>
                </div>
                <div
                  v-for="bt in danhSachBienThe"
                  :key="bt.maBienThe"
                  class="variant-box"
                  :class="{ active: selectedBienThe?.maBienThe === bt.maBienThe }"
                  @click="toggleBienThe(bt)"
                >
                  {{ bt.donViTinh
                  }}<span v-if="selectedBienThe?.maBienThe === bt.maBienThe" class="check-icon"
                    >✔</span
                  >
                </div>
              </div>
            </div>

            <div class="mb-3">
              <label class="fw-bold me-3">Chọn số lượng</label>
              <div class="quantity-selector">
                <button @click="giamSoLuong" class="btn-adjust">−</button>
                <input type="text" class="qty-input" v-model="soLuong" readonly />
                <button @click="tangSoLuong" class="btn-adjust">+</button>
              </div>
            </div>

            <div class="d-flex flex-column flex-md-row gap-3">
              <button v-if="soLuongTon > 0" class="btn btn-cart flex-fill" @click="themVaoGio">
                <i class="bi bi-cart-plus"></i> Thêm vào giỏ
              </button>
              <button v-if="soLuongTon > 0" class="btn btn-buy flex-fill" @click="muaNgay">
                MUA NGAY<br /><small>Nhận hàng tại nhà, đổi trả trong 30 ngày</small>
              </button>
              <button v-else class="btn btn-secondary flex-fill" disabled>HẾT HÀNG</button>
              <button class="btn btn-compare flex-fill" @click="themVaoSoSanh">
                <img
                  src="https://cdn.nhathuoclongchau.com.vn/unsafe/40x0/filters:quality(90)/https://cms-prod.s3-sgn09.fptcloud.com/smalls/48px_fe435f0cad.png"
                  alt="So sánh"
                  class="compare-icon me-1"
                />
                So sánh
              </button>
            </div>
          </div>
        </div>

        <ProductIntroduction />
        <ProductRelated :maDM="product?.maDM" :excludeId="product?.maThuoc" />
        <DanhGia :key="maDanhGiaKey" />
      </div>
    </div>
    <Footer />
    <CompareBar />

    <!-- Toast -->
    <ToastMessage ref="toastRef" />
  </div>
</template>

<script setup>
import { ref, onMounted, watch, provide } from 'vue'
import axios from 'axios'
import { useRoute, useRouter } from 'vue-router'
import Header from './Header.vue'
import Navbar from './Navbar.vue'
import ProductIntroduction from './ProductIntroduction.vue'
import ProductRelated from './ProductRelated.vue'
import DanhGia from './DanhGia.vue'
import Footer from './Footer.vue'
import CompareBar from './CompareBar.vue'
import ToastMessage from '../ToastMessage.vue'
import { CartStore } from './CartStore.js'
import { computed } from 'vue'

const maDanhGiaKey = computed(() => {
  // Nếu có biến thể thì dùng mã biến thể, không thì dùng mã thuốc
  return selectedBienThe.value?.maBienThe || product.value?.maThuoc || Math.random()
})

const route = useRoute()
const router = useRouter()
let id = route.params.id // ✅ Sửa từ const → let để có thể cập nhật khi route thay đổi

const product = ref(null)
const currentImage = ref('')
const danhSachBienThe = ref([])
const selectedBienThe = ref(null)
const currentThumbIndex = ref(0)
const soLuong = ref(1)
const soLuongTon = ref(0)
const productImageRef = ref(null)
const toastRef = ref(null)

// 👇 Truyền dữ liệu xuống component DanhGia
provide('product', product)
provide('selectedBienThe', selectedBienThe)

// Gọi khi component mount lần đầu
onMounted(async () => {
  await fetchProduct()
  await fetchBienThe()

  // ✅ Nếu có query ?review=true thì cuộn đến phần đánh giá
  if (route.query.review === 'true') {
    setTimeout(() => {
      const reviewSection = document.querySelector('.review-wrapper')
      if (reviewSection) {
        reviewSection.scrollIntoView({ behavior: 'smooth' })
      }
    }, 300) // Chờ DOM render xong
  }
})

// Gọi lại khi biến thể hoặc sản phẩm thay đổi để cập nhật tồn kho
watch([selectedBienThe, product], async () => {
  if (product.value) await fetchTonKho()
})

// ✅ Gọi lại khi route thay đổi ID
watch(
  () => route.params.id,
  async (newId) => {
    id = newId
    await fetchProduct()
    await fetchBienThe()
  },
)

async function fetchProduct() {
  try {
    const res = await axios.get(`http://localhost:8080/api/thuoc/${id}`)
    product.value = res.data
    product.value.giaBanGoc = product.value.giaBan
    currentImage.value = getImageUrl(
      product.value.hinhAnhChinh || product.value.thumbnails?.[0] || '',
    )
  } catch (err) {
    console.error('Lỗi tải sản phẩm:', err)
  }
}

async function fetchBienThe() {
  try {
    const res = await axios.get(`http://localhost:8080/api/bienthe/thuoc/${id}`)
    danhSachBienThe.value = res.data
  } catch (err) {
    console.error('Lỗi lấy biến thể:', err)
  }
}

async function fetchTonKho() {
  try {
    const res = await axios.get(`http://localhost:8080/api/kho-hang/so-luong-ton`, {
      params: {
        maThuoc: selectedBienThe.value ? null : product.value?.maThuoc,
        maBienThe: selectedBienThe.value?.maBienThe || null,
      },
    })
    soLuongTon.value = res.data
  } catch (err) {
    console.error('Lỗi lấy số lượng tồn:', err)
  }
}

function toggleBienThe(bt) {
  if (!bt) {
    selectedBienThe.value = null
    product.value.giaBan = product.value.giaBanGoc
    currentImage.value = getImageUrl(product.value.hinhAnhChinh)
    return
  }
  selectedBienThe.value = bt
  product.value.giaBan = bt.giaBan
  currentImage.value = getImageUrl(bt.hinhAnh || product.value.hinhAnhChinh)
}

function scrollThumbnails(direction) {
  const total = product.value.thumbnails.length
  currentThumbIndex.value =
    direction === 'left'
      ? (currentThumbIndex.value - 1 + total) % total
      : (currentThumbIndex.value + 1) % total
  currentImage.value = getImageUrl(product.value.thumbnails[currentThumbIndex.value])
}

function setCurrentImage(index) {
  currentThumbIndex.value = index
  currentImage.value = getImageUrl(product.value.thumbnails[index])
}

function tangSoLuong() {
  soLuong.value++
}
function giamSoLuong() {
  if (soLuong.value > 1) soLuong.value--
}

function flyToCart() {
  const img = productImageRef.value
  const cartIcon = document.querySelector('.bi-cart') || document.querySelector('.cart-icon')
  if (!img || !cartIcon) return

  const imgRect = img.getBoundingClientRect()
  const cartRect = cartIcon.getBoundingClientRect()
  const clone = img.cloneNode(true)

  clone.style.position = 'fixed'
  clone.style.left = imgRect.left + 'px'
  clone.style.top = imgRect.top + 'px'
  clone.style.width = imgRect.width + 'px'
  clone.style.height = imgRect.height + 'px'
  clone.style.transition = 'all 0.8s ease-in-out'
  clone.style.zIndex = 1000
  document.body.appendChild(clone)

  getComputedStyle(clone).opacity

  clone.style.left = cartRect.left + 'px'
  clone.style.top = cartRect.top + 'px'
  clone.style.width = '30px'
  clone.style.height = '30px'
  clone.style.opacity = 0.5

  setTimeout(() => {
    clone.remove()
  }, 900)
}

function themVaoGio() {
  const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
  const maKH = userInfo?.maKH || null
  const storageKey = maKH ? `cart_${maKH}` : 'cart_temp'
  const cart = JSON.parse(localStorage.getItem(storageKey)) || []

  const sp = selectedBienThe.value
    ? {
        ...selectedBienThe.value,
        tenThuoc: product.value.tenThuoc,
        soLuong: soLuong.value,
        donViTinh: selectedBienThe.value.donViTinh,
        hinhAnh: selectedBienThe.value.hinhAnh || product.value.hinhAnhChinh,
      }
    : {
        maThuoc: product.value.maThuoc,
        tenThuoc: product.value.tenThuoc,
        giaBan: product.value.giaBan,
        soLuong: soLuong.value,
        donViTinh: product.value.donViTinh,
        hinhAnh: product.value.hinhAnhChinh,
      }

  const exists = cart.find((item) =>
    selectedBienThe.value
      ? item.maBienThe === selectedBienThe.value.maBienThe
      : item.maThuoc === product.value.maThuoc && !item.maBienThe,
  )

  const tongSoLuong = exists ? exists.soLuong + soLuong.value : soLuong.value

  if (tongSoLuong > soLuongTon.value) {
    toastRef.value?.show(
      `Không thể thêm ${soLuong.value} sản phẩm. Tồn kho còn lại: ${soLuongTon.value - (exists?.soLuong || 0)}`,
      'warning',
    )
    return
  }

  if (exists) {
    exists.soLuong = tongSoLuong
    toastRef.value?.show('Đã tăng số lượng trong giỏ', 'info')
  } else {
    cart.push(sp)
    toastRef.value?.show('Đã thêm vào giỏ', 'success')
  }

  localStorage.setItem(storageKey, JSON.stringify(cart))
  CartStore.updateCount()
  flyToCart()
}

function themVaoSoSanh() {
  const key = 'compare_list'
  const list = JSON.parse(localStorage.getItem(key)) || []

  // Tạo object sản phẩm hiện tại (gồm thông tin mô tả chuyên sâu)
  const currentItem = selectedBienThe.value
    ? {
        maBienThe: selectedBienThe.value.maBienThe,
        maThuoc: product.value.maThuoc,
        tenThuoc: product.value.tenThuoc,
        giaBan: selectedBienThe.value.giaBan,
        hinhAnh: selectedBienThe.value.hinhAnh || product.value.hinhAnhChinh,
        donViTinh: selectedBienThe.value.donViTinh,
        loai: 'Biến thể',
        moTaThem: product.value.moTaThem,
        thanhPhan: product.value.thanhPhan,
        congDung: product.value.congDung,
        huongDanSuDung: product.value.huongDanSuDung,
      }
    : {
        maThuoc: product.value.maThuoc,
        tenThuoc: product.value.tenThuoc,
        giaBan: product.value.giaBan,
        hinhAnh: product.value.hinhAnhChinh,
        donViTinh: product.value.donViTinh,
        loai: 'Thuốc chính',
        moTaThem: product.value.moTaThem,
        thanhPhan: product.value.thanhPhan,
        congDung: product.value.congDung,
        huongDanSuDung: product.value.huongDanSuDung,
      }

  // Kiểm tra trùng
  const exists = list.find((sp) =>
    selectedBienThe.value
      ? sp.maBienThe === currentItem.maBienThe
      : sp.maThuoc === currentItem.maThuoc && !sp.maBienThe,
  )

  if (!exists) {
    if (list.length >= 4) {
      toastRef.value?.show('Chỉ có thể so sánh tối đa 4 sản phẩm!', 'warning')
      return
    }

    list.push(currentItem)
    localStorage.setItem(key, JSON.stringify(list))
    window.dispatchEvent(new Event('compare-updated'))
    toastRef.value?.show('Đã thêm vào danh sách so sánh', 'success')
  } else {
    toastRef.value?.show('Sản phẩm đã có trong danh sách so sánh', 'info')
  }
}

function muaNgay() {
  themVaoGio()
  router.push('/gio-hang')
}

function getImageUrl(path) {
  return path?.startsWith('http') ? path : `http://localhost:8080/${path}`
}

function formatCurrency(v) {
  return v?.toLocaleString('vi-VN', { style: 'currency', currency: 'VND' }) || ''
}
</script>

<style scoped>
.product-detail-bg {
  background-color: #f5f5f5;
  min-height: 100vh;
}
.product-card {
  background: white;
  padding: 24px;
  border-radius: 16px;
  max-width: 960px;
  margin: auto;
}
.product-img {
  width: 100%;
  height: 300px;
  object-fit: contain;
  background-color: #fff;
  padding: 8px;
}
.product-thumbs-slider {
  position: relative;
  padding: 0 20px;
}
.product-thumbs {
  scroll-behavior: smooth;
  overflow-x: auto;
  display: flex;
  justify-content: center;
}
.product-thumbs img {
  width: 90px;
  height: 90px;
  object-fit: contain;
  border: 2px solid #ccc;
  border-radius: 6px;
  margin: 6px;
  cursor: pointer;
  transition: 0.2s;
}
.product-thumbs img.active {
  border-color: #43a047;
}
.btn-slider {
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  width: 36px;
  height: 36px;
  border-radius: 50%;
  background: white;
  border: 1px solid #ccc;
  font-size: 1.5rem;
  font-weight: bold;
  color: #444;
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 10;
  cursor: pointer;
}
.btn-slider.left {
  left: -18px;
}
.btn-slider.right {
  right: -18px;
}
.price {
  color: red;
  font-size: 1.5rem;
  font-weight: bold;
}
.rating {
  color: red;
}
.badge-yeuthich {
  background: #43a047;
  color: white;
  padding: 4px 8px;
  font-size: 0.8rem;
  border-radius: 4px;
}
.variant-box {
  border: 2px solid #ccc;
  padding: 10px 16px;
  border-radius: 12px;
  cursor: pointer;
  font-weight: bold;
  background: white;
  position: relative;
}
.variant-box.active {
  border-color: #d32f2f;
  color: #d32f2f;
}
.check-icon {
  position: absolute;
  top: -6px;
  right: -6px;
  background: #d32f2f;
  color: white;
  width: 18px;
  height: 18px;
  font-size: 12px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
}
.btn-buy,
.btn-cart {
  padding: 12px;
  font-size: 1.1rem;
  font-weight: bold;
  border: none;
  border-radius: 6px;
  transition: background 0.2s;
}
.btn-cart {
  background-color: #4caf50;
  color: white;
}
.btn-buy {
  background-color: #ffa000;
  color: white;
}
.btn-cart:hover {
  background-color: #388e3c;
}
.btn-buy:hover {
  background-color: #ff8f00;
}
.quantity-selector {
  display: inline-flex;
  border: 1px solid #ccc;
  border-radius: 20px;
  overflow: hidden;
  align-items: center;
  height: 36px;
}
.btn-adjust {
  background: white;
  border: none;
  width: 36px;
  height: 100%;
  font-size: 20px;
  color: #555;
  cursor: pointer;
  border-right: 1px solid #ccc;
}
.btn-adjust:last-child {
  border-right: none;
  border-left: 1px solid #ccc;
}
.btn-adjust:hover {
  background: #f5f5f5;
}
.qty-input {
  width: 50px;
  text-align: center;
  border: none;
  font-weight: bold;
  font-size: 16px;
}
.qty-input:focus {
  outline: none;
}
.btn-compare {
  background-color: white;
  color: #2196f3;
  border: 2px solid #2196f3;
  font-weight: bold;
  padding: 10px;
  font-size: 1rem;
  border-radius: 6px;
  transition: all 0.3s ease;
}

.btn-compare:hover {
  background-color: #2196f3;
  color: white;
  box-shadow: 0 2px 8px rgba(33, 150, 243, 0.3);
}
</style>
