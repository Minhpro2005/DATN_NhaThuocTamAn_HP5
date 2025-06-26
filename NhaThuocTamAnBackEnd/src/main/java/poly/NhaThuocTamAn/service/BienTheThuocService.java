package poly.NhaThuocTamAn.service;

import com.example.demo.dto.BienTheThuocDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BienTheThuocService {
    BienTheThuocDTO save(BienTheThuocDTO dto, MultipartFile file);
    void delete(Integer id);
    List<BienTheThuocDTO> getAll();
    List<BienTheThuocDTO> findByThuoc(Integer maThuoc);
}
