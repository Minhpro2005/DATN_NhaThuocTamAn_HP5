package poly.NhaThuocTamAn.service;
import com.example.demo.dto.DanhMucDTO;

import java.util.List;

public interface DanhMucService {

    DanhMucDTO create(DanhMucDTO dto);
    DanhMucDTO update(Integer maDM, DanhMucDTO dto);
    void delete(Integer maDM);
    DanhMucDTO getById(Integer maDM);
    List<DanhMucDTO> getAll();
    List<DanhMucDTO> searchByTenDanhMuc(String tenDanhMuc);
}

