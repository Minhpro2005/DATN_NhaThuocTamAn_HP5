package poly.NhaThuocTamAn.service;

import java.util.List;
import com.example.demo.dto.NhaCungCapDTO;

public interface NhaCungCapService {
	NhaCungCapDTO create(NhaCungCapDTO dto);
    NhaCungCapDTO update(Integer maNCC, NhaCungCapDTO dto);
    void delete(Integer maNCC);
    NhaCungCapDTO getById(Integer maNCC);
    List<NhaCungCapDTO> getAll();
    List<NhaCungCapDTO> searchByTenNCC(String tenNCC);
}
