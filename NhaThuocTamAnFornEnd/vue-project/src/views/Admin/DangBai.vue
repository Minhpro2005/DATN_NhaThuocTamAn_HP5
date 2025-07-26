<template>
  <div class="container py-4">
    <h4 class="mb-4 text-success">📰 Quản Lý Tin Tức</h4>

    <!-- Nút đăng bài -->
    <div class="text-end mb-3">
      <button class="btn btn-success px-3 py-2" @click="hienForm = true">
        <i class="bi bi-plus-circle me-1"></i> Đăng bài viết mới
      </button>
    </div>

    <!-- Danh sách bài viết -->
    <div class="row">
      <div v-for="tin in tinTuc" :key="tin.maTin" class="col-md-4 mb-4">
        <div class="card shadow-sm h-100 border-0 hover-card">
          <img
            :src="tin.preview || 'https://via.placeholder.com/400x200?text=Ảnh+tin+tức'"
            class="card-img-top rounded-top"
            height="180"
            style="object-fit: cover"
          />
          <div class="card-body d-flex flex-column">
            <h5 class="card-title fw-bold text-success">{{ tin.tieuDe }}</h5>

            <router-link
              :to="`/admin/chi-tiet-bai-viet/${tin.maTin}`"
              class="btn btn-outline-primary btn-sm align-self-start"
            >
              <i class="bi bi-eye-fill me-1"></i> Xem chi tiết
            </router-link>
          </div>
        </div>
      </div>
    </div>

    <!-- Modal đăng bài -->
    <div v-if="hienForm" class="modal-overlay">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title">📝 Đăng Bài Viết Mới</h5>
          <button class="btn-close" @click="hienForm = false"></button>
        </div>

        <div class="modal-body">
          <form @submit.prevent="dangBai">
            <div class="mb-3">
              <label class="form-label fw-bold">Tiêu đề</label>
              <input v-model="baiViet.tieuDe" type="text" class="form-control" required />
            </div>

            <div class="mb-3">
              <label class="form-label fw-bold">Mô tả</label>
              <textarea v-model="baiViet.moTa" class="form-control" required></textarea>
            </div>

            <div class="mb-3">
              <label class="form-label fw-bold">Nội dung</label>
              <textarea v-model="baiViet.noiDung" class="form-control" rows="5" required></textarea>
            </div>

            <div class="mb-3">
              <label class="form-label fw-bold">Ảnh đại diện</label>
              <input type="file" @change="chonAnh" class="form-control" />
            </div>

            <div v-if="preview" class="mb-3 text-center">
              <img
                :src="preview"
                class="img-thumbnail rounded shadow-sm"
                style="max-width: 200px"
              />
            </div>

            <div class="text-end">
              <button class="btn btn-success" type="submit">
                <i class="bi bi-upload"></i> Đăng bài
              </button>
              <button class="btn btn-secondary ms-2" @click="hienForm = false" type="button">
                Hủy
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'

const hienForm = ref(false)
const tinTuc = ref([])
const preview = ref(null)
const selectedFile = ref(null)

const user = { hoTen: 'Nguyễn Văn B' }

const baiViet = ref({
  tieuDe: '',
  moTa: '',
  noiDung: '',
  tacGia: user.hoTen,
  hinhAnh: '',
})

// Chọn và preview ảnh
const chonAnh = (e) => {
  const file = e.target.files[0]
  if (file) {
    selectedFile.value = file
    const reader = new FileReader()
    reader.onload = () => {
      preview.value = reader.result
    }
    reader.readAsDataURL(file)
  }
}

// Gửi bài viết
const dangBai = async () => {
  try {
    // 1. Upload ảnh
    if (selectedFile.value) {
      const formData = new FormData()
      formData.append('file', selectedFile.value)
      formData.append('type', 'tintuc')

      const res = await axios.post('http://localhost:8080/api/upload', formData)
      baiViet.value.hinhAnh = res.data.filename // lưu đường dẫn
    }

    // 2. Gửi API tạo bài viết
    const response = await axios.post('http://localhost:8080/api/tintuc', {
      ...baiViet.value,
      ngayDang: new Date().toISOString().split('T')[0],
    })

    // 3. Thêm vào danh sách hiển thị
    tinTuc.value.unshift({
      ...response.data,
      preview: 'http://localhost:8080/' + response.data.hinhAnh,
    })

    hienForm.value = false
    baiViet.value = { tieuDe: '', moTa: '', noiDung: '', tacGia: user.hoTen, hinhAnh: '' }
    selectedFile.value = null
    preview.value = null

    alert('✅ Bài viết đã được đăng thành công!')
  } catch (err) {
    console.error('Lỗi đăng bài:', err)
    alert('❌ Lỗi khi đăng bài viết.')
  }
}

// Tải danh sách tin tức
onMounted(async () => {
  try {
    const res = await axios.get('http://localhost:8080/api/tintuc')
    tinTuc.value = res.data.map((t) => ({
      ...t,
      preview: t.hinhAnh ? 'http://localhost:8080/' + t.hinhAnh : null,
    }))
  } catch (err) {
    console.warn('Chưa có API, dữ liệu sẽ trống.')
  }
})
</script>

<style scoped>
.card-title {
  font-size: 1.1rem;
}
.hover-card:hover {
  transform: scale(1.02);
  transition: 0.2s;
}
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  z-index: 1050;
  display: flex;
  justify-content: center;
  align-items: center;
}
.modal-content {
  background: white;
  border-radius: 12px;
  padding: 24px;
  max-width: 600px;
  width: 100%;
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.3);
}
.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1rem;
}
.modal-body {
  max-height: 70vh;
  overflow-y: auto;
}
</style>
