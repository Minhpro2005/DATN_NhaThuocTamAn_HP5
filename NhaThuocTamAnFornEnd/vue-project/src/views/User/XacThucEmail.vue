<template>
  <div class="container d-flex justify-content-center align-items-center min-vh-100">
    <div class="card p-4 shadow-lg w-100" style="max-width: 450px">
      <h4 class="text-center text-success mb-4">🔐 Xác thực email</h4>
      <p class="text-center">
        Chúng tôi đã gửi mã xác thực đến: <strong>{{ email }}</strong>
      </p>

      <div class="mb-3">
        <label class="form-label">Mã xác thực</label>
        <input v-model="code" class="form-control" placeholder="Nhập mã gồm 6 ký tự" />
      </div>

      <div v-if="error" class="alert alert-danger text-center py-2">{{ error }}</div>

      <button @click="xacThuc" class="btn btn-success w-100 mb-2">Xác thực</button>
      <button class="btn btn-link w-100" @click="guiLaiMa" :disabled="countdown > 0">
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
.card {
  border-radius: 16px;
  background-color: #fff;
}
button[disabled] {
  opacity: 0.6;
  pointer-events: none;
}
</style>
