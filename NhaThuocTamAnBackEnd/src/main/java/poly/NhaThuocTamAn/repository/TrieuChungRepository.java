package poly.NhaThuocTamAn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import poly.NhaThuocTamAn.model.TrieuChung;

@Repository
public interface TrieuChungRepository extends JpaRepository<TrieuChung, Integer>{

}
