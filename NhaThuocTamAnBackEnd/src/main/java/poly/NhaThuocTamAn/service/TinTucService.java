package poly.NhaThuocTamAn.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import poly.NhaThuocTamAn.model.TinTuc;
import poly.NhaThuocTamAn.repository.TinTucRepository;

@Service
public class TinTucService {
	private final TinTucRepository tinrep;
	
	public TinTucService(TinTucRepository tinrep) {
		this.tinrep=tinrep;
	}
	
	public List<TinTuc> getAll(){
		return this.tinrep.findAll();
	}
	
	public TinTuc getById(Integer maTin) {
		Optional<TinTuc> optional = tinrep.findById(maTin);
        return optional.orElse(null);
	}
	
	public TinTuc add(TinTuc tin) {
		return tinrep.save(tin);
	}
	
	public TinTuc update(Integer maTin, TinTuc tin) {
		return tinrep.save(tin);
	}
	
	public void deleteById(Integer maTin) {
		tinrep.deleteById(maTin);
	}
}
