package poly.NhaThuocTamAn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.NhanVienDTO;

import poly.NhaThuocTamAn.service.NhanVienService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/nhanvien")
@CrossOrigin("*")
public class NhanVienController {

    @Autowired
    private NhanVienService service;

    @GetMapping
    public List<NhanVienDTO> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public NhanVienDTO getById(@PathVariable Integer id) {
        return service.getById(id);
    }

    @PostMapping
    public NhanVienDTO create(@RequestBody NhanVienDTO dto) {
        return service.create(dto);
    }

    @PutMapping("/{id}")
    public NhanVienDTO update(@PathVariable Integer id, @RequestBody NhanVienDTO dto) {
        return service.update(id, dto);
    }

    @PutMapping("/{id}/trang-thai")
    public NhanVienDTO updateStatus(@PathVariable Integer id, @RequestParam Boolean status) {
        return service.updateStatus(id, status);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        service.delete(id);
    }

    @GetMapping("/search")
    public List<NhanVienDTO> search(@RequestParam(required = false) String keyword) {
        return service.search(keyword);
    }

    @PostMapping("/{id}/avatar")
    public NhanVienDTO uploadAvatar(@PathVariable Integer id, @RequestParam("file") MultipartFile file) throws IOException {
        return service.uploadAvatar(id, file);
    }
}
