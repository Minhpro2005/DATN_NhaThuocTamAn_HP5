package poly.NhaThuocTamAn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import poly.NhaThuocTamAn.model.DanhMuc;
import java.util.List;

public interface DanhMucRepository extends JpaRepository<DanhMuc, Integer> {
    List<DanhMuc> findByTenDanhMucContainingIgnoreCase(String tenDanhMuc);
}

