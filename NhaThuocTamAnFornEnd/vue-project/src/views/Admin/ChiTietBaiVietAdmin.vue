<template>
  <div class="container py-4" v-if="tin">
    <div class="card shadow-sm p-3 bg-white">
      <h5 class="text-success mb-3">📄 Chi Tiết Bài Viết</h5>

      <!-- Form chỉnh sửa -->
      <div v-if="editMode">
        <div class="mb-3">
          <label class="form-label">📝 Tiêu đề</label>
          <input v-model="tin.tieuDe" class="form-control" />
        </div>
        <div class="mb-3">
          <label class="form-label">📌 Mô tả</label>
          <textarea v-model="tin.moTa" class="form-control"></textarea>
        </div>
        <div class="mb-3">
          <label class="form-label">📖 Nội dung</label>
          <textarea v-model="tin.noiDung" class="form-control" rows="5"></textarea>
        </div>
        <div class="mb-3">
          <label class="form-label">✍️ Tác giả</label>
          <input v-model="tin.tacGia" class="form-control" />
        </div>
        <div class="mb-3">
          <label class="form-label">🖼 Link ảnh</label>
          <input v-model="tin.hinhAnh" class="form-control" />
        </div>
        <div class="d-flex gap-2">
          <button class="btn btn-success btn-sm" @click="luuChinhSua">💾 Lưu</button>
          <button class="btn btn-secondary btn-sm" @click="editMode = false">❌ Hủy</button>
        </div>
      </div>

      <!-- Chi tiết bài viết -->
      <table class="table table-bordered align-middle small" v-else>
        <tbody>
          <tr>
            <th>📝 Tiêu đề</th>
            <td>{{ tin.tieuDe }}</td>
          </tr>
          <tr>
            <th>📌 Mô tả</th>
            <td>{{ tin.moTa }}</td>
          </tr>
          <tr>
            <th>📅 Ngày đăng</th>
            <td>{{ tin.ngayDang }}</td>
          </tr>
          <tr>
            <th>✍️ Tác giả</th>
            <td>{{ tin.tacGia }}</td>
          </tr>
          <tr>
            <th>🖼 Ảnh</th>
            <td>
              <img
                :src="'http://localhost:8080/' + tin.hinhAnh"
                class="img-fluid rounded"
                style="max-width: 200px"
              />
            </td>
          </tr>
          <tr>
            <th>📖 Nội dung</th>
            <td>
              <div
                :style="{ maxHeight: xemThem ? 'none' : '150px', overflow: 'auto' }"
                v-html="tin.noiDung"
              ></div>
              <button class="btn btn-link p-0 mt-1" @click="xemThem = !xemThem">
                {{ xemThem ? 'Thu gọn ▲' : 'Xem thêm ▼' }}
              </button>
            </td>
          </tr>
          <tr>
            <th>👁 Trạng thái</th>
            <td :class="tin.trangThai ? 'text-success' : 'text-danger'">
              {{ tin.trangThai ? 'Hiển thị' : 'Đã ẩn' }}
            </td>
          </tr>
        </tbody>
      </table>

      <div class="d-flex justify-content-between mt-3">
        <router-link to="/admin/dang-bai" class="btn btn-sm btn-secondary">
          <i class="bi bi-arrow-left"></i> Quay lại
        </router-link>
        <div class="d-flex gap-2">
          <button class="btn btn-sm btn-warning" @click="editMode = true">🖊 Sửa</button>
          <button class="btn btn-sm btn-danger" @click="xoaBaiViet">🗑 Xóa</button>
          <button class="btn btn-sm btn-outline-dark" @click="toggleTrangThai">
            👁 {{ tin.trangThai ? 'Ẩn' : 'Hiện' }}
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import axios from 'axios'

const route = useRoute()
const router = useRouter()
const id = route.params.id

const xemThem = ref(false)
const editMode = ref(false)
const tin = ref(null)

// 🔹 Tải bài viết chi tiết từ API
const taiBaiViet = async () => {
  try {
    const res = await axios.get(`http://localhost:8080/api/tintuc/${id}`)
    tin.value = res.data
  } catch (e) {
    alert('❌ Không tìm thấy bài viết.')
    router.push('/admin/dang-bai')
  }
}

// 🔸 Lưu chỉnh sửa
const luuChinhSua = async () => {
  try {
    await axios.put(`http://localhost:8080/api/tintuc/${id}`, tin.value)
    alert('✅ Đã lưu thay đổi!')
    editMode.value = false
  } catch (e) {
    alert('❌ Lỗi khi cập nhật.')
  }
}

// 🔸 Xoá bài viết
const xoaBaiViet = async () => {
  if (confirm('Bạn chắc chắn muốn xoá bài viết này?')) {
    try {
      await axios.delete(`http://localhost:8080/api/tintuc/${id}`)
      alert('🗑 Đã xóa!')
      router.push('/admin/dang-bai')
    } catch (e) {
      alert('❌ Không thể xóa bài viết.')
    }
  }
}

// 🔸 Toggle trạng thái
const toggleTrangThai = async () => {
  try {
    const res = await axios.put(`http://localhost:8080/api/tintuc/toggle/${id}`)
    tin.value.trangThai = res.data.trangThai
  } catch (e) {
    alert('❌ Lỗi khi cập nhật trạng thái.')
  }
}

onMounted(taiBaiViet)
</script>

<style scoped>
.table th {
  width: 150px;
  vertical-align: middle;
}
.table td {
  vertical-align: middle;
  word-break: break-word;
}
</style>
