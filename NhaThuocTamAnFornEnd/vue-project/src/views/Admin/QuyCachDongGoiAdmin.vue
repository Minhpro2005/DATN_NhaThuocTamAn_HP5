<template>
  <div class="container mt-4">
    <h4 class="text-primary fw-bold mb-3">📦 Quản lý Quy cách đóng gói</h4>

    <div class="d-flex mb-3">
      <input v-model="moTaMoi" class="form-control me-2" placeholder="Nhập mô tả quy cách..." />
      <button class="btn btn-primary" @click="themMoi">➕ Thêm</button>
    </div>

    <table class="table table-bordered text-center">
      <thead class="table-primary">
        <tr>
          <th>Mã</th>
          <th>Quy cách</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="qc in danhSach" :key="qc.maQCDG">
          <td>{{ qc.maQCDG }}</td>
          <td>{{ qc.moTa }}</td>
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
const moTaMoi = ref('')
const toast = ref(null)

function fetchData() {
  fetch('http://localhost:8080/api/quycachdonggoi')
    .then((res) => res.json())
    .then((data) => (danhSach.value = data))
}

function themMoi() {
  const moTa = moTaMoi.value.trim()
  if (!moTa) return toast.value.show('❌ Không được để trống!', 'warning')

  fetch('http://localhost:8080/api/quycachdonggoi', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ moTa }),
  })
    .then((res) => {
      if (!res.ok) throw new Error('Quy cách đã tồn tại')
      return res.json()
    })
    .then(() => {
      fetchData()
      moTaMoi.value = ''
      toast.value.show('✅ Thêm thành công!', 'success')
    })
    .catch((err) => toast.value.show('❌ ' + err.message, 'error'))
}

onMounted(fetchData)
</script>
