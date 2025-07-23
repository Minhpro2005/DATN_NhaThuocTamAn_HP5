package poly.NhaThuocTamAn.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.NhaCungCapDTO;
import poly.NhaThuocTamAn.service.NhaCungCapService;

@RestController
@RequestMapping("/api/nha-cung-cap")
public class NhaCungCapController {
	@Autowired
	private NhaCungCapService nccsrv;
	
	@PostMapping
    public NhaCungCapDTO create(@RequestBody NhaCungCapDTO dto) {
        return nccsrv.create(dto);
    }

    @PutMapping("/{maNCC}")
    public NhaCungCapDTO update(@PathVariable Integer maNCC, @RequestBody NhaCungCapDTO dto) {
        return nccsrv.update(maNCC, dto);
    }

    @DeleteMapping("/{maNCC}")
    public void delete(@PathVariable Integer maNCC) {
        nccsrv.delete(maNCC);
    }

    @GetMapping("/{maNCC}")
    public NhaCungCapDTO getById(@PathVariable Integer maNCC) {
        return nccsrv.getById(maNCC);
    }

    @GetMapping
    public List<NhaCungCapDTO> getAll() {
        return nccsrv.getAll();
    }

    @GetMapping("/search")
    public List<NhaCungCapDTO> search(@RequestParam String tenNCC) {
        return nccsrv.searchByTenNCC(tenNCC);
    }

}
