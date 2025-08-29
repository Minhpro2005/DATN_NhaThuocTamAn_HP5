<template>
  <div class="container mt-4">
    <h4 class="fw-bold mb-4 text-success">
      <i class="bi bi-list-ul me-2"></i> Quản lý Danh mục hỗ trợ
    </h4>

    <!-- Tabs -->
    <ul class="nav nav-tabs mb-4" role="tablist">
      <li class="nav-item">
        <button
          class="nav-link active fw-semibold"
          data-bs-toggle="tab"
          data-bs-target="#donViTinhTab"
          type="button"
        >
          📏 Đơn vị tính
        </button>
      </li>
      <li class="nav-item">
        <button
          class="nav-link fw-semibold"
          data-bs-toggle="tab"
          data-bs-target="#quyCachTab"
          type="button"
        >
          📦 Quy cách đóng gói
        </button>
      </li>
    </ul>

    <div class="tab-content">
      <!-- Đơn vị tính -->
      <div class="tab-pane fade show active" id="donViTinhTab">
        <div class="d-flex justify-content-between mb-3">
          <input
            v-model="searchDVT"
            class="form-control w-50"
            placeholder="Tìm theo mã hoặc tên đơn vị..."
          />
          <div class="d-flex gap-2">
            <button class="btn btn-success" @click="openModalDVT()">
              <i class="bi bi-plus-circle me-1"></i> Thêm đơn vị
            </button>
            <button class="btn btn-outline-secondary" @click="toggleDVTDeleted">
              {{ hienThiDaXoaDVT ? '📂 Xem đang hoạt động' : '🗑️ Xem đã xóa' }}
            </button>
          </div>
        </div>

        <div class="card shadow-sm border-0">
          <div class="card-body p-0">
            <table class="table table-bordered table-hover text-center mb-0">
              <thead class="table-success">
                <tr>
                  <th style="width: 80px">Mã</th>
                  <th class="text-start">Tên đơn vị</th>
                  <th style="width: 150px">Hành động</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="dvt in pagedDVT" :key="dvt.maDVT">
                  <td class="fw-bold text-success">#{{ dvt.maDVT }}</td>
                  <td class="text-start">{{ dvt.ten }}</td>
                  <td>
                    <div class="d-flex justify-content-center gap-2">
                      <template v-if="!hienThiDaXoaDVT">
                        <button class="btn btn-warning btn-sm" @click="openModalDVT(dvt)">
                          <i class="bi bi-pencil"></i>
                        </button>
                        <button class="btn btn-danger btn-sm" @click="xoaDVT(dvt.maDVT)">
                          <i class="bi bi-trash"></i>
                        </button>
                      </template>
                      <template v-else>
                        <button class="btn btn-success btn-sm" @click="khoiPhucDVT(dvt.maDVT)">
                          <i class="bi bi-arrow-counterclockwise"></i>
                        </button>
                      </template>
                    </div>
                  </td>
                </tr>
                <tr v-if="filteredDVT.length === 0">
                  <td colspan="3" class="text-muted py-3">
                    <i class="bi bi-inbox me-1"></i> Không có dữ liệu
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>

        <Pagination
          :current-page="currentPageDVT"
          :total-pages="totalPagesDVT"
          @change-page="changePageDVT"
        />
      </div>

      <!-- Quy cách -->
      <div class="tab-pane fade" id="quyCachTab">
        <div class="d-flex justify-content-between mb-3">
          <input
            v-model="searchQCDG"
            class="form-control w-50"
            placeholder="Tìm theo mã hoặc mô tả quy cách..."
          />
          <div class="d-flex gap-2">
            <button class="btn btn-primary" @click="openModalQCDG()">
              <i class="bi bi-plus-circle me-1"></i> Thêm quy cách
            </button>
            <button class="btn btn-outline-secondary" @click="toggleQCDGDeleted">
              {{ hienThiDaXoaQCDG ? '📂 Xem đang hoạt động' : '🗑️ Xem đã xóa' }}
            </button>
          </div>
        </div>

        <div class="card shadow-sm border-0">
          <div class="card-body p-0">
            <table class="table table-bordered table-hover text-center mb-0">
              <thead class="table-primary">
                <tr>
                  <th style="width: 80px">Mã</th>
                  <th class="text-start">Quy cách</th>
                  <th style="width: 150px">Hành động</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="qc in pagedQCDG" :key="qc.maQCDG">
                  <td class="fw-bold text-primary">#{{ qc.maQCDG }}</td>
                  <td class="text-start">{{ qc.moTa }}</td>
                  <td>
                    <div class="d-flex justify-content-center gap-2">
                      <template v-if="!hienThiDaXoaQCDG">
                        <button class="btn btn-warning btn-sm" @click="openModalQCDG(qc)">
                          <i class="bi bi-pencil"></i>
                        </button>
                        <button class="btn btn-danger btn-sm" @click="xoaQCDG(qc.maQCDG)">
                          <i class="bi bi-trash"></i>
                        </button>
                      </template>
                      <template v-else>
                        <button class="btn btn-success btn-sm" @click="khoiPhucQCDG(qc.maQCDG)">
                          <i class="bi bi-arrow-counterclockwise"></i>
                        </button>
                      </template>
                    </div>
                  </td>
                </tr>
                <tr v-if="filteredQCDG.length === 0">
                  <td colspan="3" class="text-muted py-3">
                    <i class="bi bi-inbox me-1"></i> Không có dữ liệu
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>

        <Pagination
          :current-page="currentPageQCDG"
          :total-pages="totalPagesQCDG"
          @change-page="changePageQCDG"
        />
      </div>
    </div>

    <!-- Modal Đơn vị tính -->
    <div class="modal fade" id="modalDVT" tabindex="-1">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header bg-success text-white">
            <h5 class="modal-title">
              {{ dvtForm.maDVT ? 'Sửa đơn vị tính' : 'Thêm đơn vị tính' }}
            </h5>
            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
          </div>
          <div class="modal-body">
            <input v-model="dvtForm.ten" class="form-control" placeholder="Nhập tên đơn vị..." />
          </div>
          <div class="modal-footer">
            <button class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
            <button class="btn btn-success" @click="luuDVT" data-bs-dismiss="modal">Lưu</button>
          </div>
        </div>
      </div>
    </div>

    <!-- Modal Quy cách -->
    <div class="modal fade" id="modalQCDG" tabindex="-1">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header bg-primary text-white">
            <h5 class="modal-title">{{ qcdgForm.maQCDG ? 'Sửa quy cách' : 'Thêm quy cách' }}</h5>
            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
          </div>
          <div class="modal-body">
            <input v-model="qcdgForm.moTa" class="form-control" placeholder="Nhập mô tả..." />
          </div>
          <div class="modal-footer">
            <button class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
            <button class="btn btn-primary" @click="luuQCDG" data-bs-dismiss="modal">Lưu</button>
          </div>
        </div>
      </div>
    </div>

    <ToastMessage ref="toast" />
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import ToastMessage from '../ToastMessage.vue'
import Pagination from '../Pagination.vue'

