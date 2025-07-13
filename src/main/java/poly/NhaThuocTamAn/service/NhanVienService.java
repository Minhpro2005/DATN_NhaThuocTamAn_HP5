package poly.NhaThuocTamAn.service;

import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.NhanVienDTO;

import java.io.IOException;
import java.util.List;

public interface NhanVienService {
    List<NhanVienDTO> getAll();
    NhanVienDTO getById(Integer id);
    NhanVienDTO create(NhanVienDTO dto);
    NhanVienDTO update(Integer id, NhanVienDTO dto);
    void delete(Integer id);
    NhanVienDTO updateStatus(Integer id, Boolean status);
    List<NhanVienDTO> search(String keyword);
    NhanVienDTO uploadAvatar(Integer id, MultipartFile file) throws IOException;
}
