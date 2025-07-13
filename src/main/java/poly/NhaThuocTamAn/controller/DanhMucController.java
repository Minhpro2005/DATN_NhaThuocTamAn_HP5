package poly.NhaThuocTamAn.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.DanhMucDTO;

import poly.NhaThuocTamAn.service.DanhMucService;

import java.util.List;

@RestController
@RequestMapping("/api/danhmuc")
@CrossOrigin("*")
public class DanhMucController {

    @Autowired
    private DanhMucService danhMucService;

    @PostMapping
    public DanhMucDTO create(@RequestBody DanhMucDTO dto) {
        return danhMucService.create(dto);
    }

    @PutMapping("/{maDM}")
    public DanhMucDTO update(@PathVariable Integer maDM, @RequestBody DanhMucDTO dto) {
        return danhMucService.update(maDM, dto);
    }

    @DeleteMapping("/{maDM}")
    public void delete(@PathVariable Integer maDM) {
        danhMucService.delete(maDM);
    }

    @GetMapping("/{maDM}")
    public DanhMucDTO getById(@PathVariable Integer maDM) {
        return danhMucService.getById(maDM);
    }

    @GetMapping
    public List<DanhMucDTO> getAll() {
        return danhMucService.getAll();
    }

    @GetMapping("/search")
    public List<DanhMucDTO> search(@RequestParam String tenDanhMuc) {
        return danhMucService.searchByTenDanhMuc(tenDanhMuc);
    }
}

