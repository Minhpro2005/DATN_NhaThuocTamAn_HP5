package poly.NhaThuocTamAn.controller;

import com.example.demo.dto.BienTheThuocDTO;

import poly.NhaThuocTamAn.service.BienTheThuocService;
import poly.NhaThuocTamAn.service.FileStorageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/bienthe")
@CrossOrigin
public class BienTheThuocController {

	@Autowired
	private BienTheThuocService bienTheService;

    @Autowired
    private FileStorageService fileStorageService;

    @GetMapping
    public List<BienTheThuocDTO> getAll() {
        return bienTheService.getAll();
    }

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<BienTheThuocDTO> create(
        @RequestPart("data") BienTheThuocDTO dto,
        @RequestPart(value = "file", required = false) MultipartFile file) {
        return ResponseEntity.ok(bienTheService.save(dto, file));
    }

    @PutMapping(value = "/{id}", consumes = "multipart/form-data")
    public ResponseEntity<BienTheThuocDTO> update(
        @PathVariable Integer id,
        @RequestPart("data") BienTheThuocDTO dto,
        @RequestPart(value = "file", required = false) MultipartFile file) {
        dto.setMaBienThe(id); // ensure ID is used
        return ResponseEntity.ok(bienTheService.save(dto, file));
    }


    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        bienTheService.delete(id);
    }

    // Upload ảnh
    @PostMapping("/upload")
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(fileStorageService.saveFile(file, "bienthethuoc"));
    }
    
    @GetMapping("/thuoc/{maThuoc}")
    public ResponseEntity<List<BienTheThuocDTO>> getByThuoc(@PathVariable Integer maThuoc) {
        return ResponseEntity.ok(bienTheService.findByThuoc(maThuoc));
    }

   
}
