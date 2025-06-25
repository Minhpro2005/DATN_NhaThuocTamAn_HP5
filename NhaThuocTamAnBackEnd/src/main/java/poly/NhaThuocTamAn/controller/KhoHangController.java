package poly.NhaThuocTamAn.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import poly.NhaThuocTamAn.model.KhoHang;
import poly.NhaThuocTamAn.service.KhoHangService;

@RestController
@RequestMapping("/api/kho-hang")
public class KhoHangController {
	@Autowired
	private KhoHangService khosrv;
	
	//Get all
	@GetMapping
	public List<KhoHang> getAll(){
		return khosrv.getAll();
	}
	
	//Get by ID
	@GetMapping("/{maKhoHang}")
	public KhoHang getById(@PathVariable Integer maKhoHang) {
		return khosrv.getById(maKhoHang);
	}
	
	//Update
	@PutMapping("/{maKhoHang}")
	public KhoHang update(@PathVariable Integer maKhoHang, @RequestBody KhoHang kho) {
		return khosrv.update(maKhoHang, kho);
	}
}
