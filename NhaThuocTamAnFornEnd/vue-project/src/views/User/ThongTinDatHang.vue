<template>
  <div>
    <Header />
    <Navbar />

    <div class="checkout-wrapper">
      <div class="go-back-btn mb-3">
        <router-link to="/gio-hang" class="btn btn-outline-success">
          <i class="bi bi-arrow-left-circle me-2"></i> Quay lại giỏ hàng
        </router-link>
      </div>

      <div class="checkout-form card p-4">
        <h5 class="fw-bold mb-4">Xác nhận đơn hàng</h5>

        <!-- Danh sách sản phẩm -->
        <h6 class="mb-3">Sản phẩm đã chọn</h6>
        <div
          v-for="item in cartItems"
          :key="item.maBienThe"
          class="product-item d-flex align-items-center mb-3 p-3 bg-light rounded"
        >
          <img :src="item.thumbnails?.[0] || item.img" class="product-img me-3" alt="Sản phẩm" />
          <div class="flex-grow-1">
            <h6 class="mb-1">{{ item.tenSP || item.name }}</h6>
            <div class="text-muted small">{{ item.quyCach || '---' }}</div>
          </div>
          <div class="text-end">
            <div class="fw-bold text-success">{{ formatPrice(item.giaBan || item.price) }}</div>
            <div class="text-muted">SL: {{ item.soLuong }}</div>
          </div>
        </div>

        <!-- Tổng tiền -->
        <div class="text-end mt-3">
          <h5 class="fw-bold">
            Tổng tiền: <span class="text-success">{{ formatPrice(totalPrice) }}</span>
          </h5>
        </div>

        <!-- Voucher -->
        <div class="mt-3">
          <label class="mb-1">🎁 Voucher:</label>
          <div class="input-group">
            <input
              type="text"
              class="form-control"
              :value="selectedVoucherText"
              placeholder="Chưa chọn voucher"
              disabled
            />
            <button class="btn btn-outline-primary" @click="showVoucherModal = true">
              Chọn voucher
            </button>
          </div>
        </div>

        <!-- Giảm giá -->
        <div v-if="giamGia > 0" class="text-end mt-2 text-success">
          ✅ Giảm giá: {{ formatPrice(giamGia) }}
        </div>

        <!-- Tổng sau giảm -->
        <div class="text-end mt-2">
          <h5 class="fw-bold">
            Thanh toán: <span class="text-danger">{{ formatPrice(totalPrice - giamGia) }}</span>
          </h5>
        </div>

        <!-- Thông tin người nhận -->
        <h6 class="mt-4 mb-2">Thông tin người nhận</h6>
        <div class="row g-3 mb-3">
          <div class="col-md-6">
            <input v-model="form.name" class="form-control" placeholder="Họ tên" />
          </div>
          <div class="col-md-6">
            <input v-model="form.phone" class="form-control" placeholder="SĐT" />
          </div>
          <div class="col-12">
            <input v-model="form.address" class="form-control" placeholder="Địa chỉ nhận hàng" />
          </div>
        </div>

        <!-- Phương thức thanh toán -->
        <h6 class="mb-2">Phương thức thanh toán</h6>
        <div class="form-check mb-2" v-for="m in paymentMethods" :key="m.value">
          <input
            class="form-check-input"
            type="radio"
            :value="m.value"
            v-model="form.payment"
            :id="m.value"
          />
          <label class="form-check-label" :for="m.value">{{ m.label }}</label>
        </div>

        <!-- Ghi chú -->
        <textarea
          v-model="form.note"
          class="form-control mt-3"
          rows="2"
          placeholder="Ghi chú (tuỳ chọn)"
        ></textarea>

        <button class="btn btn-success w-100 mt-4" @click="datHang">
          <i class="bi bi-cart-check me-2"></i>Hoàn tất đặt hàng
        </button>
      </div>
    </div>

    <Footer />

    <!-- Modal chọn voucher -->
    <div v-if="showVoucherModal" class="modal-overlay">
      <div class="modal-content card p-4">
        <h5 class="mb-3 text-success">🎯 Danh sách Voucher</h5>

        <div v-if="availableVouchers.length === 0" class="text-muted">
          Hiện chưa có voucher phù hợp.
        </div>

        <div
          v-for="v in availableVouchers"
          :key="v.maKM"
          class="border rounded p-2 mb-2 d-flex justify-content-between align-items-center bg-light"
        >
          <div>
            <div>
              <strong>{{ v.tenKM }}</strong> ({{ v.maCode }})
            </div>
            <div class="small text-muted">
              Giảm: {{ v.loai === 'percent' ? v.giaTri + '%' : formatPrice(v.giaTri) }}, Đơn tối
              thiểu: {{ formatPrice(v.donHangToiThieu) }}
            </div>
          </div>
          <button class="btn btn-sm btn-primary" @click="chonVoucher(v)">Chọn</button>
        </div>

        <div class="text-end">
          <button class="btn btn-secondary mt-2" @click="showVoucherModal = false">Đóng</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import Header from '../User/Header.vue'
