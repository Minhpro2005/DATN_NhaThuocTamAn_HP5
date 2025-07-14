<template>
  <div class="container my-5">
    <h4 class="text-center text-primary mb-4">🛒 TẠO ĐƠN HÀNG TẠI QUẦY</h4>

    <!-- Thông tin khách hàng -->
    <div class="card p-4 mb-4 shadow-sm">
      <h6 class="fw-bold mb-3">Thông tin người nhận</h6>
      <div class="row g-3">
        <div class="col-md-4">
          <input v-model="form.name" class="form-control" placeholder="Họ tên khách hàng" />
        </div>
        <div class="col-md-4">
          <input v-model="form.phone" class="form-control" placeholder="Số điện thoại" />
        </div>
        <div class="col-md-4">
          <input v-model="form.address" class="form-control" placeholder="Địa chỉ nhận hàng" />
        </div>
      </div>
    </div>

    <!-- Danh sách sản phẩm -->
    <div class="card p-4 mb-4 shadow-sm">
      <h6 class="fw-bold mb-3">Chọn sản phẩm</h6>
      <div class="mb-3">
        <input v-model="keyword" class="form-control" placeholder="🔍 Tìm sản phẩm..." />
      </div>

      <div class="table-responsive" style="max-height: 300px; overflow-y: auto">
        <table class="table table-bordered align-middle text-center">
          <thead class="table-light">
            <tr>
              <th>Tên thuốc</th>
              <th>Giá bán</th>
              <th>SL</th>
              <th>Thêm</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="sp in filteredProducts" :key="sp.maThuoc">
              <td>{{ sp.tenThuoc }}</td>
              <td>{{ formatPrice(sp.giaBan) }}</td>
              <td>
                <input type="number" v-model.number="sp.soLuong" min="1" class="form-control" />
              </td>
              <td>
                <button class="btn btn-success btn-sm" @click="themVaoDon(sp)">+</button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>

    <!-- Danh sách đã chọn -->
    <div v-if="cart.length" class="card p-4 mb-4 shadow-sm">
      <h6 class="fw-bold mb-3">Sản phẩm đã chọn</h6>
      <ul class="list-group">
        <li
          class="list-group-item d-flex justify-content-between"
          v-for="item in cart"
          :key="item.maThuoc"
        >
          <span>{{ item.tenThuoc }} × {{ item.soLuong }}</span>
          <span>{{ formatPrice(item.giaBan * item.soLuong) }}</span>
        </li>
      </ul>
      <div class="text-end mt-3 fw-bold">Tổng tiền: {{ formatPrice(tongTien) }}</div>
    </div>

    <!-- Ghi chú và thanh toán -->
    <div class="card p-4 shadow-sm">
      <textarea
        v-model="form.note"
        class="form-control mb-3"
        rows="2"
        placeholder="Ghi chú đơn hàng..."
      ></textarea>

      <div class="form-check mb-3">
        <input type="checkbox" class="form-check-input" id="paid" v-model="form.daThanhToan" />
        <label class="form-check-label" for="paid">Khách đã thanh toán</label>
      </div>

      <button class="btn btn-primary w-100" @click="taoDon">Tạo đơn & In hoá đơn</button>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import axios from 'axios'

const form = ref({
  name: '',
  phone: '',
  address: '',
  note: '',
  daThanhToan: false,
})

const products = ref([])
const keyword = ref('')
const cart = ref([])

onMounted(() => {
  axios.get('http://localhost:8080/api/thuoc').then((res) => {
    products.value = res.data.map((p) => ({ ...p, soLuong: 1 }))
  })
})

const filteredProducts = computed(() =>
  products.value.filter((p) => p.tenThuoc.toLowerCase().includes(keyword.value.toLowerCase())),
)

const tongTien = computed(() =>
  cart.value.reduce((sum, item) => sum + item.giaBan * item.soLuong, 0),
)

function themVaoDon(sp) {
  const existed = cart.value.find((i) => i.maThuoc === sp.maThuoc)
  if (existed) {
    existed.soLuong += sp.soLuong
  } else {
    cart.value.push({ ...sp })
  }
  sp.soLuong = 1
}

function formatPrice(val) {
  return val?.toLocaleString('vi-VN', { style: 'currency', currency: 'VND' }) || '0 ₫'
}

function taoDon() {
  if (!form.value.name || !form.value.phone || !form.value.address) {
    alert('❗ Vui lòng nhập đầy đủ thông tin khách hàng.')
    return
  }
  if (!cart.value.length) {
    alert('❗ Vui lòng chọn ít nhất 1 sản phẩm.')
    return
  }

  const dto = {
    maKH: null,
    hoTenNguoiNhan: form.value.name,
    soDienThoaiNhan: form.value.phone,
    diaChiNhan: form.value.address,
    ghiChu: form.value.note,
    hinhThucThanhToan: form.value.daThanhToan ? 'cod' : 'cod',
    daThanhToan: form.value.daThanhToan,
    chiTiet: cart.value.map((item) => ({
      maThuoc: item.maThuoc,
      soLuong: item.soLuong,
      donGia: item.giaBan,
    })),
  }

  axios
    .post('http://localhost:8080/api/donhang/create', dto)
    .then((res) => {
      alert('✅ Đặt hàng thành công!')
      window.open(`/in-phieu/${res.data.maDonHang}`, '_blank')
      cart.value = []
    })
    .catch((err) => {
      console.error('❌ Lỗi tạo đơn:', err)
      alert('❌ Không thể tạo đơn hàng!')
    })
}
</script>

<style scoped>
input[type='number'] {
  width: 80px;
  margin: auto;
}
</style>
