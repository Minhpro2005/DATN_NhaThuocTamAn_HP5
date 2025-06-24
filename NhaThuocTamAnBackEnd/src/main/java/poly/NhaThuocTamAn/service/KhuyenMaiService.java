package poly.NhaThuocTamAn.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import poly.NhaThuocTamAn.model.KhuyenMai;
import poly.NhaThuocTamAn.repository.KhuyenMaiRepository;

@Service
public class KhuyenMaiService {
	private final KhuyenMaiRepository kmrep;
	
	public KhuyenMaiService(KhuyenMaiRepository kmrep) {
		this.kmrep=kmrep;
	}
	
	public List<KhuyenMai> getAll(){
		return this.kmrep.findAll();
	}
	
	public KhuyenMai getById(Integer maKM) {
		Optional<KhuyenMai> optional = kmrep.findById(maKM);
        return optional.orElse(null);
	}
	
	public KhuyenMai add(KhuyenMai km) {
		return kmrep.save(km);
	}
	
	public KhuyenMai update(KhuyenMai km) {
		return kmrep.save(km);
	}
	
	public void deleteById(Integer maKM) {
		kmrep.deleteById(maKM);
	}
}
