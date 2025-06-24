package poly.NhaThuocTamAn.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import poly.NhaThuocTamAn.model.DanhGia;
import poly.NhaThuocTamAn.repository.DanhGiaRepository;

@Service
public class DanhGiaService {
	private final DanhGiaRepository dgrep;
	
	public DanhGiaService(DanhGiaRepository dgrep) {
		this.dgrep=dgrep;
	}
	
	public List<DanhGia> getAll(){
		return this.dgrep.findAll();
	}
	
	public DanhGia getById(Integer maDG) {
		Optional<DanhGia> optional = dgrep.findById(maDG);
        return optional.orElse(null);
	}
	
	public void deleteById(Integer maDG) {
		dgrep.deleteById(maDG);
	}
}
