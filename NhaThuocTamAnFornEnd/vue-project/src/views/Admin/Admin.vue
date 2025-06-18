<template>
  <div class="container-fluid">
    <h4 class="mb-4 text-success">📊 Dashboard</h4>

    <!-- Cards thống kê -->
    <div class="row g-4 mb-4">
      <div class="col-md-3" v-for="card in cards" :key="card.title">
        <div class="card shadow-sm border-0 h-100">
          <div class="card-body d-flex align-items-center justify-content-between">
            <div>
              <h6 class="text-muted">{{ card.title }}</h6>
              <h5 class="fw-bold">{{ card.value }}</h5>
            </div>
            <i :class="card.icon" class="fs-2 text-success"></i>
          </div>
        </div>
      </div>
    </div>

    <!-- Cảnh báo tồn kho thấp -->
    <div v-if="productsLowStock.length" class="alert alert-warning d-flex align-items-start gap-3">
      <i class="bi bi-exclamation-triangle-fill fs-4 text-warning"></i>
      <div>
        <strong>⚠️ Cảnh báo tồn kho thấp:</strong>
        <ul class="mb-0 mt-1 small ps-3">
          <li v-for="sp in productsLowStock" :key="sp.ten">
            {{ sp.ten }} – còn lại <strong>{{ sp.soLuong }}</strong> sản phẩm
          </li>
        </ul>
      </div>
    </div>

    <!-- Biểu đồ doanh thu -->
    <div class="card shadow-sm border-0 mb-4">
      <div class="card-header bg-white fw-bold">📈 Doanh thu 7 ngày gần nhất</div>
      <div class="card-body" style="height: 320px">
        <Bar :data="chartData" :options="chartOptions" />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { Bar } from 'vue-chartjs'
import {
  Chart as ChartJS,
  Title,
  Tooltip,
  Legend,
  BarElement,
  CategoryScale,
  LinearScale,
} from 'chart.js'

ChartJS.register(Title, Tooltip, Legend, BarElement, CategoryScale, LinearScale)

const cards = ref([
  { title: 'Tổng sản phẩm', value: 128, icon: 'bi bi-capsule' },
  { title: 'Đơn hàng hôm nay', value: 24, icon: 'bi bi-bag-check' },
  { title: 'Khách hàng', value: 320, icon: 'bi bi-people' },
  { title: 'Doanh thu hôm nay', value: '12,400,000₫', icon: 'bi bi-currency-dollar' },
])

const productsLowStock = ref([
  { ten: 'Paracetamol 500mg', soLuong: 3 },
  { ten: 'Vitamin C 1000mg', soLuong: 7 },
  { ten: 'Thuốc ho Prospan', soLuong: 2 },
])

const chartData = ref({
  labels: ['18/05', '19/05', '20/05', '21/05', '22/05', '23/05', '24/05'],
  datasets: [
    {
      label: 'Doanh thu (VNĐ)',
      backgroundColor: '#4caf50',
      data: [3200000, 4500000, 3900000, 5200000, 6100000, 4800000, 7400000],
    },
  ],
})

const chartOptions = ref({
  responsive: true,
  maintainAspectRatio: false,
  plugins: {
    legend: { display: true },
  },
  scales: {
    y: {
      ticks: {
        callback: (value) => value.toLocaleString('vi-VN') + ' ₫',
      },
    },
  },
})
</script>

<style scoped>
.container-fluid {
  background-color: #f8f9fa;
  padding: 24px;
  border-radius: 16px;
}

.card {
  border-radius: 16px;
  transition:
    transform 0.2s ease,
    box-shadow 0.2s ease;
}

.card:hover {
  transform: translateY(-4px);
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.08);
}

.card-body {
  padding: 20px;
  background-color: white;
  border-radius: 0 0 16px 16px;
}

.card-body h6 {
  font-size: 0.9rem;
  color: #6c757d;
  margin-bottom: 4px;
}

.card-body h5 {
  font-size: 1.4rem;
  margin: 0;
  color: #2e7d32;
}

.card-body i {
  font-size: 2rem;
  color: #4caf50;
  opacity: 0.9;
}

.card-header {
  background-color: #e8f5e9;
  border-bottom: 1px solid #c8e6c9;
  font-size: 1.1rem;
  color: #2e7d32;
}

.alert ul {
  margin-bottom: 0;
}
</style>
