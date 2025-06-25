package poly.NhaThuocTamAn.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import poly.NhaThuocTamAn.model.DanhGia;
import poly.NhaThuocTamAn.service.DanhGiaService;

@RestController
@RequestMapping("/api/danh-gia")
public class DanhGiaController {
	@Autowired
	private DanhGiaService dgsrv;
	
	//Get all
	@GetMapping
	public List<DanhGia> getAll(){
		return dgsrv.getAll();
	}
	
	//Get by ID
	@GetMapping("/{maDG}")
	public DanhGia getById(@PathVariable Integer maDG) {
		return dgsrv.getById(maDG);
	}
		
	//Delete
	@DeleteMapping("/{maDG}")
	public void delete(@PathVariable Integer maDG) {
		dgsrv.deleteById(maDG);
	}
}
