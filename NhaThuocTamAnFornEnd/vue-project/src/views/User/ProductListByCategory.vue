<template>
  <div>
    <Header />
    <Navbar />

    <main class="container py-4">
      <div class="row">
        <!-- BỘ LỌC TRÁI -->
        <div class="col-md-3 mb-4">
          <div class="border p-3 rounded bg-light shadow-sm">
            <h5 class="mb-3 d-flex justify-content-between align-items-center">
              <span><i class="bi bi-funnel-fill me-2"></i>Bộ lọc</span>
              <button class="btn btn-sm btn-outline-secondary" @click="boLocMoRong = !boLocMoRong">
                <i :class="boLocMoRong ? 'bi bi-chevron-up' : 'bi bi-chevron-down'"></i>
              </button>
            </h5>

            <!-- Thanh tìm kiếm -->
            <div class="mb-3">
              <input v-model="tuKhoa" class="form-control" placeholder="🔍 Tìm theo tên" />
            </div>

            <!-- Bộ lọc mở rộng -->
            <div v-if="boLocMoRong">
              <!-- Lọc theo giá -->
              <div class="mb-3">
                <label class="form-label fw-bold">Khoảng giá</label>
                <div class="form-check" v-for="(item, index) in giaOptions" :key="index">
                  <input
                    class="form-check-input"
                    type="radio"
                    name="gia"
                    :id="'gia' + index"
                    :value="item"
                    v-model="locGia"
                  />
                  <label class="form-check-label" :for="'gia' + index">{{ item.label }}</label>
                </div>
              </div>

              <!-- Xuất xứ -->
              <div class="mb-3">
                <label class="form-label fw-bold">Xuất xứ</label>
                <div class="form-check" v-for="(xx, index) in xuatXuOptions" :key="index">
                  <input
                    class="form-check-input"
                    type="checkbox"
                    :id="'xx' + index"
                    :value="xx"
                    v-model="locXuatXu"
                  />
                  <label class="form-check-label" :for="'xx' + index">{{ xx }}</label>
                </div>
              </div>

              <!-- Dạng bào chế -->
              <div class="mb-3">
                <label class="form-label fw-bold">Dạng bào chế</label>
                <div class="form-check" v-for="(dang, index) in dangOptions" :key="index">
                  <input
                    class="form-check-input"
                    type="checkbox"
                    :id="'dang' + index"
                    :value="dang"
                    v-model="locDang"
                  />
                  <label class="form-check-label" :for="'dang' + index">{{ dang }}</label>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- DANH SÁCH SẢN PHẨM PHẢI -->
        <div class="col-md-9">
          <!-- TIÊU ĐỀ -->
          <div class="d-flex justify-content-between align-items-center mb-3">
            <h4 class="mb-0 text-success">
              Danh mục: {{ tenDanhMuc }}
              <span class="text-muted fs-6">({{ sanPhamDaLoc.length }} sản phẩm)</span>
            </h4>
            <div>
              <span>Sắp xếp theo:</span>
              <button
                class="btn btn-outline-primary btn-sm ms-2"
                :class="{ active: sapXep === 'moiNhat' }"
                @click="sapXep = 'moiNhat'"
              >
                Bán chạy
              </button>
              <button
                class="btn btn-outline-secondary btn-sm ms-2"
                :class="{ active: sapXep === 'giaThap' }"
                @click="sapXep = 'giaThap'"
              >
                Giá thấp
              </button>
              <button
                class="btn btn-outline-secondary btn-sm ms-2"
                :class="{ active: sapXep === 'giaCao' }"
                @click="sapXep = 'giaCao'"
              >
                Giá cao
              </button>
            </div>
          </div>

          <!-- DANH MỤC CON DẠNG CARD -->
          <div v-if="danhMucCon.length" class="mb-4">
            <h5 class="fw-bold text-success mb-3">Danh mục con</h5>
            <div class="row g-3">
              <div v-for="child in danhMucCon" :key="child.maDM" class="col-md-4 col-lg-3">
                <div class="card h-100 shadow-sm hover-card">
                  <div class="card-body">
                    <!-- Tên danh mục -->
                    <router-link
                      :to="{ name: 'product-list-by-category', params: { maDM: child.maDM } }"
                      class="text-decoration-none fw-bold text-primary"
                    >
                      {{ child.tenDanhMuc }}
                    </router-link>

                    <!-- Số sản phẩm -->
                    <p class="text-muted small mb-2">{{ child.soLuongSanPham || 0 }} sản phẩm</p>

                    <!-- Danh mục con cấp 2 -->
                    <ul v-if="child.children?.length" class="list-unstyled small mb-0">
                      <li v-for="sub in child.children.slice(0, 4)" :key="sub.maDM">
                        <router-link
                          :to="{ name: 'product-list-by-category', params: { maDM: sub.maDM } }"
                          class="text-decoration-none text-dark"
                        >
                          • {{ sub.tenDanhMuc }}
                        </router-link>
                      </li>
                    </ul>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <!-- DANH SÁCH SẢN PHẨM -->
          <div class="row g-3">
            <router-link
              v-for="sp in sanPhamDaLoc.slice(0, soLuongHienThi)"
              :key="sp.maThuoc"
              :to="`/chi-tiet/${sp.maThuoc}`"
              class="col-lg-3 col-md-4 col-sm-6 text-decoration-none text-dark"
            >
              <div class="card h-100 shadow-sm product-card">
                <div class="position-relative image-wrapper">
                  <img :src="getFullImage(sp.hinhAnhChinh)" class="product-img" />
                </div>
                <div class="card-body d-flex flex-column">
                  <h6 class="card-title">{{ sp.tenThuoc }}</h6>
                  <p class="mb-1 price-green">{{ formatCurrency(sp.giaBan) }}</p>
                  <button class="btn btn-success w-100 mt-auto">Mua ngay</button>
                </div>
              </div>
            </router-link>
          </div>

          <!-- NÚT XEM THÊM -->
          <div v-if="conSanPhamChuaHien" class="text-center mt-4">
            <button class="btn btn-outline-success" @click="soLuongHienThi += 8">
              Xem thêm {{ sanPhamDaLoc.length - soLuongHienThi }} sản phẩm
            </button>
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
  name: 'ProductListByCategory',
  components: { Header, Navbar, Footer },
  data() {
    return {
      sanPhamList: [],
      soLuongHienThi: 12,
      sapXep: 'moiNhat',
      boLocMoRong: true,
      tuKhoa: '',
      locGia: null,
      locXuatXu: [],
      locDang: [],
      giaOptions: [
        { label: 'Tất cả', min: 0, max: Infinity },
        { label: 'Dưới 100.000đ', min: 0, max: 100000 },
        { label: '100.000đ - 300.000đ', min: 100000, max: 300000 },
        { label: '300.000đ - 500.000đ', min: 300000, max: 500000 },
        { label: 'Trên 500.000đ', min: 500000, max: Infinity },
      ],
      xuatXuOptions: [],
      dangOptions: [],
      tenDanhMuc: '',
      danhMucCon: [], // ✅ hiển thị danh mục con dạng card
    }
  },
  computed: {
    sanPhamSapXep() {
      const list = [...this.sanPhamList]
      if (this.sapXep === 'giaThap') return list.sort((a, b) => a.giaBan - b.giaBan)
      if (this.sapXep === 'giaCao') return list.sort((a, b) => b.giaBan - a.giaBan)
      return list
    },
    sanPhamDaLoc() {
      return this.sanPhamSapXep.filter((sp) => {
        const keyword = this.tuKhoa.toLowerCase()
        const tuKhoaOK =
          !keyword ||
          sp.tenThuoc?.toLowerCase().includes(keyword) ||
          sp.thanhPhan?.toLowerCase().includes(keyword)

        const giaOK = !this.locGia || (sp.giaBan >= this.locGia.min && sp.giaBan <= this.locGia.max)
        const xuatXuOK = this.locXuatXu.length === 0 || this.locXuatXu.includes(sp.xuatXu)
        const dangOK = this.locDang.length === 0 || this.locDang.includes(sp.dangBaoChe)

        return giaOK && xuatXuOK && dangOK && tuKhoaOK
      })
    },
    conSanPhamChuaHien() {
      return this.soLuongHienThi < this.sanPhamDaLoc.length
    },
  },
  methods: {
    async fetchSanPhamTheoDanhMuc(maDM) {
      try {
        // ✅ lấy tên danh mục cha
        const resDM = await fetch(`http://localhost:8080/api/danhmuc/${maDM}`)
        const dm = await resDM.json()
        this.tenDanhMuc = dm.tenDanhMuc

        // ✅ lấy tree danh mục để tìm con
        const resTree = await fetch(`http://localhost:8080/api/danhmuc/tree`)
        const allTree = await resTree.json()

        const findNode = (nodes, id) => {
          for (const node of nodes) {
            if (node.maDM === id) return node
            const found = findNode(node.children || [], id)
            if (found) return found
          }
          return null
        }

        const currentNode = findNode(allTree, maDM)
        this.danhMucCon = currentNode ? currentNode.children || [] : []

        // ✅ gọi API lấy sản phẩm theo cha + con
        const res = await fetch(`http://localhost:8080/api/thuoc/by-danhmuc-va-con/${maDM}`)
        const data = await res.json()
        this.sanPhamList = data

        // chuẩn hóa bộ lọc
        const clean = (str) => str?.trim().normalize('NFC').replace(/\s+/g, ' ') || ''
        this.xuatXuOptions = [...new Set(data.map((sp) => clean(sp.xuatXu)).filter((x) => x))].sort(
          (a, b) => a.localeCompare(b, 'vi'),
        )
        this.dangOptions = [
          ...new Set(data.map((sp) => clean(sp.dangBaoChe)).filter((x) => x)),
        ].sort((a, b) => a.localeCompare(b, 'vi'))
      } catch (err) {
        console.error('Lỗi khi lấy danh sách thuốc:', err)
      }
    },
    getFullImage(path) {
      if (!path) return 'https://via.placeholder.com/150'
      return `http://localhost:8080${path.startsWith('/') ? path : '/' + path}`
    },
    formatCurrency(value) {
      return Number(value).toLocaleString('vi-VN') + 'đ'
    },
  },
  mounted() {
    this.fetchSanPhamTheoDanhMuc(this.$route.params.maDM)
  },
  watch: {
    '$route.params.maDM'(newVal) {
      this.fetchSanPhamTheoDanhMuc(newVal)
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
.price-green {
  color: #2e7d32;
  font-weight: bold;
}
button.active {
  font-weight: bold;
  border-color: #28a745 !important;
  color: #28a745 !important;
}
.form-check-input:checked {
  background-color: #28a745;
  border-color: #28a745;
}
input::placeholder {
  font-style: italic;
  color: #999;
}
.hover-card:hover {
  border-color: #28a745;
  transform: translateY(-3px);
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
}
</style>
