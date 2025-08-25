<template>
  <div v-if="loading" class="text-center my-5">
    <div class="spinner-border text-success" role="status">
      <span class="visually-hidden">Loading...</span>
    </div>
  </div>

  <div v-else class="profile-card card p-4 shadow-lg border-0 rounded-4">
    <div class="row align-items-center">
      <!-- Avatar -->
      <div class="col-md-3 text-center mb-3 mb-md-0">
        <label class="avatar-wrapper">
          <img :src="getFullImageUrl(user.hinhAnh)" alt="Avatar" class="avatar-img" />
          <input type="file" class="d-none" @change="handleFileChange" />
        </label>
        <small class="text-muted d-block mt-2">📷 Click để đổi ảnh</small>
      </div>

      <!-- Info -->
      <div class="col-md-9">
        <h4 class="fw-bold text-success mb-2">{{ user.hoTen }}</h4>
        <p class="mb-1"><i class="bi bi-envelope text-success me-2"></i>{{ user.email }}</p>
        <p class="mb-1"><i class="bi bi-telephone text-success me-2"></i>{{ user.soDienThoai }}</p>
        <p class="mb-1"><i class="bi bi-geo-alt text-success me-2"></i>{{ user.diaChi }}</p>

        <button class="btn btn-outline-success mt-3 px-4" @click="editing = true">
          <i class="bi bi-pencil-square me-1"></i> Chỉnh sửa
        </button>
      </div>
    </div>

    <!-- Modal form -->
    <div v-if="editing" class="overlay">
      <div class="edit-form card p-4 shadow rounded-4">
        <h5 class="fw-bold mb-3 text-success">Cập nhật thông tin</h5>
        <div class="row g-3">
          <div class="col-md-6">
            <input v-model="form.hoTen" type="text" class="form-control" placeholder="Họ và tên" />
          </div>
          <div class="col-md-6">
            <input v-model="form.email" type="email" class="form-control" placeholder="Email" />
          </div>
          <div class="col-md-6">
            <input v-model="form.soDienThoai" type="text" class="form-control" placeholder="SĐT" />
          </div>
          <div class="col-md-6">
            <input v-model="form.diaChi" type="text" class="form-control" placeholder="Địa chỉ" />
          </div>
        </div>

        <div class="text-end mt-4">
          <button class="btn btn-outline-secondary me-2 px-4" @click="editing = false">Hủy</button>
          <button class="btn btn-success px-4" @click="saveInfo">Lưu</button>
        </div>
      </div>
    </div>

    <!-- Toast -->
    <ToastMessage ref="toastRef" />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'
import { UserStore } from './UserStore'
import ToastMessage from '../ToastMessage.vue'

const serverUrl = 'http://localhost:8080'
const defaultAvatar = 'https://cdn-icons-png.flaticon.com/512/149/149071.png'

const user = ref({})
const form = ref({})
const editing = ref(false)
const loading = ref(true)
const selectedFile = ref(null)
const toastRef = ref(null)

const maKH = UserStore.userInfo?.maKH || UserStore.userInfo?.id

onMounted(loadUser)

function loadUser() {
  axios
    .get(`${serverUrl}/api/khachhang/${maKH}`)
    .then((res) => {
      user.value = res.data
      form.value = { ...res.data }
    })
    .catch((err) => {
      console.error('Lỗi tải thông tin:', err)
      showToast('❌ Lỗi tải thông tin cá nhân', 'danger')
    })
    .finally(() => (loading.value = false))
}

function getFullImageUrl(path) {
  return path ? `${serverUrl}${path}` : defaultAvatar
}

function handleFileChange(e) {
  selectedFile.value = e.target.files[0]
  if (selectedFile.value) {
    uploadAvatar()
  }
}

async function uploadAvatar() {
  try {
    const formData = new FormData()
    formData.append('file', selectedFile.value)

    const res = await axios.post(`${serverUrl}/api/khachhang/${maKH}/avatar`, formData, {
      headers: { 'Content-Type': 'multipart/form-data' },
    })

    user.value = res.data
    form.value = { ...res.data }
    UserStore.setUser(res.data)
    showToast('✅ Ảnh đại diện đã được cập nhật!', 'success')
  } catch (err) {
    console.error('Lỗi upload avatar:', err)
    showToast('❌ Lỗi upload avatar', 'danger')
  }
}

async function saveInfo() {
  try {
    const res = await axios.put(`${serverUrl}/api/khachhang/${maKH}`, form.value)
    user.value = res.data
    form.value = { ...res.data }
    UserStore.setUser(res.data)
    editing.value = false
    showToast(' Cập nhật thông tin thành công!', 'success')
  } catch (err) {
    console.error('Lỗi cập nhật:', err)
    showToast('❌ Lỗi khi cập nhật thông tin!', 'danger')
  }
}

function showToast(msg, type = 'success') {
  toastRef.value?.show(msg, type)
}
</script>

<style scoped>
.profile-card {
  border-radius: 16px;
  background: #fff;
}

.avatar-wrapper {
  cursor: pointer;
  display: inline-block;
  transition: transform 0.2s;
}
.avatar-wrapper:hover {
  transform: scale(1.05);
}

.avatar-img {
  width: 120px;
  height: 120px;
  border-radius: 50%;
  border: 3px solid #2e7d32;
  object-fit: cover;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
}

.overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background: rgba(0, 0, 0, 0.45);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1050;
  backdrop-filter: blur(2px);
}

.edit-form {
  background: #fff;
  width: 100%;
  max-width: 600px;
  border-radius: 16px;
  animation: fadeIn 0.3s ease-in-out;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(-15px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}
</style>
