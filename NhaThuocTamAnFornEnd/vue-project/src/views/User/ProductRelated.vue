<template>
  <section class="related-wrapper mt-5">
    <div class="related-box">
      <h5 class="section-title mb-4">
        <i class="bi bi-fire text-success me-2"></i>
        Sản phẩm liên quan
      </h5>
      <div class="row">
        <!-- 5 sản phẩm cố định -->
        <div class="col-2-4 mb-4">
          <div class="card h-100">
            <img
              src="https://cdn.nhathuoclongchau.com.vn/unsafe/256x0/filters:quality(90)/https://cms-prod.s3-sgn09.fptcloud.com/DSC_03594_d9d39b4d3f.jpg"
              class="card-img-top"
              alt="Sản phẩm"
            />
            <div class="card-body">
              <h5 class="card-title">Tên thuốc 1</h5>
              <p class="card-text text-success fw-bold">Giá: 120.000đ</p>
              <a href="#" class="btn btn-green w-100">Mua ngay</a>
            </div>
          </div>
        </div>

        <div class="col-2-4 mb-4">
          <div class="card h-100">
            <img
              src="https://cdn.nhathuoclongchau.com.vn/unsafe/256x0/filters:quality(90)/https://cms-prod.s3-sgn09.fptcloud.com/00031306_dizigone_nano_bac_tuyp_25g_8953_633b_large_ca3fd63a8c.jpg"
              class="card-img-top"
              alt="Sản phẩm"
            />
            <div class="card-body">
              <h5 class="card-title">Tên thuốc 1</h5>
              <p class="card-text text-success fw-bold">Giá: 120.000đ</p>
              <a href="#" class="btn btn-green w-100">Mua ngay</a>
            </div>
          </div>
        </div>

        <div class="col-2-4 mb-4">
          <div class="card h-100">
            <img
              src="https://nhathuocthanthien.com.vn/wp-content/uploads/2019/02/dgm_nttt_prospan-100ml-250x250.jpg"
              class="card-img-top"
              alt="Sản phẩm"
            />
            <div class="card-body">
              <h5 class="card-title">Tên thuốc 1</h5>
              <p class="card-text text-success fw-bold">Giá: 120.000đ</p>
              <a href="#" class="btn btn-green w-100">Mua ngay</a>
            </div>
          </div>
        </div>

        <div class="col-2-4 mb-4">
          <div class="card h-100">
            <img
              src="https://nhathuocthanthien.com.vn/wp-content/uploads/2019/03/dgm_nttt_bio-acimin-fiber-250x250.jpg"
              class="card-img-top"
              alt="Sản phẩm"
            />
            <div class="card-body">
              <h5 class="card-title">Tên thuốc 1</h5>
              <p class="card-text text-success fw-bold">Giá: 120.000đ</p>
              <a href="#" class="btn btn-green w-100">Mua ngay</a>
            </div>
          </div>
        </div>

        <div class="col-2-4 mb-4">
          <div class="card h-100">
            <img
              src="https://nhathuocthanthien.com.vn/wp-content/uploads/2019/03/dgm_nttt_tpcn-biovital-250x250.jpg"
              class="card-img-top"
              alt="Sản phẩm"
            />
            <div class="card-body">
              <h5 class="card-title">Tên thuốc 1</h5>
              <p class="card-text text-success fw-bold">Giá: 120.000đ</p>
              <a href="#" class="btn btn-green w-100">Mua ngay</a>
            </div>
          </div>
        </div>
      </div>
    </div>
  </section>
</template>

<style scoped>
.related-wrapper {
  padding-top: 32px;
}

.related-box {
  background-color: #fff;
  border-radius: 16px;
  padding: 24px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.06);
  max-width: 960px;
  margin: auto;
}

.col-2-4 {
  flex: 0 0 auto;
  width: 20%;
}
@media (max-width: 768px) {
  .col-2-4 {
    width: 50%;
  }
}
@media (max-width: 576px) {
  .col-2-4 {
    width: 100%;
  }
}

.card {
  border-radius: 12px;
  overflow: hidden;
  transition:
    transform 0.3s ease,
    box-shadow 0.3s ease;
  box-shadow: 0 0 8px rgba(0, 0, 0, 0.05);
}

.card:hover {
  transform: translateY(-5px);
  box-shadow: 0 6px 24px rgba(0, 100, 0, 0.3);
}

.card-img-top {
  border-top-left-radius: 12px;
  border-top-right-radius: 12px;
  object-fit: cover;
}

.btn-green {
  background-color: #2e7d32;
  color: white;
  border: none;
  border-radius: 8px;
  padding: 10px 20px;
  transition: background-color 0.3s ease;
}
.btn-green:hover {
  background-color: #1b5e20;
}
</style>
