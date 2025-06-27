<template>
  <div class="register-container d-flex justify-content-center align-items-center min-vh-100">
    <div class="card p-4 shadow-lg w-100" style="max-width: 500px">
      <div class="text-center mb-4">
        <i class="bi bi-person-plus-fill text-success" style="font-size: 2.5rem"></i>
        <h4 class="mt-2 text-success fw-bold">Đăng ký tài khoản</h4>
      </div>

      <form @submit.prevent="handleRegister" novalidate>
        <!-- Họ tên -->
        <div class="mb-3">
          <label class="form-label d-flex align-items-center">
            <i class="bi bi-person-fill text-primary me-2"></i> Họ tên
          </label>
          <input
            v-model="hoTen"
            type="text"
            class="form-control rounded-pill"
            placeholder="Nhập họ tên..."
          />
        </div>

        <!-- Email -->
        <div class="mb-3">
          <label class="form-label d-flex align-items-center">
            <i class="bi bi-envelope-fill text-danger me-2"></i> Email
          </label>
          <input
            v-model="email"
            type="email"
            class="form-control rounded-pill"
            placeholder="Nhập email..."
          />
        </div>

        <!-- Mật khẩu -->
        <div class="mb-3">
          <label class="form-label d-flex align-items-center">
            <i class="bi bi-lock-fill text-warning me-2"></i> Mật khẩu
          </label>
          <div class="input-group">
            <input
              :type="showPassword ? 'text' : 'password'"
              v-model="password"
              class="form-control rounded-start-pill border-end-0"
              placeholder="Tối thiểu 6 ký tự"
            />
            <span
              class="input-group-text bg-white border-start-0 rounded-end-pill"
              style="cursor: pointer"
              @click="showPassword = !showPassword"
            >
              <i :class="showPassword ? 'bi bi-eye-slash' : 'bi bi-eye'"></i>
            </span>
          </div>
        </div>

        <!-- Xác nhận mật khẩu -->
        <div class="mb-3">
          <label class="form-label d-flex align-items-center">
            <i class="bi bi-shield-lock-fill text-info me-2"></i> Xác nhận mật khẩu
          </label>
          <input
            :type="showPassword ? 'text' : 'password'"
            v-model="confirmPassword"
            class="form-control rounded-pill"
            placeholder="Nhập lại mật khẩu"
          />
        </div>

        <!-- Thông báo lỗi -->
        <div v-if="error" class="alert alert-danger d-flex align-items-center mt-3 py-2">
          <i class="bi bi-exclamation-circle me-2"></i>
          <span>{{ error }}</span>
        </div>

        <!-- Nút đăng ký -->
        <button type="submit" class="btn btn-success w-100 rounded-pill mt-3">
          <i class="bi bi-check-circle me-2"></i> Đăng ký
        </button>
      </form>

      <p class="text-center text-muted mt-3 small">
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
const confirmPassword = ref('')
const showPassword = ref(false)
const error = ref('')
const router = useRouter()

function handleRegister() {
  error.value = ''

  if (!hoTen.value || !email.value || !password.value || !confirmPassword.value) {
    error.value = '❗ Vui lòng điền đầy đủ thông tin!'
    return
  }

  if (password.value.length < 6) {
    error.value = '❗ Mật khẩu phải có ít nhất 6 ký tự!'
    return
  }

  if (password.value !== confirmPassword.value) {
    error.value = '❗ Mật khẩu và xác nhận không khớp!'
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
      error.value = err.response?.data?.message || '❌ Đăng ký thất bại!'
    })
}
</script>

<style scoped>
.register-container {
  background: linear-gradient(to right, #e3f2fd, #e8f5e9);
  padding: 40px 0;
}
.card {
  border-radius: 20px;
}
input.form-control:focus {
  border-color: #2e7d32;
  box-shadow: 0 0 0 0.2rem rgba(46, 125, 50, 0.25);
}
.input-group-text {
  height: 100%;
}
</style>
