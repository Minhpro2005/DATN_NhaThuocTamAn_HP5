package poly.NhaThuocTamAn.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import poly.NhaThuocTamAn.model.AnhThuoc;

public interface AnhThuocRepository extends JpaRepository<AnhThuoc, Integer> {
    List<AnhThuoc> findByThuoc_MaThuoc(Integer maThuoc);
    
    @Query("SELECT a FROM AnhThuoc a WHERE a.thuoc.maThuoc = :maThuoc AND a.anhChinh = true")
    Optional<AnhThuoc> findAnhChinhByMaThuoc(@Param("maThuoc") Integer maThuoc);
    
}
