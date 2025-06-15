<template>
  <div class="review-wrapper mt-5">
    <h4 class="mb-4">
      <i class="bi bi-star-fill text-warning me-2"></i>
      Đánh giá sản phẩm
    </h4>

    <!-- Tổng đánh giá -->
    <div class="rating-summary mb-4">
      <h5 class="text-success fw-bold">4.5/5 sao</h5>
      <p class="text-muted">Dựa trên {{ reviews.length }} đánh giá</p>
    </div>

    <!-- Danh sách đánh giá -->
    <div v-for="(review, index) in reviews" :key="index" class="review-item mb-3 d-flex gap-3">
      <img :src="review.avatar" class="avatar" />
      <div class="flex-grow-1">
        <div class="d-flex justify-content-between align-items-center">
          <strong>{{ review.name }}</strong>
          <span class="text-warning">
            <i class="bi bi-star-fill" v-for="i in review.stars" :key="i"></i>
          </span>
        </div>
        <p v-if="review.comment" class="mb-0">{{ review.comment }}</p>
      </div>
    </div>

    <!-- Form đánh giá -->
    <form @submit.prevent="submitReview" class="mt-4">
      <div class="row g-3">
        <div class="col-md-6">
          <label class="form-label">Tên của bạn</label>
          <input v-model="newReview.name" type="text" class="form-control" required />
        </div>
        <div class="col-md-6">
          <label class="form-label">Số sao</label>
          <select v-model="newReview.stars" class="form-select" required>
            <option v-for="n in 5" :key="n" :value="n">{{ n }} sao</option>
          </select>
        </div>
        <div class="col-md-12">
          <label class="form-label">Nội dung đánh giá (không bắt buộc)</label>
          <textarea
            v-model="newReview.comment"
            class="form-control"
            rows="3"
            placeholder="Bạn cảm thấy thế nào về sản phẩm?"
          ></textarea>
        </div>
      </div>
      <button type="submit" class="btn btn-success mt-3">Gửi đánh giá</button>
    </form>
  </div>
</template>

<script setup>
import { ref } from 'vue'

// Danh sách đánh giá mẫu
const reviews = ref([
  {
    name: 'Nguyên Nguyễn',
    avatar: generateAvatar('Nguyên Nguyễn'),
    stars: 5,
    comment: 'Sản phẩm rất tốt, giao nhanh, đóng gói kỹ.',
  },
  {
    name: 'Mai Hương',
    avatar: generateAvatar('Mai Hương'),
    stars: 4,
    comment: '',
  },
])

const newReview = ref({
  name: '',
  stars: 5,
  comment: '',
})

// Hàm tạo avatar từ tên
function generateAvatar(name) {
  const base = encodeURIComponent(name.trim())
  return `https://ui-avatars.com/api/?name=${base}&background=43a047&color=fff&rounded=true&size=64`
}

function submitReview() {
  const avatar = generateAvatar(newReview.value.name)
  reviews.value.unshift({ ...newReview.value, avatar })
  newReview.value = { name: '', stars: 5, comment: '' }
}
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
</style>
