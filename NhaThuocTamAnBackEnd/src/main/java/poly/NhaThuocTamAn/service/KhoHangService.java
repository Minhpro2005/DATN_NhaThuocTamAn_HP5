package poly.NhaThuocTamAn.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import poly.NhaThuocTamAn.model.KhoHang;
import poly.NhaThuocTamAn.repository.KhoHangRepository;

@Service
public class KhoHangService {
	private final KhoHangRepository khorep;
	
	public KhoHangService(KhoHangRepository khorep) {
		this.khorep=khorep;
	}
	
	public List<KhoHang> getAll(){
		return this.khorep.findAll();
	}
	
	public KhoHang getById(Integer maKhoHang) {
		Optional<KhoHang> optional = khorep.findById(maKhoHang);
        return optional.orElse(null);
	}
	
	public KhoHang update(KhoHang kho) {
		return khorep.save(kho);
	}
}
