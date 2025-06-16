import FlashSale from '@/views/DanhMucThuoc/FlashSale.vue'
import SanPhamBanChay from '@/views/DanhMucThuoc/SanPhamBanChay.vue'
import Cart from '@/views/User/Cart.vue'
import DanhGia from '@/views/User/DanhGia.vue'
import DatHangThanhCong from '@/views/User/DatHangThanhCong.vue'
import DonHangCuaBan from '@/views/User/DonHangCuaBan.vue'
import Home from '@/views/User/Home.vue'
import LichSuMuaHang from '@/views/User/LichSuMuaHang.vue'
import News from '@/views/User/News.vue'
import ProductDetail from '@/views/User/ProductDetail.vue'
import ThongTinCaNhan from '@/views/User/ThongTinCaNhan.vue'
import ThongTinDatHang from '@/views/User/ThongTinDatHang.vue'
import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  { path: '/', redirect: '/home' },
  { path: '/home', name: 'home', component: Home },
  { path: '/product-detail/:id', name: 'product-detail', component: ProductDetail },
  { path: '/gio-hang', name: 'cart', component: Cart },
  { path: '/thong-tin-dat-hang', name: 'thong-tin-dat-hang', component: ThongTinDatHang },
  { path: '/dat-hang-thanh-cong', name: 'dat-hang-thanh-cong', component: DatHangThanhCong },
  { path: '/tin-tuc', name: 'tin-tuc', component: News },
  { path: '/giam-gia', name: 'giam-gia', component: FlashSale },
  { path: '/ban-chay', name: 'ban-chay', component: SanPhamBanChay },
  { path: '/danh-gia', name: 'danh-gia', component: DanhGia },

  {
    path: '/tai-khoan',
    component: TaiKhoan,
    children: [
      { path: '', redirect: '/tai-khoan/thong-tin-ca-nhan' },
      { path: 'thong-tin-ca-nhan', component: ThongTinCaNhan },
      { path: 'don-hang-cua-ban', component: DonHangCuaBan },
      { path: 'lich-su-mua-hang', component: LichSuMuaHang },
    ],
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

export default router
