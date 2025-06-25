package poly.NhaThuocTamAn.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import poly.NhaThuocTamAn.model.TinTuc;
import poly.NhaThuocTamAn.service.TinTucService;

@RestController
@RequestMapping("/api/tin-tuc")
public class TinTucController {
	@Autowired
	private TinTucService tinsrv;
	
	//Get all
	@GetMapping
	public List<TinTuc> getAll(){
		return tinsrv.getAll();
	}
	
	//Get by ID
	@GetMapping("/{maTin}")
	public TinTuc getById(@PathVariable Integer maTin) {
		return tinsrv.getById(maTin);
	}
	
	//Add
	@PostMapping
	public TinTuc add(@RequestBody TinTuc tin) {
		return tinsrv.add(tin);
	}
	
	//Update
	@PutMapping("/{maTin}")
	public TinTuc update(@PathVariable Integer maTin, @RequestBody TinTuc tin) {
		return tinsrv.update(maTin, tin);
	}
	
	//Delete
	@DeleteMapping("/{maTin}")
	public void delete(@PathVariable Integer maTin) {
		tinsrv.deleteById(maTin);
	}
}
