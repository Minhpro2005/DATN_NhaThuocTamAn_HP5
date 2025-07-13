package poly.NhaThuocTamAn.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import poly.NhaThuocTamAn.model.NhanVien;

@Repository
public interface NhanVienRepository extends JpaRepository<NhanVien, Integer> {

    @Query("SELECT nv FROM NhanVien nv WHERE " +
            "(:keyword IS NULL OR " +
            "LOWER(nv.hoTen) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(nv.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(nv.soDienThoai) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<NhanVien> search(String keyword);
    Optional<NhanVien> findByEmailAndMatKhau(String email, String matKhau);
}
