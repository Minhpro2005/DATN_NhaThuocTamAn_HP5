<template>
  <div class="review-wrapper mt-5">
    <h4 class="mb-4">
      <i class="bi bi-star-fill text-warning me-2"></i>
      Đánh giá sản phẩm
    </h4>

    <!-- Tổng đánh giá -->
    <div class="rating-summary mb-4">
      <h5 class="text-success fw-bold">{{ averageRating.toFixed(1) }}/5 sao</h5>
      <p class="text-muted">Dựa trên {{ reviews.length }} đánh giá</p>
    </div>

    <!-- Danh sách đánh giá -->
    <div v-for="(review, index) in reviews" :key="index" class="review-item mb-3 d-flex gap-3">
      <img :src="generateAvatar(review.name)" class="avatar" />
      <div class="flex-grow-1">
        <div class="d-flex justify-content-between align-items-center">
          <strong>{{ review.name }}</strong>
          <span class="text-warning">
            <i class="bi bi-star-fill" v-for="i in review.stars" :key="i"></i>
          </span>
        </div>
        <small class="text-muted">{{ review.date }}</small>
        <p v-if="review.comment" class="mb-1">{{ review.comment }}</p>
        <div v-if="review.hinhAnh" class="review-image mb-2">
          <img :src="getImageUrl(review.hinhAnh)" alt="Ảnh minh hoạ" />
        </div>

        <!-- Hiển thị phản hồi -->
        <div v-if="review.phanHoi" class="bg-light p-2 rounded mt-2">
          <strong class="text-success">Admin:</strong> {{ review.phanHoi }}
        </div>

        <!-- Nút phản hồi cho admin -->
        <button
          v-if="isAdmin && !review.phanHoi"
          class="btn btn-sm btn-outline-success mt-2"
          @click="startReply(review.maDG)"
        >
          💬 Phản hồi
        </button>

        <!-- Form phản hồi -->
        <div v-if="isAdmin && editingReplyId === review.maDG" class="mt-2">
          <textarea
            v-model="replyContent"
            class="form-control mb-2"
            placeholder="Nhập phản hồi..."
          ></textarea>
          <button class="btn btn-sm btn-success me-2" @click="submitReply(review.maDG)">Gửi</button>
          <button class="btn btn-sm btn-secondary" @click="editingReplyId = null">Hủy</button>
        </div>

        <!-- Chỉnh sửa / xoá -->
        <div v-if="user && review.maKH === user.maKH" class="mt-2">
          <button class="btn btn-sm btn-outline-primary me-2" @click="startEdit(review)">✏️</button>
          <button class="btn btn-sm btn-outline-danger" @click="deleteReview(review.maDG)">
            🗑️
          </button>
        </div>
      </div>
    </div>

    <!-- Form chỉnh sửa -->
    <div v-if="editingReview" class="mt-4 card card-body">
      <h6 class="fw-bold mb-2">✏️ Chỉnh sửa đánh giá</h6>
      <div class="mb-2">
        <label class="form-label">Số sao</label>
        <select v-model="editedStars" class="form-select">
          <option v-for="n in 5" :key="n" :value="n">{{ n }} sao</option>
        </select>
      </div>
      <div class="mb-2">
        <label class="form-label">Nội dung</label>
        <textarea v-model="editedComment" class="form-control" rows="3"></textarea>
      </div>
      <div class="mb-2">
        <label class="form-label">Hình ảnh (tuỳ chọn)</label>
        <input type="file" class="form-control" @change="(e) => (editedFile = e.target.files[0])" />
      </div>
      <div class="text-end">
        <button class="btn btn-secondary me-2" @click="editingReview = null">Huỷ</button>
        <button class="btn btn-success" @click="updateReview">Lưu</button>
      </div>
    </div>

    <!-- Form đánh giá mới -->
    <form
      @submit.prevent="submitReview"
      class="mt-4"
      v-if="user && !hasReviewed && isEligibleToReview"
    >
      <div class="row g-3">
        <div class="col-md-6">
          <label class="form-label">Tên của bạn</label>
          <input type="text" class="form-control" :value="user.hoTen" disabled />
        </div>
        <div class="col-md-6">
          <label class="form-label">Số sao</label>
          <select v-model="newReview.stars" class="form-select" required>
            <option v-for="n in 5" :key="n" :value="n">{{ n }} sao</option>
          </select>
        </div>
        <div class="col-md-12">
          <label class="form-label">Nội dung đánh giá</label>
          <textarea v-model="newReview.comment" class="form-control" rows="3"></textarea>
        </div>
        <div class="col-md-12">
          <label class="form-label">Hình ảnh minh họa (tùy chọn)</label>
          <input type="file" class="form-control" @change="handleFileUpload" />
        </div>
      </div>
      <button type="submit" class="btn btn-success mt-3">Gửi đánh giá</button>
    </form>

    <ToastMessage ref="toastRef" />
  </div>
