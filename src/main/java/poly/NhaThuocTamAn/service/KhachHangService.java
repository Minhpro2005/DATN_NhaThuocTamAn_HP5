package poly.NhaThuocTamAn.service;



import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.KhachHangDTO;

public interface KhachHangService {
    List<KhachHangDTO> getAll();
    List<KhachHangDTO> search(String hoTen, String sdt, String email, String diaChi);
    KhachHangDTO getById(Integer id);
    KhachHangDTO updateStatus(Integer id, boolean status);
    
    // Thêm update thông tin
    KhachHangDTO update(Integer id, KhachHangDTO dto);
    
    // Thêm upload avatar
    KhachHangDTO uploadAvatar(Integer id, MultipartFile file);
}


