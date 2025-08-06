<template>
  <div class="container mt-4">
    <h4 class="text-success fw-bold mb-3">📏 Quản lý Đơn vị tính</h4>

    <div class="d-flex mb-3">
      <input v-model="tenMoi" class="form-control me-2" placeholder="Nhập tên đơn vị tính..." />
      <button class="btn btn-success" @click="themMoi">➕ Thêm</button>
    </div>

    <table class="table table-bordered text-center">
      <thead class="table-success">
        <tr>
          <th>Mã</th>
          <th>Tên đơn vị</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="dvt in danhSach" :key="dvt.maDVT">
          <td>{{ dvt.maDVT }}</td>
          <td>{{ dvt.ten }}</td>
        </tr>
      </tbody>
    </table>

    <ToastMessage ref="toast" />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import ToastMessage from '../ToastMessage.vue'

const danhSach = ref([])
const tenMoi = ref('')
const toast = ref(null)

function fetchData() {
  fetch('http://localhost:8080/api/donvitinh')
    .then((res) => res.json())
    .then((data) => (danhSach.value = data))
}

function themMoi() {
  const ten = tenMoi.value.trim()
  if (!ten) return toast.value.show('❌ Không được để trống!', 'warning')

  fetch('http://localhost:8080/api/donvitinh', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ ten }),
  })
    .then((res) => {
      if (!res.ok) throw new Error('Đơn vị đã tồn tại')
      return res.json()
    })
    .then(() => {
      fetchData()
      tenMoi.value = ''
      toast.value.show('✅ Thêm thành công!', 'success')
    })
    .catch((err) => toast.value.show('❌ ' + err.message, 'error'))
}

onMounted(fetchData)
</script>
