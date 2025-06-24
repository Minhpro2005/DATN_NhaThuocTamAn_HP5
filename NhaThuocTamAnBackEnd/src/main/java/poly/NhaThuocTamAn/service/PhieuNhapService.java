package poly.NhaThuocTamAn.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import poly.NhaThuocTamAn.model.PhieuNhap;
import poly.NhaThuocTamAn.repository.PhieuNhapRepository;

@Service
public class PhieuNhapService {
	private final PhieuNhapRepository pnrep;
	
	public PhieuNhapService(PhieuNhapRepository pnrep) {
		this.pnrep=pnrep;
	}
	
	public List<PhieuNhap> getAll(){
		return this.pnrep.findAll();
	}
	
	public PhieuNhap getById(Integer maPN) {
		Optional<PhieuNhap> optional = pnrep.findById(maPN);
        return optional.orElse(null);
	}
	
	public PhieuNhap add(PhieuNhap pn) {
		return pnrep.save(pn);
	}
	
	public PhieuNhap update(PhieuNhap pn) {
		return pnrep.save(pn);
	}
	
	public void deleteById(Integer maPN) {
		pnrep.deleteById(maPN);
	}
}
