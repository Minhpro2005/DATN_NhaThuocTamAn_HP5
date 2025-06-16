<template>
  <div v-if="loading" class="text-center my-5">
    <div class="spinner-border text-success" role="status">
      <span class="visually-hidden">Loading...</span>
    </div>
  </div>

  <div v-else class="card p-4 shadow-sm">
    <div class="row align-items-center">
      <div class="col-md-3 text-center mb-3 mb-md-0 position-relative">
        <!-- Bọc avatar bằng label để click -->
        <label style="cursor: pointer">
          <img
            :src="getFullImageUrl(user.hinhAnh)"
            alt="Avatar"
            class="rounded-circle img-thumbnail"
            style="width: 120px; height: 120px; object-fit: cover"
          />
          <input type="file" class="d-none" @change="handleFileChange" />
        </label>
        <small class="text-muted d-block mt-2" style="font-size: 12px">Click để đổi ảnh</small>
      </div>

      <div class="col-md-9">
        <h4 class="fw-bold mb-2">{{ user.hoTen }}</h4>
        <p class="mb-1"><i class="bi bi-envelope me-2"></i>{{ user.email }}</p>
        <p class="mb-1"><i class="bi bi-telephone me-2"></i>{{ user.soDienThoai }}</p>
        <p class="mb-1"><i class="bi bi-geo-alt me-2"></i>{{ user.diaChi }}</p>

        <button class="btn btn-outline-primary mt-3" @click="editing = true">
          <i class="bi bi-pencil-square me-1"></i> Chỉnh sửa thông tin
        </button>
      </div>
    </div>

    <!-- Form chỉnh sửa -->
    <div v-if="editing" class="overlay">
      <div class="edit-form card p-4 shadow">
        <h5 class="fw-bold mb-3">Chỉnh sửa thông tin</h5>
        <div class="row g-3">
          <div class="col-md-6">
            <input v-model="form.hoTen" type="text" class="form-control" placeholder="Họ và tên" />
          </div>
          <div class="col-md-6">
            <input v-model="form.email" type="email" class="form-control" placeholder="Email" />
          </div>
          <div class="col-md-6">
            <input
              v-model="form.soDienThoai"
              type="text"
              class="form-control"
              placeholder="Số điện thoại"
            />
          </div>
          <div class="col-md-6">
            <input v-model="form.diaChi" type="text" class="form-control" placeholder="Địa chỉ" />
          </div>
        </div>

        <div class="text-end mt-3">
          <button class="btn btn-secondary me-2" @click="editing = false">Hủy</button>
          <button class="btn btn-success" @click="saveInfo">Lưu</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'

// Cấu hình base URL backend (thay đổi theo server thực tế của bạn)
const serverUrl = 'http://localhost:8080'

// Avatar mặc định nếu chưa có ảnh
const defaultAvatar = 'vue-project\public\Img\avatamacdinh.png'

const user = ref({})
const form = ref({})
const editing = ref(false)
const loading = ref(true)

const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
const maKH = userInfo.maKH

onMounted(() => {
  loadUser()
})

function loadUser() {
  axios
    .get(`${serverUrl}/api/khachhang/${maKH}`)
    .then((res) => {
      user.value = res.data
      form.value = { ...res.data }
    })
    .catch((err) => {
      console.error('Lỗi tải thông tin cá nhân:', err)
      alert('Lỗi tải thông tin cá nhân')
    })
    .finally(() => (loading.value = false))
}

// Ghép URL đầy đủ cho ảnh
function getFullImageUrl(path) {
  return path ? `${serverUrl}${path}` : defaultAvatar
}

// Upload avatar
async function handleFileChange(e) {
  const selectedFile = e.target.files[0]
  if (!selectedFile) return

  const formData = new FormData()
  formData.append('file', selectedFile)

  try {
    const res = await axios.post(`${serverUrl}/api/khachhang/${maKH}/avatar`, formData, {
      headers: { 'Content-Type': 'multipart/form-data' },
    })
    user.value = res.data
    form.value = { ...res.data }
    alert('✅ Ảnh đại diện đã được cập nhật!')
  } catch (err) {
    console.error('Lỗi upload avatar:', err)
    alert('❌ Lỗi upload avatar')
  }
}

async function saveInfo() {
  try {
    const res = await axios.put(`${serverUrl}/api/khachhang/${maKH}`, form.value)
    user.value = res.data
    form.value = { ...res.data }
    editing.value = false
    alert('✅ Cập nhật thông tin thành công!')
  } catch (err) {
    console.error('Lỗi cập nhật:', err)
    alert('❌ Lỗi khi cập nhật thông tin!')
  }
}
</script>

<style scoped>
.overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 9999;
}
.edit-form {
  background: white;
  width: 100%;
  max-width: 600px;
  border-radius: 12px;
}
</style>
