package poly.NhaThuocTamAn.service;

import java.util.List;

import com.example.demo.dto.DanhGiaDTO;

public interface DanhGiaService {
	List<DanhGiaDTO> getAllDanhGia();
	List<DanhGiaDTO> getDanhGiaByThuoc(Integer maThuoc);
	List<DanhGiaDTO> getDanhGiaByBienThe(Integer maBienThe);
    DanhGiaDTO addDanhGia(DanhGiaDTO dto);
    DanhGiaDTO updateDanhGia(Integer maDG, DanhGiaDTO dto); 
    void deleteDanhGia(Integer maDG);
    
}
