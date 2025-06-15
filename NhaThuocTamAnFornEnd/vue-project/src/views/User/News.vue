<template>
  <section class="py-4">
    <div class="container">
      <h2 class="text-success fw-bold mb-4">📰 Tin tức y tế</h2>
      <div class="row">
        <div class="col-md-4" v-for="news in newsList" :key="news.maTin">
          <div class="card news-card shadow-sm mb-4">
            <img :src="news.hinhAnh" class="card-img-top" :alt="news.tieuDe" />
            <div class="card-body">
              <p class="text-muted small mb-1">
                <i class="bi bi-calendar2-week me-1"></i>{{ news.ngayDang }}
              </p>
              <h5 class="card-title">{{ news.tieuDe }}</h5>
              <p class="card-text text-truncate">{{ news.moTa }}</p>
              <router-link :to="`/tin-tuc/${news.maTin}`" class="btn btn-sm btn-success">
                Đọc thêm
              </router-link>
            </div>
          </div>
        </div>
      </div>
    </div>
  </section>
</template>

<script setup>
const newsList = [
  {
    maTin: '1',
    tieuDe: '5 Cách tăng sức đề kháng hiệu quả',
    moTa: 'Giữ sức khỏe tốt vào mùa dịch với những cách đơn giản từ chuyên gia...',
    ngayDang: '20/05/2025',
    hinhAnh:
      'https://cdn.nhathuoclongchau.com.vn/unsafe/257x152/https://cms-prod.s3-sgn09.fptcloud.com/cum_9666d8fbb3.png',
  },
  {
    maTin: '2',
    tieuDe: 'Cảnh báo bệnh tay chân miệng trở lại',
    moTa: 'Số ca tăng nhanh ở trẻ em, cách phòng ngừa và xử lý...',
    ngayDang: '18/05/2025',
    hinhAnh:
      'https://cdn.nhathuoclongchau.com.vn/unsafe/257x152/https://cms-prod.s3-sgn09.fptcloud.com/tay_chan_mieng_a162a1e04b.png',
  },
  {
    maTin: '3',
    tieuDe: 'Ebola tái bùng phát: Việt Nam cần làm gì?',
    moTa: 'Khuyến cáo từ Bộ Y Tế về các biện pháp phòng dịch...',
    ngayDang: '15/05/2025',
    hinhAnh:
      'https://cdn.nhathuoclongchau.com.vn/unsafe/257x152/https://cms-prod.s3-sgn09.fptcloud.com/ebola_073e9acd59.png',
  },
]
</script>

<style scoped>
.news-card {
  border-radius: 12px;
  overflow: hidden;
  transition: all 0.3s ease;
  border: 1px solid #eee;
}
.news-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 24px rgba(0, 123, 255, 0.15);
  border-color: #4caf50;
}
.card-img-top {
  height: 160px;
  object-fit: cover;
}
</style>
