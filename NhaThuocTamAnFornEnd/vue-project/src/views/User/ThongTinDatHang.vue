<template>
  <div>
    <Header />
    <Navbar />

    <div class="checkout-wrapper">
      <div class="checkout-form card p-4">
        <h5 class="fw-bold mb-4">Xác nhận đơn hàng</h5>

        <!-- Danh sách sản phẩm -->
        <div
          v-for="item in cartItems"
          :key="item.maBienThe || item.maThuoc"
          class="product-item d-flex align-items-center mb-3 p-2 bg-light rounded"
        >
          <img :src="getImageUrl(item.hinhAnh)" class="product-img me-3" />
          <div class="flex-grow-1">
            <h6 class="mb-1">{{ item.tenThuoc }}</h6>
            <small class="text-muted">SL: {{ item.soLuong }}</small>
          </div>
          <div class="fw-bold text-success">{{ formatPrice(item.giaBan) }}</div>
        </div>

        <!-- Tổng tiền -->
        <div class="text-end my-2">
          <p>
            Tạm tính: <strong>{{ formatPrice(tongTienGoc) }}</strong>
          </p>
          <p v-if="appliedVoucher">
            Giảm giá:
            <strong class="text-danger">-{{ formatPrice(tienGiamGia) }}</strong>
          </p>
          <h5 class="fw-bold">
            Tổng tiền: <span class="text-success">{{ formatPrice(totalAfterDiscount) }}</span>
          </h5>
        </div>

        <!-- Mã giảm giá -->
        <h6 class="mb-2">Mã giảm giá</h6>
        <div class="input-group mb-3">
          <input
            v-model="selectedCode"
            class="form-control"
            placeholder="Nhấn để chọn mã giảm giá"
            readonly
          />
          <button class="btn btn-outline-success" @click="showVoucherModal = true">Chọn mã</button>
        </div>

        <div v-if="appliedVoucher" class="alert alert-success py-2">
          🎉 Đã áp dụng mã <strong>{{ appliedVoucher.maCode }}</strong> –
          {{
            appliedVoucher.loaiKM === 'percent'
              ? `Giảm ${appliedVoucher.giaTri}%`
              : `Giảm ${formatPrice(appliedVoucher.giaTri)}`
          }}
          <button class="btn btn-sm btn-link text-danger ms-2" @click="huyVoucher">❌ Hủy</button>
        </div>

        <!-- Thông tin người nhận -->
        <h6 class="mb-2">Thông tin người nhận</h6>
        <div class="row g-3 mb-3">
          <div class="col-md-6">
            <input v-model="form.name" class="form-control" placeholder="Họ tên" />
          </div>
          <div class="col-md-6">
            <input v-model="form.phone" class="form-control" placeholder="SĐT" />
          </div>
          <div class="col-12">
            <input v-model="form.email" type="email" class="form-control" placeholder="Email" />
          </div>
          <div class="col-12">
            <input v-model="form.address" class="form-control" placeholder="Địa chỉ nhận hàng" />
          </div>
        </div>

        <!-- Phương thức thanh toán -->
        <h6 class="mb-2">Phương thức thanh toán</h6>
        <div class="form-check" v-for="method in paymentMethods" :key="method.value">
          <input
            class="form-check-input"
            type="radio"
            :id="method.value"
            v-model="form.payment"
            :value="method.value"
          />
          <label class="form-check-label" :for="method.value">{{ method.label }}</label>
        </div>

        <textarea
          v-model="form.note"
          class="form-control mt-3"
          rows="2"
          placeholder="Ghi chú (tuỳ chọn)"
        ></textarea>

        <!-- Nút đặt hàng -->
        <button class="btn btn-success w-100 mt-4" @click="datHang">
          <i class="bi bi-cart-check me-2"></i> Hoàn tất đặt hàng
        </button>
      </div>
    </div>

    <!-- Modal chọn mã -->
    <div
      v-if="showVoucherModal"
      class="modal fade show d-block"
      tabindex="-1"
      style="background-color: rgba(0, 0, 0, 0.5)"
    >
      <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title">Chọn mã giảm giá</h5>
            <button type="button" class="btn-close" @click="showVoucherModal = false"></button>
          </div>
          <div class="modal-body p-0">
            <ul class="list-group">
              <li
                v-for="voucher in filteredVouchers"
                :key="voucher.maKM"
                class="list-group-item list-group-item-action"
                @click="chonVoucher(voucher)"
                style="cursor: pointer"
              >
                <div class="fw-bold">{{ voucher.maCode }}</div>
                <div class="small">
                  {{
                    voucher.loaiKM === 'percent'
                      ? `Giảm ${voucher.giaTri}%`
                      : `Giảm ${formatPrice(voucher.giaTri)}`
                  }}
                  – Tối thiểu: {{ formatPrice(voucher.donHangToiThieu || 0) }} – HSD:
                  {{ new Date(voucher.ngayKetThuc).toLocaleDateString() }}
                </div>
              </li>
            </ul>
          </div>
          <div class="modal-footer">
            <button class="btn btn-secondary" @click="showVoucherModal = false">Đóng</button>
          </div>
        </div>
      </div>
    </div>

    <Footer />
  </div>
