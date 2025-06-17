<template>
  <div
    class="forgot-password-container d-flex justify-content-center align-items-center min-vh-100"
  >
    <div class="card shadow-lg p-4 rounded" style="max-width: 420px; width: 100%">
      <div class="text-center mb-3">
        <i class="bi bi-lock-fill text-success" style="font-size: 2.5rem"></i>
        <h4 class="mt-2 text-success">Quên mật khẩu</h4>
        <p class="text-muted mb-0" style="font-size: 0.9rem">
          Vui lòng điền thông tin để đặt lại mật khẩu
        </p>
      </div>

      <form @submit.prevent="handleResetPassword">
        <!-- Email -->
        <div class="form-group mb-3">
          <label for="email">📧 Email</label>
          <input
            type="email"
            class="form-control"
            id="email"
            v-model="email"
            placeholder="Nhập email"
            required
          />
        </div>

        <!-- Mã xác thực -->
        <div class="form-group mb-3">
          <label for="verificationCode">🔐 Mã xác thực</label>
          <div class="input-group">
            <input
              type="text"
              class="form-control"
              id="verificationCode"
              v-model="verificationCode"
              placeholder="Nhập mã xác thực"
              required
            />
            <button
              class="btn btn-outline-primary"
              type="button"
              :disabled="countdown > 0"
              @click="sendCode"
            >
              {{ countdown > 0 ? `Gửi lại (${countdown}s)` : 'Gửi mã' }}
            </button>
          </div>
        </div>

        <!-- Mật khẩu mới -->
        <div class="form-group mb-3">
          <label for="newPassword">🔑 Mật khẩu mới</label>
          <div class="input-group">
            <input
              :type="showNewPassword ? 'text' : 'password'"
              class="form-control"
              id="newPassword"
              v-model="newPassword"
              placeholder="Nhập mật khẩu mới"
              required
            />
            <button class="btn btn-outline-secondary" type="button" @click="toggleNewPassword">
              <i :class="showNewPassword ? 'bi bi-eye-slash' : 'bi bi-eye'"></i>
            </button>
          </div>
        </div>

        <!-- Xác nhận mật khẩu -->
        <div class="form-group mb-3">
          <label for="confirmPassword">✅ Xác nhận mật khẩu</label>
          <div class="input-group">
            <input
              :type="showConfirmPassword ? 'text' : 'password'"
              class="form-control"
              id="confirmPassword"
              v-model="confirmPassword"
              placeholder="Nhập lại mật khẩu mới"
              required
            />
            <button class="btn btn-outline-secondary" type="button" @click="toggleConfirmPassword">
              <i :class="showConfirmPassword ? 'bi bi-eye-slash' : 'bi bi-eye'"></i>
            </button>
          </div>
        </div>

        <!-- Thông báo -->
        <div v-if="errorMessage" class="alert alert-danger py-2">{{ errorMessage }}</div>
        <div v-if="successMessage" class="alert alert-success py-2">{{ successMessage }}</div>

        <!-- Buttons -->
        <button type="submit" class="btn btn-success w-100 mt-2">
          <i class="bi bi-arrow-repeat me-1"></i>Đổi mật khẩu
        </button>
        <button type="button" class="btn btn-secondary w-100 mt-2" @click="handleCancel">
          <i class="bi bi-x-circle me-1"></i>Hủy
        </button>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'

const router = useRouter()

// Form
const email = ref('')
const verificationCode = ref('')
const newPassword = ref('')
const confirmPassword = ref('')

// Trạng thái
const errorMessage = ref('')
const successMessage = ref('')

// Ẩn/hiện mật khẩu
const showNewPassword = ref(false)
const showConfirmPassword = ref(false)

function toggleNewPassword() {
  showNewPassword.value = !showNewPassword.value
}
function toggleConfirmPassword() {
  showConfirmPassword.value = !showConfirmPassword.value
}

// Đếm ngược gửi mã
const countdown = ref(0)
let timer = null

function startCountdown() {
  countdown.value = 60
  timer = setInterval(() => {
    if (countdown.value > 0) {
      countdown.value--
    } else {
      clearInterval(timer)
    }
  }, 1000)
}

function sendCode() {
  errorMessage.value = ''
  if (!email.value) {
    errorMessage.value = '⚠️ Vui lòng nhập email trước khi gửi mã!'
    return
  }

  axios
    .post('http://localhost:8080/api/forgot/send-code', { email: email.value })
    .then(() => {
      alert(`✅ Mã xác thực đã gửi tới ${email.value}`)
      startCountdown()
    })
    .catch((err) => {
      errorMessage.value = err.response?.data?.message || 'Không thể gửi mã. Vui lòng thử lại!'
    })
}

function handleResetPassword() {
  errorMessage.value = ''
  successMessage.value = ''

  if (!email.value || !verificationCode.value || !newPassword.value || !confirmPassword.value) {
    errorMessage.value = '⚠️ Vui lòng điền đầy đủ thông tin!'
    return
  }

  if (newPassword.value !== confirmPassword.value) {
    errorMessage.value = '⚠️ Mật khẩu xác nhận không khớp.'
    return
  }

  axios
    .post('http://localhost:8080/api/forgot/verify-code', {
      email: email.value,
      code: verificationCode.value,
    })
    .then(() => {
      return axios.post('http://localhost:8080/api/forgot/reset-pass', {
        email: email.value,
        newPassword: newPassword.value,
      })
    })
    .then(() => {
      successMessage.value = '✅ Mật khẩu đã được đặt lại thành công!'
      setTimeout(() => {
        router.push('/dang-nhap')
      }, 2000)
    })
    .catch((err) => {
      errorMessage.value =
        err.response?.data?.message || '❌ Mã xác thực không hợp lệ hoặc đã hết hạn!'
    })
}

function handleCancel() {
  router.push('/dang-nhap')
}
</script>

<style scoped>
.forgot-password-container {
  background: linear-gradient(to right, #e3f2fd, #f1f8e9);
  padding: 40px 20px;
}
.card {
  background-color: #fff;
  border-radius: 16px;
}
input.form-control:focus {
  border-color: #2e7d32;
  box-shadow: 0 0 0 0.2rem rgba(46, 125, 50, 0.25);
}
button.btn-success:hover {
  background-color: #2e7d32;
}
</style>
