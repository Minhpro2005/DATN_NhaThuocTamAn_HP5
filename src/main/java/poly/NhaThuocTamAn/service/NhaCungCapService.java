package poly.NhaThuocTamAn.service;

import com.example.demo.dto.NhaCungCapDTO;
import java.util.List;

public interface NhaCungCapService {

    NhaCungCapDTO create(NhaCungCapDTO dto);
    NhaCungCapDTO update(Integer maNCC, NhaCungCapDTO dto);
    void delete(Integer maNCC);
    NhaCungCapDTO getById(Integer maNCC);
    List<NhaCungCapDTO> getAll();
    List<NhaCungCapDTO> searchByTenNCC(String tenNCC);
}