</template>

<script setup>
import Header from '../User/Header.vue'
import Navbar from '../User/Navbar.vue'
import Footer from '../User/Footer.vue'
import { ref, computed, onMounted } from 'vue'
import axios from 'axios'
import { useRouter } from 'vue-router'
import CartStore from './CartStore'

const router = useRouter()
const form = ref({ name: '', phone: '', address: '', email: '', payment: 'cod', note: '' })

const userInfo = ref(JSON.parse(localStorage.getItem('userInfo') || '{}'))
const storageKey = ref(userInfo.value?.maKH ? `cart_${userInfo.value.maKH}` : 'cart_temp')

const cartItems = ref([])
const allVouchers = ref([])
const selectedCode = ref('')
const appliedVoucher = ref(null)
const showVoucherModal = ref(false)

onMounted(async () => {
  cartItems.value = JSON.parse(localStorage.getItem('selectedItems')) || []

  const khachHangId = userInfo.value?.maKH
  if (khachHangId) {
    try {
      const res = await axios.get(`http://localhost:8080/api/khachhang/${khachHangId}`)
      const fullInfo = res.data
      form.value.name = fullInfo.hoTen
      form.value.phone = fullInfo.soDienThoai
      form.value.address = fullInfo.diaChi
      form.value.email = fullInfo.email
    } catch (err) {
      console.error('Lỗi lấy thông tin KH:', err)
    }
  }

  axios
    .get('http://localhost:8080/api/khuyen-mai')
    .then((res) => (allVouchers.value = res.data))
    .catch(() => (allVouchers.value = []))
})

const paymentMethods = [
  { value: 'cod', label: 'Thanh toán khi nhận hàng (COD)' },
  { value: 'momo', label: 'Thanh toán MoMo' },
  { value: 'vnpay', label: 'Thanh toán VNPay' },
]

const tongTienGoc = computed(() =>
  cartItems.value.reduce((sum, sp) => sum + sp.soLuong * (sp.giaBan || 0), 0),
)

const tienGiamGia = computed(() => {
  if (!appliedVoucher.value) return 0
  const v = appliedVoucher.value
  const giam = v.loaiKM === 'percent' ? (tongTienGoc.value * v.giaTri) / 100 : v.giaTri
  return v.giaTriToiDa ? Math.min(giam, v.giaTriToiDa) : giam
})

const totalAfterDiscount = computed(() => tongTienGoc.value - tienGiamGia.value)

const filteredVouchers = computed(() =>
  allVouchers.value.filter(
    (v) =>
      new Date(v.ngayBatDau) <= new Date() &&
      new Date(v.ngayKetThuc) >= new Date() &&
      v.soLuong - (v.daSuDung || 0) > 0 &&
      tongTienGoc.value >= (v.donHangToiThieu || 0),
  ),
)

