package poly.NhaThuocTamAn.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import poly.NhaThuocTamAn.model.PhieuNhapChiTiet;

@Repository
public interface PhieuNhapChiTietRepository extends JpaRepository<PhieuNhapChiTiet, Integer>{
	List<PhieuNhapChiTiet> findByPhieuNhap_maPN(Integer maPN);
}
