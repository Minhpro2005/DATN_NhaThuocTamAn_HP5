package poly.NhaThuocTamAn.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import poly.NhaThuocTamAn.model.NhaCungCap;
import poly.NhaThuocTamAn.service.NhaCungCapService;

@RestController
@RequestMapping("/api/nha-cung-cap")
public class NhaCungCapController {
	@Autowired
	private NhaCungCapService nccsrv;
	
	//Get all
	@GetMapping
	public List<NhaCungCap> getAll(){
		return nccsrv.getAll();
	}
	
	//Get by ID
	@GetMapping("/{maNCC}")
	public NhaCungCap getById(@PathVariable Integer maNCC) {
		return nccsrv.getById(maNCC);
	}
	
	//Add
	@PostMapping
	public NhaCungCap add(@RequestBody NhaCungCap ncc) {
		return nccsrv.add(ncc);
	}
	
	//Update
	@PutMapping("/{maNCC}")
	public NhaCungCap update(@PathVariable Integer maNCC, @RequestBody NhaCungCap ncc) {
		return nccsrv.update(maNCC, ncc);
	}
	
	//Delete
	@DeleteMapping("/{maNCC}")
	public void delete(@PathVariable Integer maNCC) {
		nccsrv.deleteById(maNCC);
	}
}
