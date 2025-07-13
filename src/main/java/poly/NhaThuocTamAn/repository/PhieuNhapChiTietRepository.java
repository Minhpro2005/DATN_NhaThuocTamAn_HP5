package poly.NhaThuocTamAn.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import poly.NhaThuocTamAn.model.PhieuNhapChiTiet;

public interface PhieuNhapChiTietRepository extends JpaRepository<PhieuNhapChiTiet, Integer> {
    List<PhieuNhapChiTiet> findByPhieuNhap_maPN(Integer maPN); // đúng cú pháp camelCase
}
