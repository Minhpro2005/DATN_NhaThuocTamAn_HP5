package poly.NhaThuocTamAn.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.dto.SanPhamCanhBaoDTO;

import poly.NhaThuocTamAn.model.BienTheThuoc;
import poly.NhaThuocTamAn.model.KhoHang;
import poly.NhaThuocTamAn.model.Thuoc;

public interface KhoHangRepository extends JpaRepository<KhoHang, Integer>{
	Optional<KhoHang> findByThuoc(Thuoc thuoc);
	Optional<KhoHang> findByBienTheThuoc(BienTheThuoc bienTheThuoc);

	@Query("""
		    SELECT k FROM KhoHang k
		    LEFT JOIN k.thuoc t
		    LEFT JOIN k.bienTheThuoc bt
		    WHERE LOWER(t.tenThuoc) LIKE :keyword
		       OR LOWER(bt.tenBienThe) LIKE :keyword
		""")
		List<KhoHang> searchByKeyword(@Param("keyword") String keyword);

	@Query("SELECT k FROM KhoHang k WHERE k.thuoc.maThuoc = :maThuoc")
	Optional<KhoHang> findByThuocId(@Param("maThuoc") Integer maThuoc);

	@Query("SELECT k FROM KhoHang k WHERE k.bienTheThuoc.maBienThe = :maBienThe")
	Optional<KhoHang> findByBienTheId(@Param("maBienThe") Integer maBienThe);

	@Query("SELECT new com.example.demo.dto.SanPhamCanhBaoDTO(k.bienTheThuoc.tenBienThe, k.soLuongTon) " +
		       "FROM KhoHang k WHERE k.soLuongTon <= 10")
		List<SanPhamCanhBaoDTO> findSapHet();

	@Query("""
		    SELECT 
		        COALESCE(bt.tenBienThe, t.tenThuoc),
		        COALESCE(bt.hinhAnh, (
		            SELECT a.hinhAnh FROM AnhThuoc a 
		            WHERE a.thuoc = t AND a.anhChinh = true
		        )),
		        k.soLuongTon
		    FROM KhoHang k
		    LEFT JOIN k.bienTheThuoc bt
		    LEFT JOIN k.thuoc t
		    ORDER BY k.soLuongTon DESC
		""")
		List<Object[]> topTonKho(Pageable pageable);
}
