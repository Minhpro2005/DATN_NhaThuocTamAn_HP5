<template>
  <div>
    <Header />
    <Navbar />

    <main class="container py-4">
      <div class="row">
        <!-- Bộ lọc -->
        <div class="col-md-3">
          <div class="border rounded p-3 bg-light">
            <h5 class="mb-3"><i class="bi bi-funnel-fill me-2"></i>Bộ lọc nâng cao</h5>
            <div v-for="(boLoc, index) in boLocs" :key="index" class="mb-3">
              <button
                class="btn btn-link text-dark p-0 w-100 text-start"
                @click="toggleBoLoc(index)"
              >
                {{ boLoc.tieuDe }}
                <i
                  :class="boLoc.moRong ? 'bi bi-chevron-up' : 'bi bi-chevron-down'"
                  class="float-end"
                ></i>
              </button>
              <div v-if="boLoc.moRong" class="mt-2">
                <div v-for="(item, i) in boLoc.options" :key="i">
                  <input type="checkbox" class="form-check-input me-2" />{{ item }}
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- Danh sách sản phẩm -->
        <div class="col-md-9">
          <div class="d-flex justify-content-between align-items-center mb-3">
            <h4 class="mb-0">Vitamin và khoáng chất</h4>
            <div>
              <span>Sắp xếp theo:</span>
              <button class="btn btn-outline-primary btn-sm ms-2">Bán chạy</button>
              <button class="btn btn-outline-secondary btn-sm ms-2">Giá thấp</button>
              <button class="btn btn-outline-secondary btn-sm ms-2">Giá cao</button>
            </div>
          </div>

          <div class="row g-3">
            <div v-for="sp in sanPhamList" :key="sp.id" class="col-lg-3 col-md-4 col-sm-6">
              <div class="card h-100 shadow-sm product-card d-flex flex-column">
                <div class="position-relative image-wrapper">
                  <img :src="sp.hinhAnh" class="product-img" />
                  <span v-if="sp.giamGia" class="badge bg-danger discount-badge"
                    >-{{ sp.giamGia }}%</span
                  >
                </div>
                <div class="card-body d-flex flex-column">
                  <h6 class="card-title">{{ sp.ten }}</h6>
                  <p class="mb-1 text-danger fw-bold">{{ sp.giaMoi }}đ</p>
                  <p v-if="sp.giaCu" class="text-muted text-decoration-line-through small">
                    {{ sp.giaCu }}đ
                  </p>
                  <button class="btn btn-primary w-100 mt-auto">Chọn mua</button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </main>

    <Footer />
  </div>
</template>

<script>
import Header from '../User/Header.vue'
import Navbar from '../User/Navbar.vue'
import Footer from '../User/Footer.vue'

