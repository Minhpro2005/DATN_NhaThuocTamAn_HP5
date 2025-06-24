package poly.NhaThuocTamAn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import poly.NhaThuocTamAn.model.DonHangChiTiet;

@Repository
public interface DonHangChiTietRepository extends JpaRepository<DonHangChiTiet, Integer>{

}
