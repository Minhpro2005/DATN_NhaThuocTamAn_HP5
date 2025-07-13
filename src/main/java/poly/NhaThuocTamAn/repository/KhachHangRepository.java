package poly.NhaThuocTamAn.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import poly.NhaThuocTamAn.model.KhachHang;

import java.util.List;
import java.util.Optional;

public interface KhachHangRepository extends JpaRepository<KhachHang, Integer> {

    @Query("SELECT kh FROM KhachHang kh " +
           "WHERE (:hoTen IS NULL OR LOWER(kh.hoTen) LIKE LOWER(CONCAT('%', :hoTen, '%'))) " +
           "AND (:sdt IS NULL OR kh.soDienThoai LIKE CONCAT('%', :sdt, '%')) " +
           "AND (:email IS NULL OR LOWER(kh.email) LIKE LOWER(CONCAT('%', :email, '%'))) " +
           "AND (:diaChi IS NULL OR LOWER(kh.diaChi) LIKE LOWER(CONCAT('%', :diaChi, '%')))")
    List<KhachHang> searchAdvanced(String hoTen, String sdt, String email, String diaChi);
    
    Optional<KhachHang> findByEmailAndMatKhau(String email, String matKhau);
    boolean existsByEmail(String email);
    
    Optional<KhachHang> findByEmail(String email);

}

