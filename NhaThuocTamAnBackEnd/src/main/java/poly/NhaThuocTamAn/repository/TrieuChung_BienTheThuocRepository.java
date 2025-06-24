package poly.NhaThuocTamAn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import poly.NhaThuocTamAn.model.TrieuChung_BienTheThuoc;

@Repository
public interface TrieuChung_BienTheThuocRepository extends JpaRepository<TrieuChung_BienTheThuoc, Integer>{

}
