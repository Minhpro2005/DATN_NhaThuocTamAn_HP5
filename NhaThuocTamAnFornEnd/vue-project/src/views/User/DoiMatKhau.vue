<template>
  <div class="card shadow-sm p-4">
    <h5 class="text-success mb-3">🔐 Đổi mật khẩu</h5>

    <form @submit.prevent="handleChangePassword">
      <div class="mb-3">
        <label for="oldPassword" class="form-label">Mật khẩu hiện tại</label>
        <input
          v-model="oldPassword"
          type="password"
          class="form-control"
          id="oldPassword"
          required
        />
      </div>

      <div class="mb-3">
        <label for="newPassword" class="form-label">Mật khẩu mới</label>
        <input
          v-model="newPassword"
          type="password"
          class="form-control"
          id="newPassword"
          required
        />
      </div>

      <div class="mb-3">
        <label for="confirmPassword" class="form-label">Xác nhận mật khẩu mới</label>
        <input
          v-model="confirmPassword"
          type="password"
          class="form-control"
          id="confirmPassword"
          required
        />
      </div>

      <div v-if="errorMessage" class="alert alert-danger py-2">{{ errorMessage }}</div>
      <div v-if="successMessage" class="alert alert-success py-2">{{ successMessage }}</div>

      <button type="submit" class="btn btn-success">
        <i class="bi bi-arrow-repeat me-1"></i> Cập nhật mật khẩu
      </button>
    </form>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import axios from 'axios'

const oldPassword = ref('')
const newPassword = ref('')
const confirmPassword = ref('')
const errorMessage = ref('')
const successMessage = ref('')

// Lấy userID (giả sử lưu trong localStorage sau khi đăng nhập)
const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
const userId = userInfo?.maKH // hoặc userID nếu dùng tài khoản hệ thống

async function handleChangePassword() {
  errorMessage.value = ''
  successMessage.value = ''

  if (!oldPassword.value || !newPassword.value || !confirmPassword.value) {
    errorMessage.value = 'Vui lòng nhập đầy đủ các trường.'
    return
  }

  if (newPassword.value.length < 6) {
    errorMessage.value = 'Mật khẩu mới phải có ít nhất 6 ký tự.'
    return
  }

  if (newPassword.value !== confirmPassword.value) {
    errorMessage.value = '⚠️ Mật khẩu xác nhận không khớp.'
    return
  }

  try {
    // Gọi API đổi mật khẩu thật (ví dụ)
    await axios.put(`http://localhost:8080/api/khachhang/${userId}/doi-mat-khau`, {
      oldPassword: oldPassword.value,
      newPassword: newPassword.value,
    })

    successMessage.value = '✅ Mật khẩu đã được cập nhật thành công.'
    oldPassword.value = ''
    newPassword.value = ''
    confirmPassword.value = ''
  } catch (err) {
    errorMessage.value = '❌ Mật khẩu hiện tại không đúng hoặc có lỗi xảy ra.'
  }
}
</script>
