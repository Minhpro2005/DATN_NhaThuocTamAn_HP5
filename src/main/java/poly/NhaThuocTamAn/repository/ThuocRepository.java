package poly.NhaThuocTamAn.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query; 
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.dto.ThuocBanChayDTO;
import com.example.demo.dto.ThuocVaBienTheDTO;

import poly.NhaThuocTamAn.model.Thuoc;

@Repository
public interface ThuocRepository extends JpaRepository<Thuoc, Integer> {

    @Query("SELECT t FROM Thuoc t WHERE " +
           "(:keyword IS NULL OR t.tenThuoc LIKE %:keyword%) AND " +
           "(:maDM IS NULL OR t.danhMuc.maDM = :maDM) AND " +
           "(:trangThai IS NULL OR t.trangThai = :trangThai) AND " +
           "(:maNCC IS NULL OR t.nhaCungCap.maNCC = :maNCC)")
    List<Thuoc> searchThuoc(
        @Param("keyword") String keyword,
        @Param("maDM") Integer maDM,
        @Param("trangThai") Boolean trangThai,
        @Param("maNCC") Integer maNCC
    );
    
    @Query("SELECT new com.example.demo.dto.ThuocVaBienTheDTO(t, b) " +
    	       "FROM Thuoc t LEFT JOIN BienTheThuoc b ON b.thuoc = t " +  // ✅ DÙNG object `b.thuoc = t`
    	       "WHERE t.danhMuc.maDM = :maDM")
    	List<ThuocVaBienTheDTO> findThuocVaBienTheTheoDanhMuc(@Param("maDM") Integer maDM);

    @Query("""
    	    SELECT new com.example.demo.dto.ThuocBanChayDTO(
    	        t.maThuoc, t.tenThuoc, SUM(ct.soLuong), a.hinhAnh, t.giaBan
    	    )
    	    FROM DonHangChiTiet ct
    	    JOIN ct.thuoc t
    	    LEFT JOIN AnhThuoc a ON a.thuoc.maThuoc = t.maThuoc AND a.anhChinh = true
    	    GROUP BY t.maThuoc, t.tenThuoc, a.hinhAnh, t.giaBan
    	    ORDER BY SUM(ct.soLuong) DESC
    	""")
    	List<ThuocBanChayDTO> findTop10ThuocBanChay(Pageable pageable);



}
