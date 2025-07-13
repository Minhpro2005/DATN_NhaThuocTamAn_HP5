<template>
  <div class="compare-bar-wrapper" v-if="compareList.length > 0">
    <div class="compare-bar container d-flex align-items-center justify-content-between">
      <!-- Danh sách sản phẩm -->
      <div class="d-flex gap-3 align-items-center flex-grow-1 overflow-auto">
        <div v-for="(item, index) in paddedList" :key="index" class="compare-item">
          <div v-if="item" class="position-relative text-center">
            <img :src="getImage(item.hinhAnh)" class="compare-img" />
            <div class="compare-name">{{ item.tenThuoc }}</div>
            <button class="btn-close compare-close" @click="remove(index)"></button>
          </div>
          <div v-else class="empty-item">
            <div class="plus-icon">+</div>
            <div class="text-muted small">Chọn sản phẩm so sánh</div>
          </div>
        </div>
      </div>

      <!-- Nút hành động -->
      <div class="ms-3 text-end">
        <small>Đã chọn {{ compareList.length }} sản phẩm</small>
        <div class="mt-2 d-flex gap-2">
          <button class="btn btn-outline-secondary btn-sm" @click="clearList">Thu gọn</button>
          <button
            class="btn btn-danger btn-sm"
            :disabled="compareList.length < 2"
            @click="goToCompare"
          >
            So sánh
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'

const key = 'compare_list'
const compareList = ref([])

const router = useRouter()

function loadList() {
  compareList.value = JSON.parse(localStorage.getItem(key)) || []
}

onMounted(() => {
  loadList()
  window.addEventListener('compare-updated', loadList)
})

onUnmounted(() => {
  window.removeEventListener('compare-updated', loadList)
})

const paddedList = computed(() => {
  const list = [...compareList.value]
  while (list.length < 4) list.push(null)
  return list
})

function getImage(path) {
  return path?.startsWith('http') ? path : `http://localhost:8080/${path}`
}

function remove(index) {
  compareList.value.splice(index, 1)
  localStorage.setItem(key, JSON.stringify(compareList.value))
  window.dispatchEvent(new Event('compare-updated'))
}

function clearList() {
  compareList.value = []
  localStorage.setItem(key, JSON.stringify([]))
  window.dispatchEvent(new Event('compare-updated'))
}

function goToCompare() {
  router.push('/so-sanh')
}
</script>

<style scoped>
.compare-bar-wrapper {
  position: fixed;
  bottom: 20px;
  left: 50%;
  transform: translateX(-50%);
  z-index: 9999;
  width: 90%;
  max-width: 1000px;
}

.compare-bar {
  background: #fff;
  border-radius: 16px;
  padding: 16px 20px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.12);
  border: 1px solid #eee;
}

.compare-item {
  width: 120px;
  height: 120px;
  background: #f9f9f9;
  border-radius: 12px;
  border: 1px dashed #ccc;
  padding: 10px;
  flex-shrink: 0;
  text-align: center;
  position: relative;
}

.compare-img {
  max-height: 60px;
  object-fit: contain;
  margin-bottom: 5px;
}

.compare-name {
  font-size: 12px;
  font-weight: 500;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.compare-close {
  position: absolute;
  top: 4px;
  right: 4px;
  background: transparent;
  font-size: 14px;
}

.empty-item {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  height: 100%;
  color: #aaa;
}

.plus-icon {
  font-size: 24px;
  color: #bbb;
  font-weight: bold;
}
</style>
