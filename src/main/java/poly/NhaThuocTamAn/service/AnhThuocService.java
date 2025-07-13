package poly.NhaThuocTamAn.service;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import com.example.demo.dto.AnhThuocDTO;
import poly.NhaThuocTamAn.model.AnhThuoc;

public interface AnhThuocService {
    List<AnhThuoc> getAll();
    List<AnhThuoc> findByMaThuoc(Integer maThuoc);
    AnhThuoc save(AnhThuocDTO dto);
    void deleteById(Integer id);
    String uploadAnh(MultipartFile file);
}
