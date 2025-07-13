package poly.NhaThuocTamAn.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import poly.NhaThuocTamAn.model.DonHangChiTiet;

public interface DonHangChiTietRepository extends JpaRepository<DonHangChiTiet, Integer> {
    
    // Kiểm tra đã mua biến thể
    boolean existsByDonHang_KhachHang_MaKHAndBienTheThuoc_MaBienTheAndDonHang_TrangThaiGreaterThanEqual(
        Integer maKH, Integer maBienThe, Integer trangThai);

    // Kiểm tra đã mua thuốc thường (nếu có trường 'thuoc' trong DonHangChiTiet)
    boolean existsByDonHang_KhachHang_MaKHAndThuoc_MaThuocAndDonHang_TrangThaiGreaterThanEqual(
        Integer maKH, Integer maThuoc, Integer trangThai);
}

