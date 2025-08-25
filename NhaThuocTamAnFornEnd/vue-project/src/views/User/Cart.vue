<template>
  <div>
    <Header />
    <Navbar />

    <main class="cart-page container my-4">
      <router-link to="/home" class="back-link">
        <i class="bi bi-arrow-left"></i> Tiếp tục mua sắm
      </router-link>

      <div v-if="gioHang.length === 0" class="alert alert-warning mt-4">
        Giỏ hàng đang trống. <router-link to="/home">Mua sắm ngay</router-link>
      </div>

      <div v-else class="row mt-3">
        <div class="col-md-8">
          <div class="cart-box p-3 bg-white rounded shadow-sm">
            <div
              class="row fw-bold text-secondary border-bottom pb-2 mb-3 align-items-center text-center"
            >
              <div class="col-md-5 text-start d-flex align-items-center">
                <input
                  type="checkbox"
                  class="form-check-input me-2"
                  v-model="chonTatCa"
                  @change="toggleTatCa"
                />
                <span>Chọn tất cả</span>
              </div>
              <div class="col-md-2">Giá</div>
              <div class="col-md-2">Số lượng</div>
              <div class="col-md-2">Đơn vị</div>
              <div class="col-md-1"></div>
            </div>

            <div
              v-for="(sp, index) in gioHang"
              :key="index"
              class="row mb-3 align-items-center text-center"
            >
              <div
                class="col-md-5 text-start d-flex align-items-center"
                :class="{ 'opacity-50': trangThaiTonKho[index]?.hetHang }"
              >
                <input
                  type="checkbox"
                  class="form-check-input me-2"
                  v-model="dsDaChon[index]"
                  :disabled="trangThaiTonKho[index]?.hetHang"
                />
                <img
                  :src="getImageUrl(sp.hinhAnh || sp.hinhAnhChinh || sp.thumbnails?.[0])"
                  alt="Sản phẩm"
                  style="width: 60px; height: 60px; object-fit: contain"
                  class="me-2"
                />
                <div>
                  <!-- Tên + Quy cách -->
                  <div class="fw-semibold">
                    {{ sp.tenThuoc || sp.tenSP || sp.name }}
                    <span v-if="sp.moTaQuyCach"> - {{ sp.moTaQuyCach }}</span>
                  </div>

                  <div class="text-danger small" v-if="trangThaiTonKho[index]?.hetHang">
                    (Sản phẩm đã hết hàng)
                  </div>
                  <div class="text-warning small" v-else-if="trangThaiTonKho[index]?.vuotTon">
                    (Vượt tồn kho: còn {{ trangThaiTonKho[index]?.tonKho }})
                  </div>
                </div>
              </div>

              <div class="col-md-2 fw-bold text-success">
                {{ formatCurrency(sp.giaBan || sp.price) }}
              </div>

              <div class="col-md-2 d-flex justify-content-center align-items-center gap-2">
                <div class="d-flex align-items-center border rounded px-1">
                  <button
                    class="btn btn-sm btn-outline-secondary border-0"
                    @click="giamSoLuong(index)"
                  >
                    −
                  </button>
                  <span class="px-2">{{ sp.soLuong }}</span>
                  <button
                    class="btn btn-sm btn-outline-secondary border-0"
                    @click="tangSoLuong(index)"
                  >
                    +
                  </button>
                </div>
              </div>

              <!-- Chỉ đơn vị -->
              <div class="col-md-2">
                <span>{{ sp.donViTinh }}</span>
              </div>

              <div class="col-md-1">
                <i
                  class="bi bi-trash3 text-danger"
                  role="button"
                  title="Xóa sản phẩm"
                  @click="xoaSanPham(index)"
                ></i>
              </div>
            </div>
          </div>
        </div>

        <div class="col-md-4">
          <div class="p-3 bg-white rounded shadow-sm">
            <div class="d-flex justify-content-between align-items-center mb-2">
              <strong>Tổng tiền</strong>
              <span class="fs-5 text-success fw-bold">{{ formatCurrency(tongTien) }}</span>
            </div>

            <button
              class="btn btn-green w-100 mt-3"
              @click="thanhToan"
              :disabled="sanPhamDaChon.length === 0"
            >
              Mua hàng
            </button>

            <p class="small text-muted mt-2">
              Bằng việc đặt mua, bạn đồng ý với <a href="#">Điều khoản dịch vụ</a> và
              <a href="#">Điều khoản bảo mật</a>.
            </p>
          </div>
        </div>
      </div>
    </main>

    <ToastMessage ref="toastRef" />
    <Footer />
  </div>
</template>

<script setup>
import Header from '../User/Header.vue'
import Navbar from '../User/Navbar.vue'
import Footer from '../User/Footer.vue'
import ToastMessage from '../ToastMessage.vue'
import { CartStore } from './CartStore.js'
import { ref, onMounted, computed, watch } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'

const router = useRouter()
const toastRef = ref(null)

const gioHang = ref([])
const storageKey = ref('cart_temp')
const chonTatCa = ref(false)
const dsDaChon = ref([])
const trangThaiTonKho = ref([])

onMounted(async () => {
  const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
  const maKH = userInfo?.maKH || null
  storageKey.value = maKH ? `cart_${maKH}` : 'cart_temp'
  gioHang.value = JSON.parse(localStorage.getItem(storageKey.value)) || []

  if (gioHang.value.length > 0) {
    await locBoSanPhamDaXoa()
    kiemTraTonKho()
  }
})

