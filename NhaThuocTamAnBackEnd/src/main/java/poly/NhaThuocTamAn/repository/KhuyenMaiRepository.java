package poly.NhaThuocTamAn.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import poly.NhaThuocTamAn.model.KhuyenMai;

@Repository
public interface KhuyenMaiRepository extends JpaRepository<KhuyenMai, Integer>{
	Optional<KhuyenMai> findByMaCodeIgnoreCase(String maCode);
}
