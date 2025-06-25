package poly.NhaThuocTamAn.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import poly.NhaThuocTamAn.model.DonThuoc;
import poly.NhaThuocTamAn.service.DonThuocService;

@RestController
@RequestMapping("/api/don-thuoc")
public class DonThuocController {
	@Autowired
	private DonThuocService donthuocsrv;
	
	//Get all
	@GetMapping
	public List<DonThuoc> getAll(){
		return donthuocsrv.getAll();
	}
	
	//Get by ID
	@GetMapping("/{maDonThuoc}")
	public DonThuoc getById(@PathVariable Integer maDonThuoc) {
		return donthuocsrv.getById(maDonThuoc);
	}
	
	//Update
	@PutMapping("/{maDonThuoc}")
	public DonThuoc update(@PathVariable Integer maDonThuoc, @RequestBody DonThuoc donthuoc) {
		return donthuocsrv.update(maDonThuoc, donthuoc);
	}
}