</template>

<script setup>
import { ref, onMounted, computed, inject } from 'vue'
import axios from 'axios'
import { UserStore } from '../User/UserStore'
import ToastMessage from '../ToastMessage.vue'

const toastRef = ref(null)
const product = inject('product')
const selectedBienThe = inject('selectedBienThe')
const maThuoc = computed(() => product.value?.maThuoc || null)
const maBienThe = computed(() => selectedBienThe.value?.maBienThe || null)

const user = UserStore.userInfo
const isAdmin = computed(() => user && user.vaiTro === 'ADMIN')

const reviews = ref([])
const hasReviewed = ref(false)
const isEligibleToReview = ref(false)

const newReview = ref({ stars: 3, comment: '', file: null })
const editingReview = ref(null)
const editedComment = ref('')
const editedStars = ref(5)
const editedFile = ref(null)

const editingReplyId = ref(null)
const replyContent = ref('')

const averageRating = computed(() => {
  if (!reviews.value.length) return 0
  return reviews.value.reduce((sum, r) => sum + r.stars, 0) / reviews.value.length
})

function generateAvatar(name) {
  return `https://ui-avatars.com/api/?name=${encodeURIComponent(name)}&background=43a047&color=fff&rounded=true&size=64`
}
function getImageUrl(path) {
  if (!path) return ''
  if (path.startsWith('http')) return path
  return `http://localhost:8080/uploads/${path.replace(/^.*[\\/]/, '')}`
}
function handleFileUpload(e) {
  newReview.value.file = e.target.files[0]
}

async function fetchReviews() {
  try {
    let res
    if (maBienThe.value)
      res = await axios.get(`http://localhost:8080/api/danh-gia/bien-the/${maBienThe.value}`)
    else if (maThuoc.value)
      res = await axios.get(`http://localhost:8080/api/danh-gia/thuoc/${maThuoc.value}`)
    else return

    reviews.value = res.data.map((r) => ({
      maDG: r.maDG,
      maKH: r.maKH,
      name: r.tenKhachHang,
      stars: r.soSao,
      comment: r.binhLuan,
      date: r.ngayDanhGia,
      hinhAnh: r.hinhAnh,
      phanHoi: r.phanHoi, // ✅ thêm vào
    }))
  } catch (err) {
    console.error('Lỗi khi lấy đánh giá:', err)
  }
}

async function startReply(maDG) {
  editingReplyId.value = maDG
  replyContent.value = ''
}

async function submitReply(maDG) {
  try {
    await axios.put(`http://localhost:8080/api/danh-gia/phan-hoi/${maDG}`, {
      phanHoi: replyContent.value,
    })
    toastRef.value.show('✅ Đã gửi phản hồi!', 'success')
    editingReplyId.value = null
    await fetchReviews()
  } catch (err) {
    toastRef.value.show('❌ Lỗi khi gửi phản hồi.', 'error')
  }
}

