package poly.NhaThuocTamAn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import poly.NhaThuocTamAn.model.DonHang;

@Repository
public interface DonHangRepository extends JpaRepository<DonHang, Integer>{

}
