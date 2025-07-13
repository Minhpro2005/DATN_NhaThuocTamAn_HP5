package poly.NhaThuocTamAn.controller;

import com.example.demo.dto.NhaCungCapDTO;

import poly.NhaThuocTamAn.service.NhaCungCapService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/nhacungcap")
@CrossOrigin("*")
public class NhaCungCapController {

    @Autowired
    private NhaCungCapService nhaCungCapService;

    @PostMapping
    public NhaCungCapDTO create(@RequestBody NhaCungCapDTO dto) {
        return nhaCungCapService.create(dto);
    }

    @PutMapping("/{maNCC}")
    public NhaCungCapDTO update(@PathVariable Integer maNCC, @RequestBody NhaCungCapDTO dto) {
        return nhaCungCapService.update(maNCC, dto);
    }

    @DeleteMapping("/{maNCC}")
    public void delete(@PathVariable Integer maNCC) {
        nhaCungCapService.delete(maNCC);
    }

    @GetMapping("/{maNCC}")
    public NhaCungCapDTO getById(@PathVariable Integer maNCC) {
        return nhaCungCapService.getById(maNCC);
    }

    @GetMapping
    public List<NhaCungCapDTO> getAll() {
        return nhaCungCapService.getAll();
    }

    @GetMapping("/search")
    public List<NhaCungCapDTO> search(@RequestParam String tenNCC) {
        return nhaCungCapService.searchByTenNCC(tenNCC);
    }
}