async function checkIfReviewed() {
  if (!user) return
  try {
    const params = { maKH: user.maKH }
    if (maBienThe.value) params.maBienThe = maBienThe.value
    else if (maThuoc.value) params.maThuoc = maThuoc.value

    const res = await axios.get(`http://localhost:8080/api/danh-gia/da-danh-gia`, { params })
    hasReviewed.value = res.data === true
  } catch (err) {
    console.warn('Không kiểm tra được trạng thái đánh giá:', err)
  }
}

async function checkEligibility() {
  if (!user) return
  try {
    const params = { maKH: user.maKH }
    if (maBienThe.value) params.maBienThe = maBienThe.value
    else if (maThuoc.value) params.maThuoc = maThuoc.value

    const res = await axios.get(`http://localhost:8080/api/danh-gia/co-duoc-danh-gia`, { params })
    isEligibleToReview.value = res.data === true
  } catch (err) {
    console.warn('Lỗi kiểm tra điều kiện đánh giá:', err)
    isEligibleToReview.value = false
  }
}

async function submitReview() {
  try {
    const formData = new FormData()
    formData.append('maKH', user.maKH)
    if (maBienThe.value) formData.append('maBienThe', maBienThe.value)
    else if (maThuoc.value) formData.append('maThuoc', maThuoc.value)
    formData.append('soSao', newReview.value.stars)
    formData.append('binhLuan', newReview.value.comment)
    if (newReview.value.file) formData.append('hinhAnh', newReview.value.file)

    const res = await axios.post('http://localhost:8080/api/danh-gia/upload', formData, {
      headers: { 'Content-Type': 'multipart/form-data' },
    })

    reviews.value.unshift({
      maDG: res.data.maDG,
      maKH: user.maKH,
      name: user.hoTen,
      stars: newReview.value.stars,
      comment: newReview.value.comment,
      date: new Date().toLocaleDateString('vi-VN'),
      hinhAnh: res.data.hinhAnh || null,
      phanHoi: null,
    })

    newReview.value = { stars: 3, comment: '', file: null }
    hasReviewed.value = true
    toastRef.value.show('✅ Gửi đánh giá thành công!', 'success')
  } catch (err) {
    toastRef.value.show('❌ Không thể gửi đánh giá. Vui lòng thử lại.', 'error')
  }
}

function startEdit(review) {
  editingReview.value = review
  editedComment.value = review.comment
  editedStars.value = review.stars
  editedFile.value = null
}

async function updateReview() {
  try {
    const formData = new FormData()
    formData.append('soSao', editedStars.value)
    formData.append('binhLuan', editedComment.value)
    if (editedFile.value) formData.append('hinhAnh', editedFile.value)

    await axios.put(
      `http://localhost:8080/api/danh-gia/upload/${editingReview.value.maDG}`,
      formData,
      {
        headers: { 'Content-Type': 'multipart/form-data' },
      },
    )

    editingReview.value = null
    await fetchReviews()
    toastRef.value.show('✅ Đã cập nhật đánh giá.', 'success')
  } catch (err) {
    toastRef.value.show('❌ Lỗi khi cập nhật đánh giá.', 'error')
  }
}

async function deleteReview(maDG) {
  if (!confirm('Bạn có chắc muốn xoá đánh giá này?')) return
  try {
    await axios.delete(`http://localhost:8080/api/danh-gia/${maDG}`)
    toastRef.value.show('✅ Đã xoá đánh giá.', 'success')
    await fetchReviews()
    hasReviewed.value = false
  } catch (err) {
    toastRef.value.show('❌ Lỗi khi xoá đánh giá.', 'error')
  }
}

onMounted(() => {
  fetchReviews()
  if (user) {
    checkIfReviewed()
    checkEligibility()
  }
})
</script>

<style scoped>
.review-wrapper {
  background-color: #fff;
  padding: 24px;
  border-radius: 16px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  max-width: 960px;
  margin: auto;
}
.review-item .avatar {
  width: 50px;
  height: 50px;
  object-fit: cover;
  border-radius: 50%;
  border: 2px solid #ccc;
}
.review-image img {
  max-width: 180px;
  max-height: 180px;
  border-radius: 8px;
  border: 1px solid #ccc;
  object-fit: contain;
}
</style>
