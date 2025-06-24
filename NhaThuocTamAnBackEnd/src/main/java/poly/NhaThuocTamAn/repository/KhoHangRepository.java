package poly.NhaThuocTamAn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import poly.NhaThuocTamAn.model.KhoHang;

@Repository
public interface KhoHangRepository extends JpaRepository<KhoHang, Integer>{

}
