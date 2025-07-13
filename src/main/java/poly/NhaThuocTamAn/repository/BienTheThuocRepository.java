package poly.NhaThuocTamAn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import poly.NhaThuocTamAn.model.BienTheThuoc;
import java.util.List;

public interface BienTheThuocRepository extends JpaRepository<BienTheThuoc, Integer> {
    List<BienTheThuoc> findByThuoc_MaThuoc(Integer maThuoc);
}
