<template>
  <div class="forgot-container d-flex justify-content-center align-items-center min-vh-100">
    <div
      class="card shadow p-4 text-center"
      style="max-width: 420px; width: 100%; border-radius: 16px"
    >
      <div class="mb-4">
        <img
          src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRiqUlj1LB8gUERYiFqhQZfT6YFZa2VRvqfwA&s"
          alt="icon"
          width="64"
          class="mb-3"
        />
        <h4 class="fw-bold">Quên Mật Khẩu</h4>
        <p class="text-muted small">
          Nhập địa chỉ email đã đăng ký để nhận<br />
          mã khôi phục mật khẩu
        </p>
      </div>

      <form @submit.prevent="handleResetPassword" class="text-start">
        <!-- Email -->
        <div class="mb-3">
          <label class="form-label">Email</label>
          <div class="input-group">
            <span class="input-group-text"><i class="bi bi-envelope-fill"></i></span>
            <input
              v-model="email"
              type="email"
              class="form-control"
              placeholder="Nhập email của bạn"
              required
            />
          </div>
        </div>

        <!-- Mã xác thực -->
        <div class="mb-3">
          <label class="form-label">Mã xác thực</label>
          <div class="input-group">
            <span class="input-group-text"><i class="bi bi-shield-lock-fill"></i></span>
            <input
              v-model="verificationCode"
              type="text"
              class="form-control"
              placeholder="Nhập mã xác thực"
            />
            <button
              class="btn btn-outline-success"
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
          <label class="form-label">Mật khẩu mới</label>
          <div class="input-group">
            <span class="input-group-text"><i class="bi bi-lock-fill"></i></span>
            <input
              :type="showNewPassword ? 'text' : 'password'"
              v-model="newPassword"
              class="form-control"
              placeholder="Tối thiểu 6 ký tự"
            />
            <button type="button" class="btn btn-outline-secondary" @click="toggleNewPassword">
              <i :class="showNewPassword ? 'bi bi-eye-slash' : 'bi bi-eye'"></i>
            </button>
          </div>
        </div>

        <!-- Xác nhận mật khẩu -->
        <div class="mb-3">
          <label class="form-label">Xác nhận mật khẩu</label>
          <div class="input-group">
            <span class="input-group-text"><i class="bi bi-lock-fill"></i></span>
            <input
              :type="showConfirmPassword ? 'text' : 'password'"
              v-model="confirmPassword"
              class="form-control"
              placeholder="Nhập lại mật khẩu"
            />
            <button type="button" class="btn btn-outline-secondary" @click="toggleConfirmPassword">
              <i :class="showConfirmPassword ? 'bi bi-eye-slash' : 'bi bi-eye'"></i>
            </button>
          </div>
        </div>

        <!-- Thông báo -->
        <div v-if="errorMessage" class="alert alert-danger py-2">
          <i class="bi bi-exclamation-triangle me-2"></i> {{ errorMessage }}
        </div>
        <div v-if="successMessage" class="alert alert-success py-2">
          <i class="bi bi-check-circle me-2"></i> {{ successMessage }}
        </div>

        <!-- Nút -->
        <button type="submit" class="btn btn-success w-100 rounded mt-2">Đặt lại mật khẩu</button>

        <div class="text-center mt-3">
          <router-link to="/dang-nhap" class="text-decoration-none small text-success">
            ← Quay lại đăng nhập
          </router-link>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'

const dieuHuong = useRouter()

// Biến dữ liệu
const email = ref('')
const maXacThuc = ref('')
const matKhauMoi = ref('')
const xacNhanMatKhau = ref('')
const hienMatKhauMoi = ref(false)
const hienXacNhanMatKhau = ref(false)

const thongBaoLoi = ref('')
const thongBaoThanhCong = ref('')

const demNguoc = ref(0)
let boDemThoiGian = null

// Toggle hiện/ẩn mật khẩu
function chuyenTrangThaiMatKhauMoi() {
  hienMatKhauMoi.value = !hienMatKhauMoi.value
}

function chuyenTrangThaiXacNhanMatKhau() {
  hienXacNhanMatKhau.value = !hienXacNhanMatKhau.value
}

// Bắt đầu đếm ngược gửi lại mã
function batDauDemNguoc() {
  demNguoc.value = 60
  boDemThoiGian = setInterval(() => {
    if (demNguoc.value > 0) {
      demNguoc.value--
    } else {
      clearInterval(boDemThoiGian)
    }
  }, 1000)
}

// Gửi mã xác thực
function guiMa() {
  thongBaoLoi.value = ''
  if (!email.value) {
    thongBaoLoi.value = 'Vui lòng nhập email trước khi gửi mã xác thực!'
    return
  }

  axios
    .post('http://localhost:8080/api/forgot/send-code', { email: email.value })
    .then(() => {
      alert('✅ Mã xác thực đã được gửi!')
      batDauDemNguoc()
    })
    .catch((err) => {
      thongBaoLoi.value = err.response?.data?.message || 'Không thể gửi mã. Vui lòng thử lại sau.'
    })
}

// Xử lý đặt lại mật khẩu
function xuLyDatLaiMatKhau() {
  thongBaoLoi.value = ''
  thongBaoThanhCong.value = ''

  if (!email.value || !maXacThuc.value || !matKhauMoi.value || !xacNhanMatKhau.value) {
    thongBaoLoi.value = 'Vui lòng điền đầy đủ các trường.'
    return
  }

  if (matKhauMoi.value.length < 6) {
    thongBaoLoi.value = 'Mật khẩu phải có ít nhất 6 ký tự.'
    return
  }

  if (matKhauMoi.value !== xacNhanMatKhau.value) {
    thongBaoLoi.value = 'Mật khẩu xác nhận không khớp.'
    return
  }

  axios
    .post('http://localhost:8080/api/forgot/verify-code', {
      email: email.value,
      code: maXacThuc.value,
    })
    .then(() =>
      axios.post('http://localhost:8080/api/forgot/reset-pass', {
        email: email.value,
        newPassword: matKhauMoi.value,
      }),
    )
    .then(() => {
      thongBaoThanhCong.value = 'Đặt lại mật khẩu thành công!'
      setTimeout(() => dieuHuong.push('/dang-nhap'), 2000)
    })
    .catch((err) => {
      thongBaoLoi.value = err.response?.data?.message || 'Mã xác thực không hợp lệ hoặc đã hết hạn.'
    })
}
</script>

<style scoped>
.forgot-container {
  background: linear-gradient(to right, #f1f8e9, #e3f2fd);
  padding: 20px;
}

.card {
  border-radius: 16px;
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.1);
}

input.form-control:focus {
  border-color: #2e7d32;
  box-shadow: 0 0 0 0.2rem rgba(46, 125, 50, 0.25);
}

button:disabled {
  opacity: 0.7;
  cursor: not-allowed;
}
</style>
