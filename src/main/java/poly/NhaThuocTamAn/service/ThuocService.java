package poly.NhaThuocTamAn.service;

import java.util.List;

import com.example.demo.dto.ThuocBanChayDTO;
import com.example.demo.dto.ThuocDTO;
import com.example.demo.dto.ThuocVaBienTheDTO;

import poly.NhaThuocTamAn.model.Thuoc;

public interface ThuocService {
	List<ThuocDTO> getAll();
    Thuoc save(ThuocDTO dto);
    Thuoc update(Integer id, ThuocDTO dto);
    void delete(Integer id);
    List<ThuocDTO> search(String keyword, Integer maDM, Boolean trangThai, Integer maNCC);
    
    List<ThuocVaBienTheDTO> getThuocVaBienTheTheoDanhMuc(Integer maDM);
    
    ThuocDTO findById(Integer id);

    List<ThuocBanChayDTO> getTop10ThuocBanChay();
    
    
}

