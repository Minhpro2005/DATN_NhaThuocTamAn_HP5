package poly.NhaThuocTamAn.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.PhieuNhapDTO;
import poly.NhaThuocTamAn.model.PhieuNhap;
import poly.NhaThuocTamAn.model.PhieuNhapChiTiet;
import poly.NhaThuocTamAn.service.PhieuNhapService;

@RestController
@RequestMapping("/api/phieu-nhap")
@CrossOrigin("*")
public class PhieuNhapController {
	@Autowired
	private PhieuNhapService pnsrv;
	
	//Get all
	@GetMapping
	public List<PhieuNhap> getAll(){
		return pnsrv.getAll();
	}
	
	//Get detail
	@GetMapping("/{maPN}/chi-tiet")
	public List<PhieuNhapChiTiet> getChiTiet(@PathVariable Integer maPN) {
		return pnsrv.getChiTietByPhieuNhap(maPN);
	}
	
	//Add
	@PostMapping
	public PhieuNhap add(@RequestBody PhieuNhapDTO dto) {
		return pnsrv.create(dto);
	}	
}
