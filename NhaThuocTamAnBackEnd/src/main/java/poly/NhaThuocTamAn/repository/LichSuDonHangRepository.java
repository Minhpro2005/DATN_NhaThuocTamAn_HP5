package poly.NhaThuocTamAn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import poly.NhaThuocTamAn.model.LichSuDonHang;

@Repository
public interface LichSuDonHangRepository extends JpaRepository<LichSuDonHang, Integer>{

}
