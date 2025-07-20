<template>
  <div class="verify-container d-flex justify-content-center align-items-center min-vh-100">
    <div class="card p-4 shadow-lg w-100" style="max-width: 460px; border-radius: 16px">
      <div class="text-center mb-3">
        <i class="bi bi-envelope-check-fill text-success" style="font-size: 3rem"></i>
        <h4 class="text-success fw-bold mt-2">Xác thực email</h4>
      </div>

      <div class="mb-3">
        <label class="form-label">🔐 Mã xác thực</label>
        <input v-model="code" class="form-control" placeholder="Nhập mã gồm 6 ký tự" />
      </div>

      <div v-if="error" class="alert alert-danger text-center py-2">
        <i class="bi bi-exclamation-triangle-fill me-2"></i>{{ error }}
      </div>

      <button @click="xacThuc" class="btn btn-success w-100 mb-2 fw-bold">✅ Xác thực</button>

      <button class="btn btn-link w-100 text-success" @click="guiLaiMa" :disabled="countdown > 0">
        <span v-if="countdown === 0">📩 Gửi lại mã xác thực</span>
        <span v-else>⏳ Gửi lại mã sau {{ countdown }}s</span>
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import axios from 'axios'

const route = useRoute()
const router = useRouter()

const email = route.query.email || ''
const code = ref('')
const error = ref('')
const countdown = ref(0)
let countdownInterval = null

function xacThuc() {
  error.value = ''
  if (!code.value) {
    error.value = 'Vui lòng nhập mã xác thực!'
    return
  }

  axios
    .post('http://localhost:8080/api/auth/xac-thuc-email', {
      email,
      code: code.value,
    })
    .then(() => {
      alert('✅ Xác thực thành công!')
      router.push('/dang-nhap')
    })
    .catch(() => {
      error.value = '❌ Mã xác thực không đúng hoặc đã hết hạn!'
    })
}

function guiLaiMa() {
  axios
    .post('http://localhost:8080/api/forgot/send-code', { email })
    .then(() => {
      alert('📨 Đã gửi lại mã xác thực!')
      startCountdown()
    })
    .catch(() => {
      error.value = 'Không thể gửi lại mã, thử lại sau.'
    })
}

function startCountdown() {
  countdown.value = 60
  clearInterval(countdownInterval)
  countdownInterval = setInterval(() => {
    countdown.value--
    if (countdown.value <= 0) {
      clearInterval(countdownInterval)
    }
  }, 1000)
}
</script>

<style scoped>
.verify-container {
  background: linear-gradient(to right, #f1f8e9, #e3f2fd);
  padding: 20px;
}

.card {
  border-radius: 16px;
  background-color: #fff;
}

button[disabled] {
  opacity: 0.6;
  pointer-events: none;
}

input.form-control:focus {
  border-color: #2e7d32;
  box-shadow: 0 0 0 0.2rem rgba(46, 125, 50, 0.25);
}
</style>
