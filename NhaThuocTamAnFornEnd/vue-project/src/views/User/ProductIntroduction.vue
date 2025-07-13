<template>
  <div class="product-details-wrapper product-details-tabs mt-5" v-if="product">
    <div class="row">
      <!-- Tabs bên trái -->
      <div class="col-md-3">
        <ul class="nav flex-column nav-pills">
          <li v-for="(tab, index) in tabs" :key="index" class="nav-item">
            <button
              class="nav-link"
              :class="{ active: selectedTab === tab.key }"
              @click="selectedTab = tab.key"
            >
              {{ tab.label }}
            </button>
          </li>
        </ul>
      </div>

      <!-- Nội dung bên phải -->
      <div class="col-md-9">
        <div v-if="selectedTab === 'moTa'">
          <h5>Mô tả sản phẩm</h5>
          <p v-html="product.moTaThem || 'Đang cập nhật...'"></p>
        </div>

        <div v-else-if="selectedTab === 'thanhPhan'">
          <h5>Thành phần</h5>
          <p v-html="product.thanhPhan || 'Đang cập nhật...'"></p>
        </div>

        <div v-else-if="selectedTab === 'congDung'">
          <h5>Công dụng</h5>
          <p v-html="product.congDung || 'Đang cập nhật...'"></p>
        </div>

        <div v-else-if="selectedTab === 'cachDung'">
          <h5>Cách dùng</h5>
          <p v-html="product.huongDanSuDung || 'Đang cập nhật...'"></p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()
const selectedTab = ref('moTa')

const tabs = [
  { key: 'moTa', label: 'Mô tả sản phẩm' },
  { key: 'thanhPhan', label: 'Thành phần' },
  { key: 'congDung', label: 'Công dụng' },
  { key: 'cachDung', label: 'Cách dùng' },
]

const product = ref(null)

onMounted(async () => {
  try {
    const res = await fetch(`http://localhost:8080/api/thuoc/${route.params.id}`)
    if (res.ok) {
      product.value = await res.json()
    } else {
      console.error('Không thể tải sản phẩm')
    }
  } catch (err) {
    console.error('Lỗi khi fetch sản phẩm:', err)
  }
})
</script>

<style scoped>
.product-details-wrapper {
  background-color: #fff;
  border-radius: 16px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.08);
  padding: 24px;
  max-width: 960px;
  margin: auto;
}

.product-details-tabs .nav-link {
  border-radius: 6px;
  text-align: left;
  color: #333;
  transition: background-color 0.3s ease;
}

.product-details-tabs .nav-link.active {
  background-color: #43a047;
  color: white;
  font-weight: 500;
}

.product-details-tabs .nav-link:hover {
  background-color: #e8f5e9;
}

.product-details-tabs h5 {
  font-weight: bold;
  margin-bottom: 12px;
}

.product-details-tabs p {
  margin-bottom: 12px;
  line-height: 1.6;
}
</style>
