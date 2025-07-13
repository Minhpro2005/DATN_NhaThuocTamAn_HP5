package poly.NhaThuocTamAn.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.AnhThuocDTO;

import poly.NhaThuocTamAn.service.AnhThuocService;

@RestController
@RequestMapping("/api/anh-thuoc")
@CrossOrigin("*")
public class AnhThuocController {

    @Autowired
    private AnhThuocService service;

    @GetMapping
    public List<AnhThuocDTO> getAll() {
        return service.getAll().stream().map(a -> AnhThuocDTO.builder()
                .maAnhThuoc(a.getMaAnhThuoc())
                .hinhAnh(a.getHinhAnh())
                .anhChinh(a.getAnhChinh())
                .maThuoc(a.getThuoc().getMaThuoc())
                .build()).toList();
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody AnhThuocDTO dto) {
        try {
            return ResponseEntity.ok(service.save(dto));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }


    @PutMapping
    public ResponseEntity<?> update(@RequestBody AnhThuocDTO dto) {
        try {
            return ResponseEntity.ok(service.save(dto));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }


    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        service.deleteById(id);
    }

    @PostMapping("/upload")
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(service.uploadAnh(file));
    }
}
