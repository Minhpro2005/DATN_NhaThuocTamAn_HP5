<template>
  <div
    class="forgot-password-container d-flex justify-content-center align-items-center min-vh-100"
  >
    <div class="card shadow-lg p-4 rounded" style="max-width: 460px; width: 100%">
      <div class="text-center mb-3">
        <i class="bi bi-lock-fill text-success" style="font-size: 2.5rem"></i>
        <h4 class="mt-2 text-success fw-bold">Quên mật khẩu</h4>
        <p class="text-muted mb-0 small">Vui lòng điền thông tin để đặt lại mật khẩu</p>
      </div>

      <form @submit.prevent="handleResetPassword">
        <!-- Email -->
        <div class="mb-3">
          <label class="form-label d-flex align-items-center">
            <i class="bi bi-envelope-fill me-2 text-danger"></i> Email
          </label>
          <input
            type="email"
            class="form-control rounded-pill"
            v-model="email"
            placeholder="Nhập email"
          />
        </div>

        <!-- Mã xác thực -->
        <div class="mb-3">
          <label class="form-label d-flex align-items-center">
            <i class="bi bi-key-fill me-2 text-warning"></i> Mã xác thực
          </label>
          <div class="input-group">
            <input
              type="text"
              class="form-control rounded-start-pill border-end-0"
              v-model="verificationCode"
              placeholder="Nhập mã xác thực"
            />
            <button
              class="btn btn-outline-primary rounded-end-pill"
              type="button"
              :disabled="countdown > 0"
              @click="sendCode"
            >
              {{ countdown > 0 ? `Gửi lại (${countdown}s)` : 'Gửi mã' }}
            </button>
          </div>
        </div>

        <!-- Mật khẩu mới -->
        <div class="mb-3">
          <label class="form-label d-flex align-items-center">
            <i class="bi bi-shield-lock-fill me-2 text-info"></i> Mật khẩu mới
          </label>
          <div class="input-group">
            <input
              :type="showNewPassword ? 'text' : 'password'"
              class="form-control rounded-start-pill border-end-0"
              v-model="newPassword"
              placeholder="Tối thiểu 6 ký tự"
            />
            <span
              class="input-group-text bg-white rounded-end-pill border-start-0"
              @click="toggleNewPassword"
              style="cursor: pointer"
            >
              <i :class="showNewPassword ? 'bi bi-eye-slash' : 'bi bi-eye'"></i>
            </span>
          </div>
        </div>

        <!-- Xác nhận mật khẩu -->
        <div class="mb-3">
          <label class="form-label d-flex align-items-center">
            <i class="bi bi-check2-circle me-2 text-success"></i> Xác nhận mật khẩu
          </label>
          <div class="input-group">
            <input
              :type="showConfirmPassword ? 'text' : 'password'"
              class="form-control rounded-start-pill border-end-0"
              v-model="confirmPassword"
              placeholder="Nhập lại mật khẩu mới"
            />
            <span
              class="input-group-text bg-white rounded-end-pill border-start-0"
              @click="toggleConfirmPassword"
              style="cursor: pointer"
            >
              <i :class="showConfirmPassword ? 'bi bi-eye-slash' : 'bi bi-eye'"></i>
            </span>
          </div>
        </div>

        <!-- Thông báo -->
        <div v-if="errorMessage" class="alert alert-danger d-flex align-items-center py-2">
          <i class="bi bi-exclamation-triangle-fill me-2"></i>
          <span>{{ errorMessage }}</span>
        </div>

        <div v-if="successMessage" class="alert alert-success d-flex align-items-center py-2">
          <i class="bi bi-check-circle-fill me-2"></i>
          <span>{{ successMessage }}</span>
        </div>

        <!-- Nút hành động -->
        <button type="submit" class="btn btn-success w-100 rounded-pill mt-2">
          <i class="bi bi-arrow-repeat me-1"></i> Đổi mật khẩu
        </button>
        <button
          type="button"
          class="btn btn-outline-secondary w-100 rounded-pill mt-2"
          @click="handleCancel"
        >
          <i class="bi bi-x-circle me-1"></i> Hủy
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

// Hiện/ẩn mật khẩu
const showNewPassword = ref(false)
const showConfirmPassword = ref(false)

function toggleNewPassword() {
  showNewPassword.value = !showNewPassword.value
}
function toggleConfirmPassword() {
  showConfirmPassword.value = !showConfirmPassword.value
}

// Gửi mã
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

  if (newPassword.value.length < 6) {
    errorMessage.value = '⚠️ Mật khẩu phải có ít nhất 6 ký tự!'
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
      setTimeout(() => router.push('/dang-nhap'), 2000)
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
  border-radius: 20px;
}
input.form-control:focus {
  border-color: #2e7d32;
  box-shadow: 0 0 0 0.2rem rgba(46, 125, 50, 0.25);
}
.input-group-text {
  height: 100%;
}
button:disabled {
  opacity: 0.7;
}
</style>