const toast = ref(null)

// ====== Đơn vị tính ======
const danhSachDVT = ref([])
const searchDVT = ref('')
const hienThiDaXoaDVT = ref(false)
const dvtForm = ref({ maDVT: null, ten: '' })

// ====== Quy cách ======
const danhSachQCDG = ref([])
const searchQCDG = ref('')
const hienThiDaXoaQCDG = ref(false)
const qcdgForm = ref({ maQCDG: null, moTa: '' })

// ================== FETCH ==================
const fetchDVT = () => {
  const url = hienThiDaXoaDVT.value
    ? 'http://localhost:8080/api/donvitinh/deleted'
    : 'http://localhost:8080/api/donvitinh'
  fetch(url)
    .then((res) => res.json())
    .then((data) => (danhSachDVT.value = data))
}

const fetchQCDG = () => {
  const url = hienThiDaXoaQCDG.value
    ? 'http://localhost:8080/api/quycachdonggoi/deleted'
    : 'http://localhost:8080/api/quycachdonggoi'
  fetch(url)
    .then((res) => res.json())
    .then((data) => (danhSachQCDG.value = data))
}

// ================== OPEN MODAL ==================
const openModalDVT = (dvt = null) => {
  dvtForm.value = dvt ? { ...dvt } : { maDVT: null, ten: '' }
  new bootstrap.Modal(document.getElementById('modalDVT')).show()
}

const openModalQCDG = (qc = null) => {
  qcdgForm.value = qc ? { ...qc } : { maQCDG: null, moTa: '' }
  new bootstrap.Modal(document.getElementById('modalQCDG')).show()
}