export default {
  name: 'VitaminDanhMuc',
  components: { Header, Navbar, Footer },
  data() {
    return {
      boLocs: [
        {
          tieuDe: 'Đối tượng sử dụng',
          moRong: false,
          options: ['Trẻ em', 'Người lớn', 'Phụ nữ mang thai'],
        },
        { tieuDe: 'Giá bán', moRong: false, options: ['Dưới 100k', '100k - 300k', 'Trên 300k'] },
        { tieuDe: 'Mùi vị/ Mùi hương', moRong: false, options: ['Cam', 'Dâu', 'Không mùi'] },
        { tieuDe: 'Nước sản xuất', moRong: false, options: ['Việt Nam', 'Úc', 'Mỹ', 'Đức'] },
        { tieuDe: 'Chỉ định', moRong: false, options: ['Bổ sung canxi', 'Tăng sức đề kháng'] },
        { tieuDe: 'Thương hiệu', moRong: false, options: ['Kudos', 'Brauer', 'Easylife'] },
        {
          tieuDe: 'Xuất xứ thương hiệu',
          moRong: false,
          options: ['Australia', 'Germany', 'Vietnam'],
        },
      ],
      sanPhamList: [
        {
          id: 1,
          ten: 'Viên uống Immuvita Easylife bổ sung vitamin...',
          hinhAnh:
            'https://cdn.nhathuoclongchau.com.vn/unsafe/144x0/filters:quality(90)/https://cms-prod.s3-sgn09.fptcloud.com/DSC_05505_4c243a16f9.jpg',
          giamGia: 20,
          giaMoi: '312.000',
          giaCu: '390.000',
        },
        {
          id: 2,
          ten: 'Siro Canxi-D3-K2 Kingphar bổ sung cho bé',
          hinhAnh:
            'https://cdn.nhathuoclongchau.com.vn/unsafe/375x0/filters:quality(90)/https://cms-prod.s3-sgn09.fptcloud.com/DSC_04874_6c29236c37.jpg',
          giamGia: 0,
          giaMoi: '105.000',
          giaCu: '',
        },
        {
          id: 3,
          ten: 'Siro Brauer Baby Kids D3 + K2 hỗ trợ xương',
          hinhAnh:
            'https://cdn.nhathuoclongchau.com.vn/unsafe/144x0/filters:quality(90)/https://cms-prod.s3-sgn09.fptcloud.com/DSC_04996_7d25dc3860.jpg',
          giamGia: 0,
          giaMoi: '396.000',
          giaCu: '',
        },
        {
          id: 4,
          ten: 'Viên sủi Kudos Vitamin C giúp tăng đề kháng',
          hinhAnh:
            'https://cdn.nhathuoclongchau.com.vn/unsafe/144x0/filters:quality(90)/https://cms-prod.s3-sgn09.fptcloud.com/DSC_09335_24b9811179.jpg',
          giamGia: 15,
          giaMoi: '107.000',
          giaCu: '126.000',
        },
        {
          id: 5,
          ten: 'Viên uống Immuvita Easylife bổ sung vitamin...',
          hinhAnh:
            'https://cdn.nhathuoclongchau.com.vn/unsafe/144x0/filters:quality(90)/https://cms-prod.s3-sgn09.fptcloud.com/DSC_00115_72e83dcffe.jpg',
          giamGia: 20,
          giaMoi: '312.000',
          giaCu: '390.000',
        },
        {
          id: 6,
          ten: 'Siro Canxi-D3-K2 Kingphar bổ sung cho bé',
          hinhAnh:
            'https://cdn.nhathuoclongchau.com.vn/unsafe/375x0/filters:quality(90)/https://cms-prod.s3-sgn09.fptcloud.com/DSC_04874_6c29236c37.jpg',
          giamGia: 0,
          giaMoi: '105.000',
          giaCu: '',
        },
        {
          id: 7,
          ten: 'Siro Brauer Baby Kids D3 + K2 hỗ trợ xương',
          hinhAnh:
            'https://cdn.nhathuoclongchau.com.vn/unsafe/144x0/filters:quality(90)/https://cms-prod.s3-sgn09.fptcloud.com/DSC_04996_7d25dc3860.jpg',
          giamGia: 0,
          giaMoi: '396.000',
          giaCu: '',
        },
        {
          id: 8,
          ten: 'Viên sủi Kudos Vitamin C giúp tăng đề kháng',
          hinhAnh:
            'https://cdn.nhathuoclongchau.com.vn/unsafe/144x0/filters:quality(90)/https://cms-prod.s3-sgn09.fptcloud.com/DSC_09335_24b9811179.jpg',
          giamGia: 15,
          giaMoi: '107.000',
          giaCu: '126.000',
        },
      ],
    }
  },
  methods: {
    toggleBoLoc(index) {
      this.boLocs[index].moRong = !this.boLocs[index].moRong
    },
  },
}
</script>

<style scoped>
.card-title {
  font-size: 0.95rem;
  height: 2.8em;
  overflow: hidden;
  line-height: 1.4;
}

.product-card {
  transition:
    transform 0.2s ease-in-out,
    border-color 0.2s;
  border-radius: 10px;
  overflow: hidden;
  display: flex;
  flex-direction: column;
  border: 1px solid #e0e0e0;
  background-color: #fff;
}

.product-card:hover {
  transform: translateY(-5px);
  border-color: #28a745;
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.1);
}

.image-wrapper {
  height: 180px;
  display: flex;
  justify-content: center;
  align-items: center;
  background: #fafafa;
  position: relative;
}

.product-img {
  max-height: 100%;
  max-width: 100%;
  object-fit: contain;
  transition: transform 0.3s ease;
}

.product-card:hover .product-img {
  transform: scale(1.05);
}

.discount-badge {
  position: absolute;
  top: 10px;
  left: 10px;
  padding: 5px 10px;
  font-size: 0.75rem;
  border-radius: 20px;
}

.card-body {
  display: flex;
  flex-direction: column;
  flex: 1;
  padding: 12px 16px;
}

.card-body button {
  margin-top: auto;
  font-weight: 500;
  font-size: 0.9rem;
  padding: 8px 12px;
  border-radius: 6px;
}

.btn-link {
  text-decoration: none !important;
  color: #000 !important;
}

.btn-link:hover {
  color: #28a745 !important;
}
</style>
