package poly.NhaThuocTamAn.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import poly.NhaThuocTamAn.model.LichSuDonHang;

public interface LichSuDonHangRepository extends JpaRepository<LichSuDonHang, Integer> {
	List<LichSuDonHang> findByDonHangMaDonHang(int maDonHang);

}