// ================== LƯU ==================
const luuDVT = () => {
  const url = dvtForm.value.maDVT
    ? `http://localhost:8080/api/donvitinh/${dvtForm.value.maDVT}`
    : 'http://localhost:8080/api/donvitinh'
  const method = dvtForm.value.maDVT ? 'PUT' : 'POST'

  fetch(url, {
    method,
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(dvtForm.value),
  })
    .then((res) => {
      if (!res.ok) throw new Error('Tên đơn vị đã tồn tại')
      return res.json()
    })
    .then(() => {
      toast.value.show('✅ Lưu thành công!', 'success')
      fetchDVT()
    })
    .catch((err) => toast.value.show('❌ ' + err.message, 'error'))
}

const luuQCDG = () => {
  const url = qcdgForm.value.maQCDG
    ? `http://localhost:8080/api/quycachdonggoi/${qcdgForm.value.maQCDG}`
    : 'http://localhost:8080/api/quycachdonggoi'
  const method = qcdgForm.value.maQCDG ? 'PUT' : 'POST'

  fetch(url, {
    method,
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(qcdgForm.value),
  })
    .then((res) => {
      if (!res.ok) throw new Error('Mô tả đã tồn tại')
      return res.json()
    })
    .then(() => {
      toast.value.show('✅ Lưu thành công!', 'success')
      fetchQCDG()
    })
    .catch((err) => toast.value.show('❌ ' + err.message, 'error'))
}

// ================== XOÁ & KHÔI PHỤC ==================
const xoaDVT = (id) => {
  if (!confirm('Bạn có chắc chắn muốn ẩn?')) return
  fetch(`http://localhost:8080/api/donvitinh/${id}`, { method: 'DELETE' }).then(() => {
    toast.value.show('🚫 Đã ẩn đơn vị tính', 'success')
    fetchDVT()
  })
}

const khoiPhucDVT = (id) => {
  fetch(`http://localhost:8080/api/donvitinh/restore/${id}`, { method: 'PUT' }).then(() => {
    toast.value.show('✅ Khôi phục thành công', 'success')
    fetchDVT()
  })
}

const xoaQCDG = (id) => {
  if (!confirm('Bạn có chắc chắn muốn ẩn?')) return
  fetch(`http://localhost:8080/api/quycachdonggoi/${id}`, { method: 'DELETE' }).then(() => {
    toast.value.show('🚫 Đã ẩn quy cách', 'success')
    fetchQCDG()
  })
}

const khoiPhucQCDG = (id) => {
  fetch(`http://localhost:8080/api/quycachdonggoi/restore/${id}`, { method: 'PUT' }).then(() => {
    toast.value.show('✅ Khôi phục thành công', 'success')
    fetchQCDG()
  })
}

// ================== PHÂN TRANG ==================
const filteredDVT = computed(() =>
  danhSachDVT.value.filter(
    (d) =>
      d.ten.toLowerCase().includes(searchDVT.value.toLowerCase()) ||
      d.maDVT.toString().includes(searchDVT.value),
  ),
)
const currentPageDVT = ref(1)
const pageSizeDVT = 5
const totalPagesDVT = computed(() => Math.ceil(filteredDVT.value.length / pageSizeDVT))
const pagedDVT = computed(() => {
  const start = (currentPageDVT.value - 1) * pageSizeDVT
  return filteredDVT.value.slice(start, start + pageSizeDVT)
})
const changePageDVT = (page) => (currentPageDVT.value = page)

const filteredQCDG = computed(() =>
  danhSachQCDG.value.filter(
    (q) =>
      q.moTa.toLowerCase().includes(searchQCDG.value.toLowerCase()) ||
      q.maQCDG.toString().includes(searchQCDG.value),
  ),
)
const currentPageQCDG = ref(1)
const pageSizeQCDG = 5
const totalPagesQCDG = computed(() => Math.ceil(filteredQCDG.value.length / pageSizeQCDG))
const pagedQCDG = computed(() => {
  const start = (currentPageQCDG.value - 1) * pageSizeQCDG
  return filteredQCDG.value.slice(start, start + pageSizeQCDG)
})
const changePageQCDG = (page) => (currentPageQCDG.value = page)

// ================== TOGGLE VIEW ==================
const toggleDVTDeleted = () => {
  hienThiDaXoaDVT.value = !hienThiDaXoaDVT.value
  fetchDVT()
}

const toggleQCDGDeleted = () => {
  hienThiDaXoaQCDG.value = !hienThiDaXoaQCDG.value
  fetchQCDG()
}

// ================== INIT ==================
onMounted(() => {
  fetchDVT()
  fetchQCDG()
})
</script>
