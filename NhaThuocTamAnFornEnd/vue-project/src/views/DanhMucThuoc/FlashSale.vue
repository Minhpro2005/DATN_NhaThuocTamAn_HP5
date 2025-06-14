<template>
  <section class="container my-5">
    <div class="flash-sale-section">
      <div class="d-flex justify-content-between align-items-center mb-3">
        <h3 class="fw-bold mb-0">⚡ FLASH SALE - Ưu đãi đặc biệt hôm nay!</h3>
        <div>
          <button class="btn-carousel" data-bs-target="#flashSaleCarousel" data-bs-slide="prev">
            <i class="bi bi-chevron-left"></i>
          </button>
          <button class="btn-carousel" data-bs-target="#flashSaleCarousel" data-bs-slide="next">
            <i class="bi bi-chevron-right"></i>
          </button>
        </div>
      </div>

      <div id="flashSaleCarousel" class="carousel slide" data-bs-ride="carousel">
        <div class="carousel-inner">
          <!-- ✅ Slide 1 -->
          <div class="carousel-item active">
            <div class="row row-cols-2 row-cols-md-5 g-3">
              <div class="col" v-for="n in 5" :key="n">
                <div class="card border-0 shadow-sm h-100">
                  <img
                    src="https://cdn.nhathuoclongchau.com.vn/unsafe/256x0/filters:quality(90)/https://cms-prod.s3-sgn09.fptcloud.com/DSC_04180_e961a7a6a9.png"
                    class="card-img-top"
                    alt="Sản phẩm"
                  />
                  <div class="card-body p-2">
                    <h6 class="card-title small mb-1">Vitamin C 1000mg</h6>
                    <p class="mb-2">
                      <span class="text-danger fw-bold">59.000đ</span><br />
                      <small class="text-decoration-line-through text-light">80.000đ</small>
                    </p>
                    <a href="#" class="btn btn-sm btn-success w-100">Mua ngay</a>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <!-- ✅ Slide 2 -->
          <div class="carousel-item">
            <div class="row row-cols-2 row-cols-md-5 g-3">
              <div class="col">
                <div class="card border-0 shadow-sm h-100">
                  <img
                    src="https://cdn.nhathuoclongchau.com.vn/unsafe/256x0/filters:quality(90)/https://cms-prod.s3-sgn09.fptcloud.com/DSC_04267_4065764ed9.jpg"
                    class="card-img-top"
                    alt="Sản phẩm"
                  />
                  <div class="card-body p-2">
                    <h6 class="card-title small mb-1">Thực phẩm bổ sung X</h6>
                    <p class="mb-2">
                      <span class="text-danger fw-bold">120.000đ</span><br />
                      <small class="text-decoration-line-through text-light">150.000đ</small>
                    </p>
                    <a href="#" class="btn btn-sm btn-success w-100">Mua ngay</a>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </section>
</template>

<style scoped>
.flash-sale-section {
  background-color: #4caf50;
  color: white;
  padding: 24px;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 100, 0, 0.2);
}

/* ✅ Nút "Mua ngay" */
.flash-sale-section .btn-success {
  background-color: #1b5e20;
  border: none;
  font-weight: 500;
}
.flash-sale-section .btn-success:hover {
  background-color: #155a1e;
}

/* ✅ Card sản phẩm */
.flash-sale-section .card {
  border-radius: 12px;
  overflow: hidden;
  border: 1px solid transparent;
  transition:
    transform 0.3s ease,
    box-shadow 0.3s ease,
    border-color 0.3s ease;
  box-shadow: 0 0 8px rgba(0, 0, 0, 0.05);
}
.flash-sale-section .card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 28px rgba(46, 125, 50, 0.4);
  border-color: #2e7d32;
}

/* ✅ Ảnh sản phẩm */
.flash-sale-section .card-img-top {
  height: 140px;
  object-fit: contain;
  border-top-left-radius: 12px;
  border-top-right-radius: 12px;
}

/* ✅ Nút chuyển slide trái/phải */
.btn-carousel {
  background-color: white;
  color: #2e7d32;
  border: none;
  width: 40px;
  height: 40px;
  border-radius: 12px;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
  display: inline-flex;
  align-items: center;
  justify-content: center;
  padding: 0;
  margin-left: 4px;
  transition:
    background-color 0.3s ease,
    transform 0.2s ease;
}
.btn-carousel:hover {
  background-color: #e8f5e9;
  transform: scale(1.05);
}
.btn-carousel i {
  font-size: 1.2rem;
}
</style>
