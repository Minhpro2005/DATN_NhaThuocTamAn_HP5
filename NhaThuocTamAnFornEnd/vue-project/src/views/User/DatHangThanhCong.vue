<template>
  <div class="container py-5 text-center">
    <div class="card p-5 shadow-sm mx-auto" style="max-width: 600px">
      <div class="text-success mb-3">
        <i class="bi bi-check-circle-fill" style="font-size: 4rem"></i>
      </div>
      <h3 class="fw-bold mb-3">Đặt hàng thành công!</h3>
      <p class="text-muted">Cảm ơn bạn đã mua hàng tại cửa hàng của chúng tôi.</p>
      <div class="d-flex justify-content-center gap-3 mt-4">
        <router-link to="/tai-khoan/don-hang-cua-ban" class="btn btn-outline-primary">
          <i class="bi bi-card-list me-1"></i> Xem đơn hàng
        </router-link>
        <router-link to="/" class="btn btn-success">
          <i class="bi bi-house-door me-1"></i> Tiếp tục mua sắm
        </router-link>
      </div>
    </div>
  </div>
</template>

<script setup>
// Bạn có thể nhận thông tin đơn hàng từ route params hoặc store nếu muốn hiển thị chi tiết
</script>

<style scoped>
.container {
  min-height: 80vh;
  display: flex;
  align-items: center;
  justify-content: center;
}
</style>
