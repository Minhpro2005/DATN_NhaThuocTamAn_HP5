<template>
  <div class="card p-4 shadow-sm">
    <h5 class="fw-bold mb-4 text-success"><i class="bi bi-box-seam me-2"></i> Đơn hàng của bạn</h5>

    <!-- Tabs trạng thái -->
    <ul class="nav nav-pills mb-4 gap-2">
      <li class="nav-item" v-for="status in tabs" :key="status.value">
        <button
          class="nav-link px-3 py-2"
          :class="{ active: currentStatus === status.value }"
          @click="currentStatus = status.value"
        >
          {{ status.label }}
        </button>
      </li>
    </ul>

    <div v-if="filteredOrders.length === 0" class="alert alert-light text-center shadow-sm">
      <i class="bi bi-inbox me-2 text-secondary"></i>
      Không có đơn hàng nào thuộc trạng thái
      <strong>{{ getTrangThaiLabel(currentStatus) }}</strong>
    </div>

    <!-- Danh sách đơn -->
    <div
      v-for="order in filteredOrders"
      :key="order.id"
      class="order-item border rounded p-3 mb-4 shadow-sm"
    >
      <!-- Thông tin chung -->
      <div class="d-flex justify-content-between align-items-center mb-2">
        <div>
          <strong>Mã đơn hàng: #{{ order.id }}</strong
          ><br />
          <small class="text-muted">Ngày đặt: {{ formatDate(order.date) }}</small>
        </div>
        <span :class="getTrangThaiClass(order.status)">
          {{ getTrangThaiLabel(order.status) }}
        </span>
      </div>

      <!-- Sản phẩm -->
      <div
        v-for="(sp, index) in visibleProducts(order)"
        :key="index"
        class="d-flex align-items-center gap-3 mb-2"
      >
        <img
          :src="sp.hinhAnh"
          class="rounded shadow-sm"
          style="width: 80px; height: 80px; object-fit: cover"
        />
        <div class="flex-grow-1">
          <div class="fw-semibold">{{ sp.tenThuoc }}</div>
          <div class="text-muted small">Số lượng: {{ sp.soLuong }}</div>
        </div>
        <div class="fw-bold text-success">{{ formatPrice(sp.donGia * sp.soLuong) }}</div>

        <!-- Nút đánh giá -->
        <div v-if="order.status === 3 && sp.hienDanhGia" class="ms-2">
          <button
            v-if="sp.maDHCT && !sp.danhGiaXong"
            class="btn btn-outline-success btn-sm"
            @click="reviewProduct(sp)"
          >
            <i class="bi bi-star me-1"></i> Đánh giá
          </button>
          <span v-else-if="sp.danhGiaXong" class="badge bg-success">Đã đánh giá</span>
        </div>
      </div>

      <!-- Nút xem thêm / thu gọn -->
      <div v-if="order.products.length > 1" class="text-center mt-2">
        <button
          v-if="!expandedOrders.includes(order.id)"
          class="btn btn-outline-success btn-sm d-inline-flex align-items-center"
          @click="expandedOrders.push(order.id)"
        >
          <i class="bi bi-chevron-down me-1"></i>
          Xem thêm {{ order.products.length - 1 }} sản phẩm
        </button>
        <button
          v-else
          class="btn btn-outline-danger btn-sm d-inline-flex align-items-center"
          @click="expandedOrders = expandedOrders.filter((id) => id !== order.id)"
        >
          <i class="bi bi-chevron-up me-1"></i>
          Thu gọn
        </button>
      </div>

      <!-- Tổng tiền -->
      <div class="text-end mt-2 fw-bold">
        Tổng đơn: <span class="text-success">{{ formatPrice(order.total) }}</span>
      </div>

      <!-- Nút thao tác -->
      <!-- Nút thao tác -->
      <div class="text-end mt-3 d-flex gap-2 justify-content-end">
        <button class="btn btn-outline-info btn-sm" @click="openDetail(order.id)">
          <i class="bi bi-receipt me-1"></i> Xem chi tiết
        </button>
        <button
          v-if="order.status === 0"
          class="btn btn-outline-danger btn-sm"
          @click="cancelOrder(order)"
        >
          <i class="bi bi-x-circle me-1"></i> Hủy đơn
        </button>
        <button v-if="order.status === 3" class="btn btn-danger btn-sm" @click="muaLai(order)">
          <i class="bi bi-cart-plus me-1"></i> Mua lại
        </button>
      </div>
    </div>

    <!-- Modal chi tiết -->
    <XemChiTietDonHang
      v-if="showDetailModal"
      :donHang="selectedOrder"
      @close="showDetailModal = false"
    />

    <!-- Toast -->
    <ToastMessage ref="toastRef" />

    <!-- Loader toàn màn hình -->
    <div v-if="loading" class="fullscreen-loader d-flex justify-content-center align-items-center">
      <div class="spinner-border text-success" role="status" style="width: 3rem; height: 3rem">
        <span class="visually-hidden">Loading...</span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'
