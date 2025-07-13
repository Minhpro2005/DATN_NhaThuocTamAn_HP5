package poly.NhaThuocTamAn.service;

import com.example.demo.dto.DanhGiaDTO;

import java.util.List;

public interface DanhGiaService {
    List<DanhGiaDTO> getDanhGiaByBienThe(Integer maBienThe);
    DanhGiaDTO addDanhGia(DanhGiaDTO dto);
    DanhGiaDTO updateDanhGia(Integer maDG, DanhGiaDTO dto); 
    void deleteDanhGia(Integer maDG);
    List<DanhGiaDTO> getDanhGiaByThuoc(Integer maThuoc);
    List<DanhGiaDTO> getAllDanhGia();
}

