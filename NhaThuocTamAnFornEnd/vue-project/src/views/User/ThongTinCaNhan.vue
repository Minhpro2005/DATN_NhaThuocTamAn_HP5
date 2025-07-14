<template>
  <div v-if="loading" class="text-center my-5">
    <div class="spinner-border text-success" role="status">
      <span class="visually-hidden">Loading...</span>
    </div>
  </div>

  <div v-else class="card p-4 shadow-sm">
    <div class="row align-items-center">
      <div class="col-md-3 text-center mb-3 mb-md-0 position-relative">
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
            <input v-model="form.soDienThoai" type="text" class="form-control" placeholder="SĐT" />
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
import { UserStore } from './UserStore'

const serverUrl = 'http://localhost:8080'
const defaultAvatar = 'https://cdn-icons-png.flaticon.com/512/149/149071.png'

const user = ref({})
const form = ref({})
const editing = ref(false)
const loading = ref(true)
const selectedFile = ref(null)

const maKH = UserStore.userInfo?.maKH || UserStore.userInfo?.id // ⚠️ fix chính

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
      alert('Lỗi tải thông tin cá nhân')
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
    UserStore.setUser(res.data)
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
