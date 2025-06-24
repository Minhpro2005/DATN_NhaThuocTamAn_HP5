package poly.NhaThuocTamAn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import poly.NhaThuocTamAn.model.DanhGia;

@Repository
public interface DanhGiaRepository extends JpaRepository<DanhGia, Integer>{

}
