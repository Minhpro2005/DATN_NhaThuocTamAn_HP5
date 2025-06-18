<template>
  <div class="container py-4">
    <h4 class="mb-4 text-success">Quản lý nhà cung cấp</h4>

    <!-- Nút thêm mới -->
    <div class="mb-3">
      <button class="btn btn-success" @click="openModal()">➕ Thêm nhà cung cấp</button>
    </div>

    <!-- Bảng danh sách -->
    <div class="card shadow-sm">
      <div class="card-body p-0">
        <table class="table table-bordered table-hover mb-0">
          <thead class="table-success text-center">
            <tr>
              <th>Mã NCC</th>
              <th>Tên nhà cung cấp</th>
              <th>Địa chỉ</th>
              <th>SĐT</th>
              <th>Email</th>
              <th>Hành động</th>
            </tr>
          </thead>
          <tbody class="text-center align-middle">
            <tr v-for="ncc in danhSachNCC" :key="ncc.maNCC">
              <td>{{ ncc.maNCC }}</td>
              <td>{{ ncc.tenNCC }}</td>
              <td>{{ ncc.diaChi }}</td>
              <td>{{ ncc.soDienThoai }}</td>
              <td>{{ ncc.email }}</td>
              <td>
                <button class="btn btn-sm btn-warning me-2" @click="openModal(ncc)">Sửa</button>
                <button class="btn btn-sm btn-danger" @click="xoaNCC(ncc.maNCC)">Xóa</button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>

    <!-- Modal thêm/sửa -->
    <div
      class="modal fade"
      :class="{ show: showModal }"
      :style="showModal ? 'display:block' : ''"
      @click.self="closeModal"
    >
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title">{{ form.maNCC ? 'Sửa' : 'Thêm' }} nhà cung cấp</h5>
            <button class="btn-close" @click="closeModal"></button>
          </div>
          <div class="modal-body">
            <input v-model="form.tenNCC" class="form-control mb-2" placeholder="Tên nhà cung cấp" />
            <input v-model="form.diaChi" class="form-control mb-2" placeholder="Địa chỉ" />
            <input
              v-model="form.soDienThoai"
              class="form-control mb-2"
              placeholder="Số điện thoại"
            />
            <input v-model="form.email" class="form-control mb-2" placeholder="Email" />
          </div>
          <div class="modal-footer">
            <button class="btn btn-primary" @click="luuNCC">Lưu</button>
            <button class="btn btn-secondary" @click="closeModal">Hủy</button>
          </div>
        </div>
      </div>
    </div>
    <div v-if="showModal" class="modal-backdrop fade show"></div>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const danhSachNCC = ref([
  {
    maNCC: 1,
    tenNCC: 'Dược phẩm A',
    diaChi: 'Hà Nội',
    soDienThoai: '0123456789',
    email: 'a@example.com',
  },
  {
    maNCC: 2,
    tenNCC: 'Dược phẩm B',
    diaChi: 'TP.HCM',
    soDienThoai: '0987654321',
    email: 'b@example.com',
  },
])

const showModal = ref(false)
const form = ref({})

function openModal(ncc = null) {
  form.value = ncc
    ? { ...ncc }
    : { maNCC: null, tenNCC: '', diaChi: '', soDienThoai: '', email: '' }
  showModal.value = true
}

function closeModal() {
  showModal.value = false
}

function luuNCC() {
  if (form.value.maNCC) {
    const index = danhSachNCC.value.findIndex((n) => n.maNCC === form.value.maNCC)
    danhSachNCC.value[index] = { ...form.value }
  } else {
    const newID = Math.max(...danhSachNCC.value.map((n) => n.maNCC), 0) + 1
    danhSachNCC.value.push({ ...form.value, maNCC: newID })
  }
  closeModal()
}

function xoaNCC(id) {
  if (confirm('Bạn có chắc chắn muốn xóa nhà cung cấp này?')) {
    danhSachNCC.value = danhSachNCC.value.filter((n) => n.maNCC !== id)
  }
}
</script>

<style scoped>
.table td,
.table th {
  vertical-align: middle;
}
</style>
