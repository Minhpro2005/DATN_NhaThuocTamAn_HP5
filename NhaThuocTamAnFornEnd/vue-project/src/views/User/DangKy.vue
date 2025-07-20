<template>
  <div class="register-page d-flex min-vh-100">
    <!-- Bên trái: giới thiệu -->
    <div
      class="intro-section d-flex flex-column justify-content-center align-items-center text-white"
    >
      <h1 class="fw-bold display-5 text-center">Hệ thống Quản lý Dược phẩm</h1>
      <p class="mt-3 fs-5 text-center">Giải pháp toàn diện cho ngành dược</p>
    </div>

    <!-- Bên phải: form đăng ký -->
    <div class="form-section d-flex flex-column justify-content-center align-items-center">
      <div class="register-box w-100" style="max-width: 450px">
        <div class="text-center mb-4">
          <i class="bi bi-person-plus-fill text-success" style="font-size: 2.5rem"></i>
          <h4 class="mt-2 text-success fw-bold">Đăng ký tài khoản</h4>
          <p class="text-dark text-medium">Vui lòng điền đầy đủ thông tin để tạo tài khoản</p>
        </div>

        <form @submit.prevent="handleRegister" novalidate>
          <!-- Họ tên -->
          <div class="mb-3">
            <label class="form-label">Họ tên</label>
            <div class="input-group">
              <span class="input-group-text"><i class="bi bi-person-fill"></i></span>
              <input
                v-model="hoTen"
                type="text"
                class="form-control"
                placeholder="Nhập họ tên..."
              />
            </div>
          </div>

          <!-- Email -->
          <div class="mb-3">
            <label class="form-label">Email</label>
            <div class="input-group">
              <span class="input-group-text"><i class="bi bi-envelope-fill"></i></span>
              <input
                v-model="email"
                type="email"
                class="form-control"
                placeholder="Nhập email..."
              />
            </div>
          </div>

          <!-- Mật khẩu -->
          <div class="mb-3">
            <label class="form-label">Mật khẩu</label>
            <div class="input-group">
              <span class="input-group-text"><i class="bi bi-lock-fill"></i></span>
              <input
                :type="showPassword ? 'text' : 'password'"
                v-model="password"
                class="form-control"
                placeholder="Tối thiểu 6 ký tự"
              />
              <button
                type="button"
                class="btn btn-outline-secondary"
                @click="showPassword = !showPassword"
              >
                <i :class="showPassword ? 'bi bi-eye-slash' : 'bi bi-eye'"></i>
              </button>
            </div>
          </div>

          <!-- Xác nhận mật khẩu -->
          <div class="mb-3">
            <label class="form-label">Xác nhận mật khẩu</label>
            <div class="input-group">
              <span class="input-group-text"><i class="bi bi-shield-lock-fill"></i></span>
              <input
                :type="showPassword ? 'text' : 'password'"
                v-model="confirmPassword"
                class="form-control"
                placeholder="Nhập lại mật khẩu"
              />
              <button
                type="button"
                class="btn btn-outline-secondary"
                @click="showPassword = !showPassword"
              >
                <i :class="showPassword ? 'bi bi-eye-slash' : 'bi bi-eye'"></i>
              </button>
            </div>
          </div>

          <!-- Thông báo lỗi -->
          <div v-if="error" class="alert alert-danger text-center">{{ error }}</div>

          <!-- Nút đăng ký -->
          <button type="submit" class="btn btn-success w-100">Đăng ký</button>
        </form>

        <p class="text-center mt-3">
          Đã có tài khoản?
          <router-link to="/dang-nhap" class="fw-bold text-success text-decoration-none"
            >Đăng nhập</router-link
          >
        </p>
      </div>
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
@import url('https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css');

.register-page {
  display: flex;
  height: 100vh;
}

.intro-section {
  background-image: url('https://images.pexels.com/photos/7615574/pexels-photo-7615574.jpeg');
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  width: 50%;
  padding: 40px;
  color: white;
}

.intro-section h1,
.intro-section p {
  text-shadow: 1px 1px 4px rgba(0, 0, 0, 0.6);
}

.form-section {
  width: 50%;
  padding: 40px;
  background-color: #ffffff;
}

.register-box {
  background-color: #ffffff;
  border-radius: 1rem;
  padding: 2rem;
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.1);
}

.text-medium {
  font-weight: 500;
}
</style>
