package poly.NhaThuocTamAn.service;

import java.util.List;

import com.example.demo.dto.KhoHangDTO;
import poly.NhaThuocTamAn.model.BienTheThuoc;
import poly.NhaThuocTamAn.model.Thuoc;

public interface KhoHangService {
	
	void themHoacCapNhat(Thuoc thuoc, BienTheThuoc bienThe, int soLuongThem);
    List<KhoHangDTO> getAll();

    List<KhoHangDTO> search(String keyword);
    
    int getSoLuongTon(Integer maThuoc, Integer maBienThe);
    
}
