package poly.NhaThuocTamAn.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import poly.NhaThuocTamAn.model.DanhGia;

public interface DanhGiaRepository extends JpaRepository<DanhGia, Integer>{
	List<DanhGia> findByBienTheThuoc_MaBienThe(Integer maBienThe);
    List<DanhGia> findByBienTheThuoc_Thuoc_MaThuoc(Integer maThuoc);
    boolean existsByKhachHang_MaKHAndBienTheThuoc_MaBienThe(Integer maKH, Integer maBienThe);
    boolean existsByKhachHang_MaKHAndThuoc_MaThuoc(Integer maKH, Integer maThuoc);
    List<DanhGia> findByThuoc_MaThuoc(Integer maThuoc);
}
