<template>
  <div class="product-details-wrapper product-details-tabs mt-5">
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
          <h5>Chai xịt Aloclair là gì?</h5>
          <p>
            Xác định vị trí vết thương hở trong khoang miệng (cấy ghép Implant, viêm lợi, chảy máu
            chân răng, lở loét, nhiệt miệng,...).
          </p>
          <p>Mở nắp nhựa trong suốt và lắp đầu xịt vào. Xoay đầu xịt đến vị trí thích hợp.</p>
          <p>
            Hướng đầu vòi xịt vào vết thương, nhấn 1–2 lần để dung dịch tiếp xúc với vùng tổn
            thương. An toàn khi nuốt phải.
          </p>
          <p><strong>Đối tượng sử dụng:</strong> Sản phẩm phù hợp dùng cho mọi lứa tuổi.</p>
        </div>

        <div v-else-if="selectedTab === 'thanhPhan'">
          <h5>Thành phần</h5>
          <p>Natri Hyaluronate, Aloe Vera, nước tinh khiết, không chứa cồn và chất kích ứng.</p>
        </div>

        <div v-else-if="selectedTab === 'congDung'">
          <h5>Công dụng</h5>
          <p>
            Giúp tạo màng sinh học bảo vệ, hỗ trợ làm lành tổn thương trong khoang miệng, giảm đau
            tức thì.
          </p>
        </div>

        <div v-else-if="selectedTab === 'cachDung'">
          <h5>Cách dùng</h5>
          <p>
            Xịt trực tiếp vào vùng tổn thương trong khoang miệng. Sử dụng sau khi ăn hoặc trước khi
            đi ngủ.
          </p>
        </div>

        <div v-else-if="selectedTab === 'luuY'">
          <h5 class="text-warning">⚠️ Lưu ý</h5>
          <p>
            Chờ 30 giây để sản phẩm tạo màng sinh học giúp giảm đau và hỗ trợ lành thương. Hạn chế
            ăn/uống trong 30 phút sau khi sử dụng để đạt hiệu quả cao nhất.
          </p>
        </div>

        <div v-else-if="selectedTab === 'baoQuan'">
          <h5>Bảo quản</h5>
          <p>Nơi khô ráo, thoáng mát, tránh ánh sáng trực tiếp và nhiệt độ cao.</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const tabs = [
  { key: 'moTa', label: 'Mô tả sản phẩm' },
  { key: 'thanhPhan', label: 'Thành phần' },
  { key: 'congDung', label: 'Công dụng' },
  { key: 'cachDung', label: 'Cách dùng' },
  { key: 'luuY', label: 'Lưu ý' },
  { key: 'baoQuan', label: 'Bảo quản' },
]

const selectedTab = ref('moTa')
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
