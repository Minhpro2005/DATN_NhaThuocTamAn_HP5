import { createRouter, createWebHistory } from 'vue-router'

// Layouts
import AdminLayout from '@/views/Admin/AdminLayout.vue'

// Trang người dùng
import Home from '@/views/User/Home.vue'
import DangNhap from '@/views/User/DangNhap.vue'
import DangKy from '@/views/User/DangKy.vue'
import QuenMatKhau from '@/views/User/QuenMatKhau.vue'
import XacThucEmail from '@/views/User/XacThucEmail.vue'
import Cart from '@/views/User/Cart.vue'
import ThongTinDatHang from '@/views/User/ThongTinDatHang.vue'
import DatHangThanhCong from '@/views/User/DatHangThanhCong.vue'
import ProductDetail from '@/views/User/ProductDetail.vue'
import DonThuoc from '@/views/User/DonThuoc.vue'
import News from '@/views/User/News.vue'
import ChiTietTinTuc from '@/views/User/ChiTietTinTuc.vue'
import DanhGia from '@/views/User/DanhGia.vue'
import TrieuChungGoiYThuoc from '@/views/User/TrieuChungGoiYThuoc.vue'

// Trang tài khoản cá nhân (có router con)
import TaiKhoan from '@/views/User/TaiKhoan.vue'
import ThongTinCaNhan from '@/views/User/ThongTinCaNhan.vue'
import DonHangCuaBan from '@/views/User/DonHangCuaBan.vue'
import LichSuMuaHang from '@/views/User/LichSuMuaHang.vue'
import DoiMatKhau from '@/views/User/DoiMatKhau.vue'

// Danh mục thuốc
import VitaminVaKhoangChat from '@/views/DanhMucThuoc/VitaminVaKhoangChat.vue'
import FlashSale from '@/views/DanhMucThuoc/FlashSale.vue'
import SanPhamBanChay from '@/views/DanhMucThuoc/SanPhamBanChay.vue'

// Trang quản trị
import Admin from '@/views/Admin/Admin.vue'
import SanPhamAdmin from '@/views/Admin/SanPhamAdmin.vue'
import BienTheThuoc from '@/views/Admin/BienTheThuoc.vue'
import AnhThuoc from '@/views/Admin/AnhThuoc.vue'
import DanhMuc from '@/views/Admin/DanhMuc.vue'
import NhaCungCapAdmin from '@/views/Admin/NhaCungCapAdmin.vue'
import NhapHangAdmin from '@/views/Admin/NhapHangAdmin.vue'
import DonHang from '@/views/Admin/DonHang.vue'
import DonHangChiTiet from '@/views/Admin/DonHangChiTiet.vue'
import DonThuocAdmin from '@/views/Admin/DonThuocAdmin.vue'
import ChiTietPhieuNhap from '@/views/Admin/ChiTietPhieuNhap.vue'
import KhoHangAdmin from '@/views/Admin/KhoHangAdmin.vue'
import KhuyenMaiAdmin from '@/views/Admin/KhuyenMaiAdmin.vue'
import DanhGiaAdmin from '@/views/Admin/DanhGiaAdmin.vue'
import DangBai from '@/views/Admin/DangBai.vue'
import ChiTietBaiVietAdmin from '@/views/Admin/ChiTietBaiVietAdmin.vue'
import NhanVienAdmin from '@/views/Admin/NhanVienAdmin.vue'
import KhachHangAdmin from '@/views/Admin/KhachHangAdmin.vue'

const routes = [
  { path: '/', redirect: '/home' },
  { path: '/home', name: 'home', component: Home },
  { path: '/dang-nhap', name: 'dang-nhap', component: DangNhap },
  { path: '/dang-ky', name: 'dang-ky', component: DangKy },
  { path: '/quen-mat-khau', name: 'quen-mat-khau', component: QuenMatKhau },
  { path: '/xac-thuc-email', name: 'xac-thuc-email', component: XacThucEmail },
  { path: '/gio-hang', name: 'cart', component: Cart },
  { path: '/thong-tin-dat-hang', name: 'thong-tin-dat-hang', component: ThongTinDatHang },
  { path: '/dat-hang-thanh-cong', name: 'dat-hang-thanh-cong', component: DatHangThanhCong },
  { path: '/product-detail/:id', name: 'product-detail', component: ProductDetail },
  { path: '/don-thuoc', name: 'don-thuoc', component: DonThuoc },
  { path: '/tin-tuc', name: 'tin-tuc', component: News },
  { path: '/tin-tuc/:maTin', name: 'chi-tiet-tin-tuc', component: ChiTietTinTuc },
  { path: '/vitamin-khoang-chat', name: 'vitamin', component: VitaminVaKhoangChat },
  { path: '/giam-gia', name: 'giam-gia', component: FlashSale },
  { path: '/ban-chay', name: 'ban-chay', component: SanPhamBanChay },
  { path: '/danh-gia', name: 'danh-gia', component: DanhGia },
  { path: '/goi-y-mua-thuoc-theo-trieu-trung', component: TrieuChungGoiYThuoc },

  {
    path: '/tai-khoan',
    component: TaiKhoan,
    children: [
      { path: '', redirect: '/tai-khoan/thong-tin-ca-nhan' },
      { path: 'thong-tin-ca-nhan', component: ThongTinCaNhan },
      { path: 'don-hang-cua-ban', component: DonHangCuaBan },
      { path: 'lich-su-mua-hang', component: LichSuMuaHang },
      { path: 'doi-mat-khau', component: DoiMatKhau },
    ],
  },

  {
    path: '/admin',
    component: AdminLayout,
    children: [
      { path: '', component: Admin },
      { path: 'san-pham', component: SanPhamAdmin },
      { path: 'bien-the', component: BienTheThuoc },
      { path: 'anh-san-pham', component: AnhThuoc },
      { path: 'danh-muc', component: DanhMuc },
      { path: 'nha-cung-cap', component: NhaCungCapAdmin },
      { path: 'nhap-hang', component: NhapHangAdmin },
      { path: 'don-hang', component: DonHang },
      { path: 'chi-tiet-don-hang/:maDonHang', component: DonHangChiTiet },
      { path: 'don-thuoc', component: DonThuocAdmin },
      { path: 'phieu-nhap/:maPN', component: ChiTietPhieuNhap },
      { path: 'kho-hang', component: KhoHangAdmin },
      { path: 'khuyen-mai', component: KhuyenMaiAdmin },
      { path: 'danh-gia', component: DanhGiaAdmin },
      { path: 'dang-bai', component: DangBai },
      { path: 'chi-tiet-bai-viet/:id', component: ChiTietBaiVietAdmin },
      { path: 'nhan-vien', component: NhanVienAdmin },
      { path: 'khach-hang', component: KhachHangAdmin },
    ],
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

export default router
