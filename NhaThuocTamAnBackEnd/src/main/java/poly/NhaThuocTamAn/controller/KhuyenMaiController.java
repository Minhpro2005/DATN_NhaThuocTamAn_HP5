package poly.NhaThuocTamAn.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import poly.NhaThuocTamAn.model.KhuyenMai;
import poly.NhaThuocTamAn.service.KhuyenMaiService;

@RestController
@RequestMapping("/api/khuyen-mai")
public class KhuyenMaiController {
	@Autowired
	private KhuyenMaiService kmsrv;
	
	//Get all
	@GetMapping
	public List<KhuyenMai> getAll(){
		return kmsrv.getAll();
	}
	
	//Get by ID
	@GetMapping("/{maKM}")
	public KhuyenMai getById(@PathVariable Integer maKM) {
		return kmsrv.getById(maKM);
	}
	
	//Add
	@PostMapping
	public KhuyenMai add(@RequestBody KhuyenMai km) {
		return kmsrv.add(km);
	}
	
	//Update
	@PutMapping("/{maKM}")
	public KhuyenMai update(@PathVariable Integer maKM, @RequestBody KhuyenMai km) {
		return kmsrv.update(maKM, km);
	}
	
	//Delete
	@DeleteMapping("/{maKM}")
	public void delete(@PathVariable Integer maKM) {
		kmsrv.deleteById(maKM);
	}
}
