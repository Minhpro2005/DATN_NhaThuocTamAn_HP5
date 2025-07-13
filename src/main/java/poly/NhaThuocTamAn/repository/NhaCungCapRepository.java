package poly.NhaThuocTamAn.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import poly.NhaThuocTamAn.model.NhaCungCap;

import java.util.List;

public interface NhaCungCapRepository extends JpaRepository<NhaCungCap, Integer> {
    List<NhaCungCap> findByTenNCCContainingIgnoreCase(String tenNCC);
}

