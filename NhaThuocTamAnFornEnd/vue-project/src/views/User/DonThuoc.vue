<template>
  <div class="container py-4">
    <h4 class="text-success mb-3">💊 Gửi Đơn Thuốc</h4>

    <!-- Gửi đơn thuốc mới -->
    <div class="card shadow-sm">
      <div class="card-body">
        <h6 class="mb-3">📤 Gửi đơn thuốc mới</h6>

        <div class="mb-3">
          <label class="form-label">Họ tên</label>
          <input v-model="tenKhach" class="form-control" placeholder="Nhập họ tên của bạn" />
        </div>

        <div class="mb-3">
          <label class="form-label">Số điện thoại</label>
          <input v-model="sdtKhach" class="form-control" placeholder="Nhập SĐT để liên hệ" />
        </div>

        <div class="mb-3">
          <label class="form-label">Ảnh đơn thuốc</label>
          <input type="file" class="form-control" @change="chonFile" />
        </div>

        <div class="mb-3">
          <label class="form-label">Mô tả / Ghi chú</label>
          <textarea v-model="moTa" class="form-control"></textarea>
        </div>

        <div class="text-end">
          <button class="btn btn-success" @click="guiDonThuoc">📨 Gửi đơn thuốc</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const tenKhach = ref('')
const sdtKhach = ref('')
const moTa = ref('')
const fileAnh = ref(null)

const chonFile = (e) => {
  const file = e.target.files[0]
  if (file) {
    fileAnh.value = file
  }
}

const guiDonThuoc = () => {
  if (!tenKhach.value || !sdtKhach.value || !fileAnh.value) {
    alert('Vui lòng nhập đầy đủ họ tên, SĐT và chọn ảnh đơn thuốc!')
    return
  }

  alert('📬 Đã gửi đơn thuốc thành công!')

  tenKhach.value = ''
  sdtKhach.value = ''
  moTa.value = ''
  fileAnh.value = null
}

const huyDon = () => {
  if (confirm('Bạn có chắc chắn muốn hủy đơn đang soạn?')) {
    tenKhach.value = ''
    sdtKhach.value = ''
    moTa.value = ''
    fileAnh.value = null
  }
}
</script>

<style scoped>
img {
  object-fit: contain;
}
</style>
