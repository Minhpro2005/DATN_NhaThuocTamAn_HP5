package poly.NhaThuocTamAn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import poly.NhaThuocTamAn.model.BienTheThuoc;

@Repository
public interface BienTheThuocRepository extends JpaRepository<BienTheThuoc, Integer>{

}
