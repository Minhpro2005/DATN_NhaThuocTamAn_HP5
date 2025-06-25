package poly.NhaThuocTamAn.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import poly.NhaThuocTamAn.model.PhieuNhap;
import poly.NhaThuocTamAn.service.PhieuNhapService;

@RestController
@RequestMapping("/api/phieu-nhap")
public class PhieuNhapController {
	@Autowired
	private PhieuNhapService pnsrv;
	
	//Get all
	@GetMapping
	public List<PhieuNhap> getAll(){
		return pnsrv.getAll();
	}
	
	//Get by ID
	@GetMapping("/{maPN}")
	public PhieuNhap getById(@PathVariable Integer maPN) {
		return pnsrv.getById(maPN);
	}
	
	//Add
	@PostMapping
	public PhieuNhap add(@RequestBody PhieuNhap pn) {
		return pnsrv.add(pn);
	}
	
	//Update
	@PutMapping("/{maPN}")
	public PhieuNhap update(@PathVariable Integer maPN, @RequestBody PhieuNhap pn) {
		return pnsrv.update(maPN, pn);
	}
	
	//Delete
	@DeleteMapping("/{maPN")
	public void delete(@PathVariable Integer maPN) {
		pnsrv.deleteById(maPN);
	}
	
}