import XemChiTietDonHang from './XemChiTietDonHang.vue'

const router = useRouter()

const orders = ref([])
const loading = ref(false)
const currentStatus = ref(0)
const showDetailModal = ref(false)
const selectedOrder = ref(null)
const expandedOrders = ref([]) // đơn đã bấm "xem thêm"
const maKH = localStorage.getItem('maKH')

const tabs = [
  { value: 0, label: 'Chờ xác nhận' },
  { value: 1, label: 'Đã xác nhận' },
  { value: 2, label: 'Đang giao' },
  { value: 3, label: 'Hoàn tất' },
  { value: 4, label: 'Đã huỷ' },
]

const formatPrice = (price) =>
  price?.toLocaleString('vi-VN', { style: 'currency', currency: 'VND' })

const formatDate = (date) =>
  new Date(date).toLocaleDateString('vi-VN', {
    day: '2-digit',
    month: '2-digit',
    year: 'numeric',
  })

const getTrangThaiLabel = (val) => {
  switch (val) {
    case 0:
      return 'Chờ xác nhận'
    case 1:
      return 'Đã xác nhận'
    case 2:
      return 'Đang giao'
    case 3:
      return 'Hoàn tất'
    case 4:
      return 'Đã huỷ'
    default:
      return 'Không rõ'
  }
}

const getTrangThaiClass = (val) => {
  switch (val) {
    case 0:
      return 'badge bg-secondary'
    case 1:
      return 'badge bg-primary'
    case 2:
      return 'badge bg-warning text-dark'
    case 3:
      return 'badge bg-success'
    case 4:
      return 'badge bg-dark'
    default:
      return 'badge bg-light text-dark'
  }
}

const getFullImageUrl = (path) => {
  if (!path) return 'https://cdn-icons-png.flaticon.com/512/891/891462.png'
  if (path.startsWith('http')) return path
  const fixedPath = path.startsWith('uploads/') ? path : `uploads/${path}`
  return `http://localhost:8080/${fixedPath}`
}

const filteredOrders = computed(() =>
  orders.value
    .filter((order) => order.status === currentStatus.value)
    .sort((a, b) => new Date(b.date) - new Date(a.date)),
)

const visibleProducts = (order) => {
  if (expandedOrders.value.includes(order.id)) return order.products
  return order.products.slice(0, 1)
}

// ✅ Check đã đánh giá chưa
const checkDaDanhGia = async (maKH, maDHCT) => {
  if (!maKH || !maDHCT) return false
  try {
    const res = await axios.get(`http://localhost:8080/api/danh-gia/da-danh-gia-dhct`, {
      params: { maKH, maDHCT },
    })
    return res.data === true
  } catch (err) {
    console.error('❌ Lỗi check đánh giá:', err)
    return false
  }
}

//  Check tồn kho
const checkTonKho = async (maThuoc, maBienThe) => {
  try {
    const res = await axios.get(`http://localhost:8080/api/kho-hang/so-luong-ton`, {
      params: { maThuoc, maBienThe },
    })
    return res.data > 0 // ✅ vì backend trả về số nguyên trực tiếp
  } catch (err) {
    console.error('❌ Lỗi kiểm tra tồn kho:', err)
    return false
  }
}

// 🛒 Mua lại
const muaLai = async (order) => {
  try {
    const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
    const maKH = userInfo?.maKH || null
    const storageKey = maKH ? `cart_${maKH}` : 'cart_temp'

    let cart = JSON.parse(localStorage.getItem(storageKey)) || []
    let soLuongThem = 0

    for (const sp of order.products) {
      const conHang = await checkTonKho(sp.maThuoc || null, sp.maBienThe || null)
      if (!conHang) {
        console.warn(`⚠️ ${sp.tenThuoc} hết hàng, bỏ qua.`)
        continue
      }

      // kiểm tra xem sp đã có trong giỏ chưa
      const index = cart.findIndex(
        (item) => item.maThuoc === sp.maThuoc && item.maBienThe === sp.maBienThe,
      )

      if (index !== -1) {
        cart[index].soLuong += sp.soLuong
      } else {
        cart.push({
          maThuoc: sp.maThuoc,
          maBienThe: sp.maBienThe,
          tenThuoc: sp.tenThuoc,
          soLuong: sp.soLuong,
          giaBan: sp.donGia,
          hinhAnh: sp.hinhAnh,
          donViTinh: sp.donViTinh || 'Hộp',
        })
      }
      soLuongThem++
    }

    // cập nhật localStorage
    localStorage.setItem(storageKey, JSON.stringify(cart))

    if (soLuongThem > 0) {
      alert(`🛒 Đã thêm ${soLuongThem} sản phẩm vào giỏ hàng!`)
      router.push('/gio-hang')
    } else {
      alert('⚠️ Tất cả sản phẩm trong đơn đã hết hàng, không thể mua lại.')
    }
  } catch (err) {
    console.error('❌ Lỗi mua lại:', err)
    alert('⚠️ Không thể mua lại đơn hàng này.')
  }
}

