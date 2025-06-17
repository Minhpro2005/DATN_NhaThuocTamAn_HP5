<template>
  <div class="login-container d-flex justify-content-center align-items-center min-vh-100">
    <div class="card p-4 shadow-lg w-100" style="max-width: 420px">
      <div class="text-center mb-4">
        <i class="bi bi-person-circle text-success" style="font-size: 3rem"></i>
        <h4 class="mt-2 text-success fw-bold">Đăng nhập hệ thống</h4>
      </div>

      <form @submit.prevent="handleLogin" novalidate>
        <div class="mb-3">
          <label class="form-label">📧 Email</label>
          <input
            v-model="email"
            type="email"
            class="form-control"
            placeholder="Nhập email"
            required
          />
        </div>

        <div class="mb-3">
          <label class="form-label">🔒 Mật khẩu</label>
          <div class="input-group">
            <input
              :type="showPassword ? 'text' : 'password'"
              v-model="password"
              class="form-control"
              placeholder="Nhập mật khẩu"
              required
            />
            <button class="btn btn-outline-secondary" type="button" @click="togglePassword">
              <i :class="showPassword ? 'bi bi-eye-slash' : 'bi bi-eye'"></i>
            </button>
          </div>
        </div>

        <div v-if="error" class="alert alert-danger text-center py-2">{{ error }}</div>

        <button type="submit" class="btn btn-success w-100 mt-2">
          <i class="bi bi-box-arrow-in-right me-1"></i> Đăng nhập
        </button>
      </form>

      <p class="text-center text-muted mt-3" style="font-size: 0.9rem">
        <router-link to="/quen-mat-khau" class="text-decoration-none me-3"
          >Quên mật khẩu?</router-link
        >
        |
        <router-link to="/dang-ky" class="text-decoration-none ms-3">Đăng ký</router-link>
      </p>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'
import { UserStore } from './UserStore'
import { CartStore } from './CartStore' // ⚠ Đã thêm đồng bộ CartStore

const email = ref('')
const password = ref('')
const error = ref('')
const showPassword = ref(false)
const router = useRouter()

function togglePassword() {
  showPassword.value = !showPassword.value
}

function handleLogin() {
  error.value = ''

  if (!email.value || !password.value) {
    error.value = 'Vui lòng nhập email và mật khẩu'
    return
  }

  axios
    .post('http://localhost:8080/api/auth/dang-nhap', {
      email: email.value,
      matKhau: password.value,
    })
    .then((res) => {
      const user = res.data

      // ✅ Cập nhật reactive user info
      UserStore.setUser(user)
      // ✅ Đồng bộ cart ngay sau login
      CartStore.updateCount()

      alert('✅ Đăng nhập thành công!')

      switch (user.vaiTro) {
        case 1:
        case 2:
          router.push('/admin')
          break
        case 3:
        default:
          router.push('/home')
      }
    })
    .catch((err) => {
      if (
        err.response?.status === 403 &&
        err.response.data?.message?.includes('chưa được xác thực')
      ) {
        alert(err.response.data.message)
        const emailQuery = encodeURIComponent(email.value)
        router.push(`/xac-thuc-email?email=${emailQuery}`)
      } else if (err.response?.status === 401) {
        error.value = '❌ Email hoặc mật khẩu không đúng!'
      } else {
        error.value = '❌ Lỗi kết nối đến máy chủ!'
      }
    })
}
</script>

<style scoped>
@import url('https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css');

.login-container {
  background: linear-gradient(to right, #e3f2fd, #f1f8e9);
  padding: 40px 20px;
}
.card {
  border-radius: 20px;
  background-color: #fff;
}
input.form-control:focus {
  border-color: #2e7d32;
  box-shadow: 0 0 0 0.2rem rgba(46, 125, 50, 0.25);
}
button.btn-success:hover {
  background-color: #2e7d32;
}
</style>