function chonVoucher(voucher) {
  appliedVoucher.value = voucher
  selectedCode.value = voucher.maCode
  showVoucherModal.value = false
}

function huyVoucher() {
  appliedVoucher.value = null
  selectedCode.value = ''
}

function formatPrice(val) {
  return typeof val === 'number'
    ? val.toLocaleString('vi-VN', { style: 'currency', currency: 'VND' })
    : '0 ₫'
}

function getImageUrl(path) {
  if (!path) return 'https://cdn-icons-png.flaticon.com/512/891/891462.png'
  return path.startsWith('http')
    ? path
    : `http://localhost:8080/${path.startsWith('uploads/') ? path : 'uploads/' + path}`
}

function datHang() {
  if (!form.value.name || !form.value.phone || !form.value.address) {
    alert('❗ Vui lòng điền đầy đủ thông tin nhận hàng!')
    return
  }

  const dto = {
    maKH: userInfo.value?.maKH || null,
    maKM: appliedVoucher.value?.maKM || null,
    diaChiNhan: form.value.address,
    soDienThoaiNhan: form.value.phone,
    hoTenNguoiNhan: form.value.name,
    emailNguoiNhan: form.value.email,
    ghiChu: form.value.note,
    hinhThucThanhToan: form.value.payment,
    daThanhToan: form.value.payment !== 'cod',

    chiTiet: cartItems.value.map((sp) => ({
      maThuoc: sp.maThuoc || null,
      maBienThe: sp.maBienThe || null,
      soLuong: sp.soLuong,
      donGia: Number(sp.giaBan),
    })),
  }

  const tongTien = totalAfterDiscount.value

  // 👉 Nếu là COD → tạo đơn hàng trực tiếp
  if (form.value.payment === 'cod') {
    axios
      .post('http://localhost:8080/api/donhang/create', dto)
      .then((res) => {
        const maDonHang = res.data?.maDonHang

        // Xóa giỏ hàng
        const allCart = JSON.parse(localStorage.getItem(storageKey.value) || '[]')
        const remaining = allCart.filter(
          (item) =>
            !cartItems.value.some(
              (sp) =>
                sp.maThuoc === item.maThuoc && (sp.maBienThe || null) === (item.maBienThe || null),
            ),
        )
        localStorage.setItem(storageKey.value, JSON.stringify(remaining))
        localStorage.removeItem('selectedItems')
        CartStore.updateCount()

        alert('✅ Đặt hàng thành công (COD)!')
        if (!userInfo.value?.maKH) localStorage.setItem('guestOrderCode', maDonHang)
        router.push('/dat-hang-thanh-cong')
      })
      .catch((err) => {
        console.error('❌ Lỗi tạo đơn hàng COD:', err)
        alert('❌ Không thể tạo đơn hàng!')
      })

    return
  }

  // 👉 Nếu là VNPAY hoặc MOMO → lấy URL thanh toán
  const apiUrl =
    form.value.payment === 'vnpay'
      ? 'http://localhost:8080/api/thanh-toan/vnpay'
      : 'http://localhost:8080/api/thanh-toan/momo'

  // ❗ GỬI ĐÚNG DẠNG { donHangDTO: ..., tongTien: ... }
  axios
    .post(apiUrl, {
      donHangDTO: dto,
      tongTien: tongTien,
    })
    .then((res) => {
      const paymentUrl = form.value.payment === 'vnpay' ? res.data.paymentUrl : res.data.payUrl

      if (paymentUrl) {
        window.location.href = paymentUrl
      } else {
        alert('❌ Không thể tạo URL thanh toán!')
      }
    })
    .catch((err) => {
      console.error('❌ Lỗi tạo URL thanh toán:', err)
      alert('❌ Không thể khởi tạo thanh toán!')
    })
}
</script>

<style scoped>
.checkout-wrapper {
  max-width: 700px;
  margin: auto;
  padding: 40px 20px;
}
.product-img {
  width: 70px;
  height: 70px;
  object-fit: contain;
  border-radius: 6px;
}
.modal {
  z-index: 1050;
}
</style>
