package poly.NhaThuocTamAn.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import poly.NhaThuocTamAn.model.DonThuoc;
import poly.NhaThuocTamAn.repository.DonThuocRepository;

@Service
public class DonThuocService {
	private final DonThuocRepository donthuocrep;
	
	public DonThuocService(DonThuocRepository donthuocrep) {
		this.donthuocrep=donthuocrep;
	}
	
	public List<DonThuoc> getAll(){
		return this.donthuocrep.findAll();
	}
	
	public DonThuoc getById(Integer maDonThuoc) {
		Optional<DonThuoc> optional = donthuocrep.findById(maDonThuoc);
        return optional.orElse(null);
	}
	
	public DonThuoc update(DonThuoc donthuoc) {
		return donthuocrep.save(donthuoc);
	}
}
