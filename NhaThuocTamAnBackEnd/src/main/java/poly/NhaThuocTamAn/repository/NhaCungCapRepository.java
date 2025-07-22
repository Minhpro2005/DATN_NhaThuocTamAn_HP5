package poly.NhaThuocTamAn.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import poly.NhaThuocTamAn.model.NhaCungCap;

@Repository
public interface NhaCungCapRepository extends JpaRepository<NhaCungCap, Integer>{
	List<NhaCungCap> findByTenNCCContainingIgnoreCase(String tenNCC);
}