const reviewProduct = (sp) => {
  const maThuoc = sp.maThuoc || null
  const maBienThe = sp.maBienThe || null
  const maDHCT = sp.maDHCT || null
  if (!maDHCT) {
    alert('Không tìm thấy mã chi tiết đơn hàng để đánh giá!')
    return
  }
  router.push({
    name: 'chi-tiet-san-pham',
    params: { id: maThuoc || maBienThe },
    query: { review: 'true', loai: maBienThe ? 'bienThe' : 'thuoc', maDHCT },
  })
}

const cancelOrder = async (order) => {
  if (order.status !== 0) {
    alert("❌ Chỉ đơn 'Chờ xác nhận' mới được hủy!")
    return
  }
  const lyDo = prompt(`📌 Nhập lý do hủy đơn #${order.id}:`)
  if (!lyDo) return
  loading.value = true
  try {
    await axios.put(
      `http://localhost:8080/api/donhang/update-status?id=${order.id}&status=4&lyDo=${encodeURIComponent(lyDo)}`,
    )
    order.status = 4
    alert('✅ Hủy đơn thành công!')
  } catch (err) {
    console.error('❌ Lỗi khi hủy đơn:', err)
    alert('⚠️ Hủy đơn thất bại.')
  } finally {
    loading.value = false
  }
}

const fetchOrders = async () => {
  if (!maKH) return
  loading.value = true
  try {
    const res = await axios.get(`http://localhost:8080/api/donhang/khach-hang/${maKH}`)
    orders.value = await Promise.all(
      res.data.map(async (dh) => {
        let products = await Promise.all(
          (dh.chiTietHienThi || []).map(async (sp) => {
            const daDanhGia = sp.maDHCT ? await checkDaDanhGia(maKH, sp.maDHCT) : false
            return {
              maThuoc: sp.maThuoc,
              maBienThe: sp.maBienThe,
              maDHCT: sp.maDHCT,
              tenThuoc: sp.tenThuoc,
              soLuong: sp.soLuong,
              donGia: sp.donGia,
              hinhAnh: getFullImageUrl(sp.hinhAnh || sp.hinhAnhChinh),
              danhGiaXong: daDanhGia,
              hienDanhGia: true,
            }
          }),
        )
        return {
          id: dh.maDonHang,
          date: new Date(dh.ngayDat),
          status: dh.trangThai,
          total: dh.thanhToan,
          products,
        }
      }),
    )
  } catch (err) {
    console.error('❌ Lỗi khi lấy đơn hàng:', err)
  } finally {
    loading.value = false
  }
}

const openDetail = async (orderId) => {
  loading.value = true
  try {
    const res = await axios.get(`http://localhost:8080/api/donhang/dto/${orderId}`)
    const donHang = res.data
    donHang.chiTiet = (donHang.chiTietHienThi || []).map((ct) => ({
      ...ct,
      hinhAnh: getFullImageUrl(ct.hinhAnh || ct.hinhAnhChinh),
      tenThuoc: ct.tenThuoc || '---',
    }))
    selectedOrder.value = donHang
    showDetailModal.value = true
  } catch (err) {
    console.error('❌ Lỗi khi lấy chi tiết:', err)
    alert('Lỗi lấy chi tiết đơn hàng')
  } finally {
    loading.value = false
  }
}

let refreshInterval = null
onMounted(async () => {
  await fetchOrders()
  refreshInterval = setInterval(fetchOrders, 15000)
})
onUnmounted(() => {
  if (refreshInterval) clearInterval(refreshInterval)
})
</script>

<style scoped>
.badge {
  font-size: 0.9rem;
  padding: 6px 12px;
}
.nav-pills .nav-link {
  border-radius: 20px;
  font-weight: 500;
  color: #555;
}
.nav-pills .nav-link.active {
  background-color: #2e7d32;
}
.order-item {
  transition: all 0.3s ease;
  border: 1px solid #e0e0e0;
  border-radius: 10px;
}

.order-item:hover {
  transform: translateY(-4px); /* chỉ nổi lên nhẹ, không phóng to */
  box-shadow:
    0 10px 25px rgba(0, 0, 0, 0.25),
    0 0 18px rgba(46, 125, 50, 0.7); /* bóng đậm + glow xanh lá */
  border-color: #2e7d32; /* đổi viền xanh lá */
  background: #f9fff9; /* nền xanh nhạt để nhìn rõ hơn */
}
.fullscreen-loader {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background: rgba(255, 255, 255, 0.7); /* nền mờ */
  z-index: 2000; /* cao hơn modal */
}
</style>
