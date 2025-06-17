<template>
  <div class="register-container d-flex justify-content-center align-items-center min-vh-100">
    <div class="card p-4 shadow-lg w-100" style="max-width: 500px">
      <h4 class="text-center mb-4 text-success">📝 Đăng ký tài khoản</h4>

      <form @submit.prevent="handleRegister" novalidate>
        <div class="mb-3">
          <label class="form-label">Họ tên</label>
          <input
            v-model="hoTen"
            type="text"
            class="form-control"
            placeholder="Nhập họ tên..."
            required
          />
        </div>

        <div class="mb-3">
          <label class="form-label">Email</label>
          <input
            v-model="email"
            type="email"
            class="form-control"
            placeholder="Nhập email..."
            required
          />
        </div>

        <div class="mb-3">
          <label class="form-label">Mật khẩu</label>
          <div class="input-group">
            <input
              :type="showPassword ? 'text' : 'password'"
              v-model="password"
              class="form-control"
              placeholder="Tối thiểu 6 ký tự"
              required
            />
            <button
              class="btn btn-outline-secondary"
              type="button"
              @click="showPassword = !showPassword"
            >
              <i :class="showPassword ? 'bi bi-eye-slash' : 'bi bi-eye'"></i>
            </button>
          </div>
        </div>

        <div v-if="error" class="alert alert-danger text-center py-2">{{ error }}</div>

        <button type="submit" class="btn btn-success w-100">Đăng ký</button>
      </form>

      <p class="text-center text-muted mt-3" style="font-size: 0.9rem">
        Đã có tài khoản?
        <router-link to="/dang-nhap" class="text-decoration-none">Đăng nhập</router-link>
      </p>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import axios from 'axios'
import { useRouter } from 'vue-router'

const hoTen = ref('')
const email = ref('')
const password = ref('')
const showPassword = ref(false)
const error = ref('')
const router = useRouter()

function handleRegister() {
  error.value = ''

  if (!hoTen.value || !email.value || !password.value) {
    error.value = 'Vui lòng điền đầy đủ thông tin!'
    return
  }

  if (password.value.length < 6) {
    error.value = 'Mật khẩu phải có ít nhất 6 ký tự!'
    return
  }

  const khachHang = {
    hoTen: hoTen.value,
    email: email.value,
    matKhau: password.value,
  }

  axios
    .post('http://localhost:8080/api/auth/dang-ky', khachHang)
    .then(() => {
      alert('🎉 Đăng ký thành công! Mã xác thực đã được gửi đến email.')
      router.push(`/xac-thuc-email?email=${encodeURIComponent(email.value)}`)
    })
    .catch((err) => {
      error.value = err.response?.data?.message || 'Đăng ký thất bại!'
    })
}
</script>

<style scoped>
.register-container {
  background: linear-gradient(to right, #e3f2fd, #e8f5e9);
  padding: 40px 0;
}
.card {
  border-radius: 16px;
  background-color: #fff;
}
input.form-control:focus {
  border-color: #2e7d32;
  box-shadow: 0 0 0 0.2rem rgba(46, 125, 50, 0.25);
}
</style>
