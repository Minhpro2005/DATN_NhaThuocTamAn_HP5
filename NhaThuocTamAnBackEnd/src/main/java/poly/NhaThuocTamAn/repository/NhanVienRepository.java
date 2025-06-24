package poly.NhaThuocTamAn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import poly.NhaThuocTamAn.model.NhanVien;

@Repository
public interface NhanVienRepository extends JpaRepository<NhanVien, Integer>{

}
