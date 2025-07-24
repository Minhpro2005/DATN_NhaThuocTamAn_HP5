package poly.NhaThuocTamAn.service;

import java.util.List;
import poly.NhaThuocTamAn.model.PhieuNhap;
import poly.NhaThuocTamAn.model.PhieuNhapChiTiet;
import com.example.demo.dto.PhieuNhapDTO;

public interface PhieuNhapService {
	PhieuNhap create(PhieuNhapDTO dto);
    List<PhieuNhap> getAll();
    
    // Thêm dòng này để khớp với @Override trong PhieuNhapServiceImpl
    List<PhieuNhapChiTiet> getChiTietByPhieuNhap(int maPN);

}
