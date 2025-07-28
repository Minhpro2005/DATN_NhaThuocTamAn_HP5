package poly.NhaThuocTamAn.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import com.example.demo.dto.KhuyenMaiDTO;

public interface KhuyenMaiService {
	List<KhuyenMaiDTO> getAll();
    KhuyenMaiDTO getById(Integer id);
    KhuyenMaiDTO create(KhuyenMaiDTO dto);
    KhuyenMaiDTO update(Integer id, KhuyenMaiDTO dto);
    void delete(Integer id);
    Optional<KhuyenMaiDTO> apDungVoucher(String code, BigDecimal tongTien);
}
