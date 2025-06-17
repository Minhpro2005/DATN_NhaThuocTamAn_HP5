<template>
  <div class="container py-5">
    <div class="card shadow rounded-4 p-4">
      <h4 class="mb-4 text-success">🩺 Gợi ý thuốc theo triệu chứng</h4>

      <!-- Nhập triệu chứng -->
      <div class="mb-3">
        <label class="form-label fw-semibold">Triệu chứng bạn đang gặp:</label>
        <input
          v-model="trieuChungNhap"
          class="form-control"
          placeholder="Ví dụ: ho, dau dau, met moi..."
          @keyup.enter="timThuoc"
        />
      </div>

      <button class="btn btn-success w-100 mb-4" @click="timThuoc">🔍 Tìm thuốc phù hợp</button>

      <!-- Kết quả -->
      <div v-if="daTim">
        <div v-if="thuocGoiY.length">
          <h5 class="text-primary mb-3">📋 Kết quả tìm được:</h5>
          <div class="row g-3">
            <div class="col-md-6" v-for="thuoc in thuocGoiY" :key="thuoc.ten">
              <div class="card h-100 shadow-sm border-0">
                <div class="row g-0">
                  <div class="col-4">
                    <img :src="thuoc.image" alt="ảnh thuốc" class="img-fluid rounded-start" />
                  </div>
                  <div class="col-8">
                    <div class="card-body">
                      <h6 class="card-title mb-1 text-success">
                        <i class="bi bi-capsule me-1"></i> {{ thuoc.ten }}
                      </h6>
                      <p class="card-text small text-muted">{{ thuoc.moTa }}</p>
                      <div class="d-flex flex-wrap gap-1 mb-2">
                        <span
                          class="badge bg-light text-dark border"
                          v-for="tc in thuoc.trieuChung"
                          :key="tc"
                          >{{ tc }}</span
                        >
                      </div>
                      <button
                        class="btn btn-outline-primary btn-sm"
                        @click="chonSoSanh(thuoc)"
                        :disabled="thuocSoSanh.includes(thuoc) || thuocSoSanh.length >= 2"
                      >
                        🔄 So sánh
                      </button>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <button
            class="btn btn-primary w-100 mt-3"
            data-bs-toggle="modal"
            data-bs-target="#soSanhModal"
            v-if="thuocSoSanh.length === 2"
          >
            🧪 So sánh 2 thuốc đã chọn
          </button>
        </div>
        <div v-else class="alert alert-warning mt-3">
          ❌ Không tìm thấy thuốc phù hợp với triệu chứng bạn nhập.
        </div>
      </div>
    </div>

    <!-- Modal So Sánh -->
    <div class="modal fade" id="soSanhModal" tabindex="-1" aria-hidden="true">
      <div class="modal-dialog modal-xl modal-dialog-centered">
        <div class="modal-content rounded-4">
          <div class="modal-header bg-light">
            <h5 class="modal-title text-success">🧪 So sánh thuốc</h5>
            <button
              type="button"
              class="btn-close"
              data-bs-dismiss="modal"
              aria-label="Đóng"
            ></button>
          </div>
          <div class="modal-body">
            <div class="row text-center">
              <div class="col-md-6" v-for="thuoc in thuocSoSanh" :key="thuoc.ten">
                <img :src="thuoc.image" class="img-fluid mb-2" style="max-height: 120px" />
                <h6 class="text-success">{{ thuoc.ten }}</h6>
                <p class="small text-muted">{{ thuoc.moTa }}</p>
                <div class="d-flex flex-wrap justify-content-center gap-1">
                  <span class="badge bg-secondary" v-for="tc in thuoc.trieuChung" :key="tc">{{
                    tc
                  }}</span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, watch } from 'vue'

const thuocList = [
  {
    ten: 'Paracetamol',
    trieuChung: ['sốt', 'đau đầu', 'nhức mỏi'],
    moTa: 'Thuốc giảm đau, hạ sốt thông dụng.',
    image:
      'https://cdn.nhathuoclongchau.com.vn/unsafe/144x0/filters:quality(90)/https://cms-prod.s3-sgn09.fptcloud.com/DSC_03594_d9d39b4d3f.jpg',
  },
  {
    ten: 'Prospan',
    trieuChung: ['ho', 'viêm họng', 'khó thở'],
    moTa: 'Thuốc ho thảo dược, dùng được cho trẻ em.',
    image:
      'https://cdn.nhathuoclongchau.com.vn/unsafe/144x0/filters:quality(90)/https://cms-prod.s3-sgn09.fptcloud.com/00018636_special_kid_calcium_vitamine_d_eric_favre_125ml_strawberry_7590_5c51_large_c023869638.jpg',
  },
  {
    ten: 'Vitamin C',
    trieuChung: ['mệt mỏi', 'suy nhược', 'miễn dịch yếu'],
    moTa: 'Tăng sức đề kháng, giảm mệt mỏi, chống oxy hóa.',
    image:
      'https://cdn.nhathuoclongchau.com.vn/unsafe/144x0/filters:quality(90)/https://cms-prod.s3-sgn09.fptcloud.com/DSC_09589_44c5e2fc09.jpg',
  },
  {
    ten: 'Efferalgan',
    trieuChung: ['sốt', 'đau nhức', 'cảm cúm'],
    moTa: 'Viên sủi giảm đau, hạ sốt nhanh chóng.',
    image:
      'https://cdn.nhathuoclongchau.com.vn/unsafe/144x0/filters:quality(90)/https://cms-prod.s3-sgn09.fptcloud.com/DSC_01321_e59ecd3a79.jpg',
  },
  {
    ten: 'Decolgen',
    trieuChung: ['nghẹt mũi', 'nhức đầu', 'hắt hơi'],
    moTa: 'Điều trị cảm lạnh, nghẹt mũi, nhức đầu.',
    image:
      'https://cdn.nhathuoclongchau.com.vn/unsafe/144x0/filters:quality(90)/https://cms-prod.s3-sgn09.fptcloud.com/IMG_6464_4e4f351816.jpg',
  },
]

const trieuChungNhap = ref('')
const daTim = ref(false)
const thuocGoiY = ref([])
const thuocSoSanh = ref([])

function timThuoc() {
  const inputArr = trieuChungNhap.value
    .toLowerCase()
    .split(/[,\s]+/)
    .filter(Boolean)

  thuocGoiY.value = thuocList.filter((thuoc) =>
    thuoc.trieuChung.some((tc) =>
      inputArr.some((userTc) => removeAccents(tc.toLowerCase()).includes(removeAccents(userTc))),
    ),
  )

  daTim.value = true
}

watch(trieuChungNhap, (val) => {
  if (!val.trim()) {
    thuocGoiY.value = []
    daTim.value = false
    thuocSoSanh.value = []
  }
})

function chonSoSanh(thuoc) {
  if (thuocSoSanh.value.length < 2 && !thuocSoSanh.value.includes(thuoc)) {
    thuocSoSanh.value.push(thuoc)
  }
}

function removeAccents(str) {
  return str.normalize('NFD').replace(/\u0300-\u036f/g, '')
}
</script>

<style scoped>
.card {
  background-color: #f9fefb;
  border-radius: 16px;
}
input.form-control {
  border-radius: 12px;
  padding: 12px;
}
.btn {
  border-radius: 12px;
  font-weight: 600;
}
.card-text {
  font-size: 0.85rem;
}
.badge {
  font-size: 0.75rem;
}
</style>
