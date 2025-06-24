package poly.NhaThuocTamAn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import poly.NhaThuocTamAn.model.DonThuoc;

@Repository
public interface DonThuocRepository extends JpaRepository<DonThuoc, Integer>{

}
