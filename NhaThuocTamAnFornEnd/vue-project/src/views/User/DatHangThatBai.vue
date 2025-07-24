<template>
  <div class="container text-center py-5">
    <img
      src="https://cdn-icons-png.flaticon.com/512/463/463612.png"
      alt="fail"
      class="img-fluid mb-4"
      style="max-width: 180px"
    />
    <h2 class="text-danger fw-bold mb-3">❌ Đặt hàng thất bại!</h2>
    <p class="text-muted mb-4">
      Có lỗi xảy ra trong quá trình thanh toán hoặc xác nhận đơn hàng.<br />
      Vui lòng thử lại hoặc liên hệ hỗ trợ.
    </p>
    <router-link to="/" class="btn btn-outline-primary"> ⬅ Quay về trang chủ </router-link>
  </div>
</template>

<script setup>
// Không cần logic JS phức tạp, đây là trang tĩnh
</script>

<style scoped>
.container {
  max-width: 600px;
}
</style>
