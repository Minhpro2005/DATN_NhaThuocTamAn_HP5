package poly.NhaThuocTamAn.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import poly.NhaThuocTamAn.model.NhaCungCap;
import poly.NhaThuocTamAn.repository.NhaCungCapRepository;

@Service
public class NhaCungCapService {
	private final NhaCungCapRepository nccrep;
	
	public NhaCungCapService(NhaCungCapRepository nccrep) {
		this.nccrep=nccrep;
	}
	
	public List<NhaCungCap> getAll(){
		return this.nccrep.findAll();
	}
	
	public NhaCungCap getById(Integer maNCC) {
		Optional<NhaCungCap> optional = nccrep.findById(maNCC);
        return optional.orElse(null);
	}
	
	public NhaCungCap add(NhaCungCap ncc) {
		return nccrep.save(ncc);
	}
	
	public NhaCungCap update(Integer maNCC, NhaCungCap ncc) {
		return nccrep.save(ncc);
	}
	
	public void deleteById(Integer maNCC) {
		nccrep.deleteById(maNCC);
	}
}
