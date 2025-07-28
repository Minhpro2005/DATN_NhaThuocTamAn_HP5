package poly.NhaThuocTamAn.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import com.example.demo.dto.KhuyenMaiDTO;
import poly.NhaThuocTamAn.service.KhuyenMaiService;

@RestController
@RequestMapping("/api/khuyen-mai")
@CrossOrigin(origins = "*")
public class KhuyenMaiController {
	@Autowired
	private KhuyenMaiService kmsrv;
	
	//Get all
	@GetMapping
	public List<KhuyenMaiDTO> getAll(){
		return kmsrv.getAll();
	}
	
	//Get by ID
	@GetMapping("/{id}")
	public ResponseEntity<KhuyenMaiDTO> getById(@PathVariable Integer id) {
		return ResponseEntity.ok(kmsrv.getById(id));
	}
	
	//Add
	@PostMapping
	public ResponseEntity<KhuyenMaiDTO> add(@RequestBody KhuyenMaiDTO km) {
		return ResponseEntity.ok(kmsrv.create(km));
	}
	
	//Update
	@PutMapping("/{id}")
	public ResponseEntity<KhuyenMaiDTO> update(@PathVariable Integer id, @RequestBody KhuyenMaiDTO km) {
		return ResponseEntity.ok(kmsrv.update(id, km));
	}
	
	//Delete
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		kmsrv.delete(id);
		return ResponseEntity.ok().build();
	}
}
