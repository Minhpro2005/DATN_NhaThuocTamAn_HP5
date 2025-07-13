package poly.NhaThuocTamAn.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import poly.NhaThuocTamAn.model.DonHang;

public interface DonHangRepository extends JpaRepository<DonHang, Integer> {
	// Lấy danh sách đơn hàng theo mã khách hàng
	List<DonHang> findByKhachHang_MaKH(Integer maKH);

}