async function locBoSanPhamDaXoa() {
  const ketQua = []

  for (const sp of gioHang.value) {
    try {
      const res = await axios.get(`http://localhost:8080/api/thuoc/${sp.maThuoc}`)
      if (res.data && res.data.daXoa === false) {
        ketQua.push(sp)
      }
    } catch (err) {
      // Nếu không tìm thấy thuốc (có thể đã bị xóa) thì bỏ qua
      console.warn('Bỏ sản phẩm đã bị xóa:', sp.tenThuoc || sp.maThuoc)
    }
  }

  if (ketQua.length < gioHang.value.length) {
    showToast(
      '🗑 Một số sản phẩm đã bị xóa khỏi hệ thống và không hiển thị trong giỏ hàng.',
      'warning',
    )
  }

  gioHang.value = ketQua
  capNhatLocal()
}

function showToast(msg, type = 'success') {
  toastRef.value?.show(msg, type)
}

async function kiemTraTonKho() {
  const result = []

  for (const sp of gioHang.value) {
    try {
      const res = await axios.get('http://localhost:8080/api/kho-hang/so-luong-ton', {
        params: {
          maThuoc: sp.maBienThe ? null : sp.maThuoc,
          maBienThe: sp.maBienThe || null,
        },
      })
      const tonKho = res.data
      result.push({
        hetHang: tonKho <= 0,
        vuotTon: sp.soLuong > tonKho,
        tonKho,
      })
    } catch (err) {
      result.push({ hetHang: false, vuotTon: false, tonKho: 9999 })
    }
  }

  trangThaiTonKho.value = result
  dsDaChon.value = gioHang.value.map((_, i) => !result[i].hetHang)
  chonTatCa.value = dsDaChon.value.every(Boolean)

  if (result.some((r) => r.hetHang)) {
    showToast('⚠ Một số sản phẩm đã <strong>hết hàng</strong> và không thể thanh toán.', 'danger')
  }
}

function toggleTatCa() {
  dsDaChon.value = gioHang.value.map(
    (_, i) => !trangThaiTonKho.value[i]?.hetHang && chonTatCa.value,
  )
}

watch(
  dsDaChon,
  (val) => {
    if (val.length === 0) return
    chonTatCa.value = val.every((v, i) => v || trangThaiTonKho.value[i]?.hetHang)
  },
  { deep: true },
)

const sanPhamDaChon = computed(() => gioHang.value.filter((_, index) => dsDaChon.value[index]))

const tongTien = computed(() =>
  sanPhamDaChon.value.reduce((tong, sp) => {
    const price = sp.giaBan || sp.price || 0
    return tong + sp.soLuong * price
  }, 0),
)

function giamSoLuong(index) {
  if (gioHang.value[index].soLuong <= 1) {
    showToast('⚠ Số lượng tối thiểu là 1.', 'warning')
    return
  }
  gioHang.value[index].soLuong--
  capNhatLocal()
  kiemTraTonKho()
}

function tangSoLuong(index) {
  const tonKho = trangThaiTonKho.value[index]?.tonKho || 9999
  if (gioHang.value[index].soLuong >= tonKho) {
    showToast(`⚠ Không thể tăng số lượng. Tồn kho chỉ còn <strong>${tonKho}</strong>.`, 'warning')
    return
  }
  gioHang.value[index].soLuong++
  capNhatLocal()
  kiemTraTonKho()
}

function xoaSanPham(index) {
  gioHang.value.splice(index, 1)
  dsDaChon.value.splice(index, 1)
  trangThaiTonKho.value.splice(index, 1)
  capNhatLocal()
  showToast('🗑 Đã xóa sản phẩm khỏi giỏ hàng.', 'success')
}

function capNhatLocal() {
  localStorage.setItem(storageKey.value, JSON.stringify(gioHang.value))
  CartStore.updateCount()
}

function thanhToan() {
  const coLoi = sanPhamDaChon.value.some((_, i) => {
    const index = gioHang.value.findIndex((sp) => sp === sanPhamDaChon.value[i])
    return trangThaiTonKho.value[index]?.hetHang || trangThaiTonKho.value[index]?.vuotTon
  })
  if (coLoi) {
    showToast(
      '❌ Không thể thanh toán vì có sản phẩm <strong>hết hàng</strong> hoặc <strong>vượt kho</strong>.',
      'danger',
    )
    return
  }

  localStorage.setItem('selectedItems', JSON.stringify(sanPhamDaChon.value))
  router.push('/thong-tin-dat-hang')
}

function formatCurrency(v) {
  return v?.toLocaleString('vi-VN', { style: 'currency', currency: 'VND' }) || '0 ₫'
}

function getImageUrl(path) {
  return path?.startsWith('http') ? path : `http://localhost:8080/${path}`
}
</script>

<style scoped>
.cart-page {
  font-family: 'Segoe UI', sans-serif;
}
.back-link {
  display: inline-block;
  background-color: #2e7d32;
  color: white;
  font-weight: 500;
  padding: 6px 16px;
  border-radius: 6px;
  text-decoration: none;
}
.back-link:hover {
  background-color: #1b5e20;
}
.btn-green {
  background-color: #2e7d32;
  color: white;
  border: none;
  padding: 10px 16px;
  border-radius: 8px;
}
.btn-green:hover {
  background-color: #1b5e20;
}
button.btn-sm {
  min-width: 30px;
  padding: 2px 6px;
}
.opacity-50 {
  opacity: 0.5;
}
</style>
