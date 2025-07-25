package poly.NhaThuocTamAn.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.NhaCungCapDTO;
import poly.NhaThuocTamAn.service.NhaCungCapService;

@RestController
@RequestMapping("/api/nha-cung-cap")
@CrossOrigin("*")
public class NhaCungCapController {
	@Autowired
	private NhaCungCapService nccsrv;
	
	// Get all
    @GetMapping
    public List<NhaCungCapDTO> getAll() {
        return nccsrv.getAll();
    }
    
    // Get by ID
    @GetMapping("/{maNCC}")
    public NhaCungCapDTO getById(@PathVariable Integer maNCC) {
        return nccsrv.getById(maNCC);
    }
    
    // Search
    @GetMapping("/search")
    public List<NhaCungCapDTO> search(@RequestParam String tenNCC) {
        return nccsrv.searchByTenNCC(tenNCC);
    }
    
	// Create
	@PostMapping
    public NhaCungCapDTO create(@RequestBody NhaCungCapDTO dto) {
        return nccsrv.create(dto);
    }
	
	// Update
    @PutMapping("/{maNCC}")
    public NhaCungCapDTO update(@PathVariable Integer maNCC, @RequestBody NhaCungCapDTO dto) {
        return nccsrv.update(maNCC, dto);
    }
    
    // Delete
    @DeleteMapping("/{maNCC}")
    public void delete(@PathVariable Integer maNCC) {
        nccsrv.delete(maNCC);
    }
    
    

}