import Navbar from '../User/Navbar.vue'
import Footer from '../User/Footer.vue'
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const cartItems = ref([])
const storageKey = ref('cart_temp')

const form = ref({ name: '', phone: '', address: '', payment: 'cod', note: '' })
const paymentMethods = [
  { value: 'cod', label: 'Thanh toán khi nhận hàng (COD)' },
  { value: 'online', label: 'Thanh toán Online (VNPay, Momo...)' },
]

const selectedVoucher = ref(null)
const giamGia = ref(0)
const availableVouchers = ref([])
const showVoucherModal = ref(false)

onMounted(() => {
  const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
  const maKH = userInfo?.maKH || null
  storageKey.value = maKH ? `cart_${maKH}` : 'cart_temp'
  cartItems.value = JSON.parse(localStorage.getItem(storageKey.value)) || []

  if (userInfo) {
    form.value.name = userInfo.hoTen || ''
    form.value.phone = userInfo.soDienThoai || ''
    form.value.address = userInfo.diaChi || ''
  }
  loadAvailableVouchers()
})

function loadAvailableVouchers() {
  const list = JSON.parse(localStorage.getItem('voucherList')) || []
  const today = new Date()
  availableVouchers.value = list.filter((v) => {
    const bd = new Date(v.ngayBatDau)
    const kt = new Date(v.ngayKetThuc)
    const conLai = v.soLuong - (v.daSuDung || 0)
    return v.trangThai && today >= bd && today <= kt && conLai > 0
  })
}

const totalPrice = computed(() =>
  cartItems.value.reduce((total, sp) => total + sp.soLuong * (sp.giaBan || sp.price || 0), 0),
)

const selectedVoucherText = computed(() => {
  if (!selectedVoucher.value) return ''
  return `${selectedVoucher.value.tenKM} (${selectedVoucher.value.maCode})`
})

function formatPrice(val) {
  if (typeof val !== 'number' || isNaN(val)) return '0 ₫'
  return val.toLocaleString('vi-VN', { style: 'currency', currency: 'VND' })
}

function chonVoucher(voucher) {
  if (totalPrice.value < voucher.donHangToiThieu) {
    alert('❌ Đơn hàng chưa đủ điều kiện tối thiểu!')
    return
  }

  let giam = 0
  const giaTri = parseFloat(voucher.giaTri)
  const giaTriToiDa = parseFloat(voucher.giaTriToiDa)

  if (voucher.loai === 'percent') {
    giam = totalPrice.value * (giaTri / 100)
    if (giaTriToiDa > 0) giam = Math.min(giam, giaTriToiDa)
  } else {
    giam = giaTri
  }

  giamGia.value = Math.round(giam)
  selectedVoucher.value = voucher
  showVoucherModal.value = false
  alert('✅ Đã áp dụng voucher thành công!')
}

function datHang() {
  if (!form.value.name || !form.value.phone || !form.value.address) {
    alert('❗ Vui lòng điền đầy đủ thông tin!')
    return
  }

  alert('✅ Đặt hàng thành công!')
  localStorage.removeItem(storageKey.value)
  router.push('/dat-hang-thanh-cong')
}
</script>

<style scoped>
.checkout-wrapper {
  max-width: 720px;
  margin: auto;
  padding: 40px 20px;
}
.product-img {
  width: 70px;
  height: 70px;
  object-fit: contain;
  border-radius: 6px;
}
.modal-overlay {
  position: fixed;
  inset: 0;
  background: rgba(0, 0, 0, 0.4);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 999;
}
.modal-content {
  background: white;
  border-radius: 8px;
  max-height: 90vh;
  overflow-y: auto;
  width: 600px;
}
</style>
