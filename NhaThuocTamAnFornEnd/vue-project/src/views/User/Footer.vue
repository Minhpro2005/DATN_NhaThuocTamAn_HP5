<template>
  <footer>
    <div class="footer-wrapper">
      <div class="footer-top text-center border-bottom pb-4">
        <div>
          <i class="bi bi-patch-check-fill fs-3 text-success"></i>
          <p class="mb-0 fw-bold text-success">Thân thiện cam kết</p>
          <small>chất lượng sản phẩm</small>
        </div>
        <div>
          <i class="bi bi-truck fs-3 text-success"></i>
          <p class="mb-0 fw-bold text-success">Miễn phí vận chuyển</p>
          <small>theo chính sách giao hàng</small>
        </div>
        <div>
          <i class="bi bi-clock fs-3 text-success"></i>
          <p class="mb-0 fw-bold text-success">Giao nhanh 2 giờ</p>
          <small>chỉ áp dụng ở Hà Nội</small>
        </div>
        <div>
          <i class="bi bi-arrow-counterclockwise fs-3 text-success"></i>
          <p class="mb-0 fw-bold text-success">Đổi trả 30 ngày</p>
          <small>kể từ ngày mua hàng</small>
        </div>
      </div>

      <div class="footer-columns container py-4">
        <div class="footer-column">
          <h6 class="fw-bold">Về chúng tôi</h6>
          <a href="#">Giới thiệu</a>
          <a href="#">Liên hệ nhà thuốc</a>
          <a href="#">Sơ đồ trang web</a>
          <a href="#">Điều khoản sử dụng</a>
          <a href="#">Khiếu nại dịch vụ</a>
          <a href="#" class="fw-bold text-success">Cách tra cứu thuốc đã cấp phép</a>
        </div>

        <div class="footer-column">
          <h6 class="fw-bold">Hỗ trợ khách hàng</h6>
          <a href="#">Hướng dẫn mua hàng</a>
          <a href="#">Chính sách giao hàng</a>
          <a href="#">Chính sách đổi trả</a>
          <a href="#">Chính sách bảo hành</a>
          <a href="#">Chính sách bảo mật</a>
        </div>

        <div class="footer-column">
          <h6 class="fw-bold">Chứng nhận bởi</h6>
          <img src="vue-project/public/Img/fotter1.jpg" alt="NCSC" height="40" class="mb-2 me-2" />
          <img src="vue-project\public\Img\fotter2.jpg" alt="BCT" height="40" />
        </div>

        <div class="footer-column">
          <h6 class="fw-bold">Đối tác vận chuyển</h6>
          <img src="vue-project\public\Img\fotterdhtk.png" alt="GHN" height="30" class="mb-2" />
          <br />
          <img src="vue-project\public\Img\fotterghn.png" alt="GHN" height="30" class="mb-2" />
          <br />
          <img src="vue-project\public\Img\fotterviettelpost.png" alt="Viettel Post" height="30" />
        </div>
      </div>

      <div class="footer-bottom border-top text-center pt-3">
        <small>&copy; 2025 NHÀ THUỐC TÂM AN.</small>
      </div>
    </div>
  </footer>
</template>

<style scoped>
footer {
  background-color: #e8f5e9;
  color: #333;
  font-family: 'Segoe UI', sans-serif;
}

.footer-wrapper {
  width: 100%;
}

.footer-top {
  display: flex;
  justify-content: space-around;
  flex-wrap: wrap;
  padding: 30px 15px;
  background-color: #e8f5e9;
}

.footer-top div {
  flex: 1 1 200px;
  margin: 10px;
  text-align: center;
}

.footer-columns {
  display: flex;
  flex-wrap: wrap;
  justify-content: space-between;
}

.footer-column {
  flex: 1 1 220px;
  margin-bottom: 20px;
}

.footer-column a {
  display: block;
  color: #333;
  font-size: 14px;
  margin-bottom: 6px;
  text-decoration: none;
}

.footer-column a:hover {
  color: #2e7d32;
  text-decoration: underline;
}

.footer-bottom {
  background-color: #e8f5e9;
  font-size: 13px;
  color: #777;
  padding-bottom: 20px;
}
</style>
