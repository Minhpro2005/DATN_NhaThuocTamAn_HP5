package poly.NhaThuocTamAn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.KhuyenMaiDTO;

import poly.NhaThuocTamAn.service.KhuyenMaiService;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/khuyen-mai")
@CrossOrigin(origins = "*") // Cho phép gọi từ frontend Vue
public class KhuyenMaiController {

    @Autowired
    private KhuyenMaiService khuyenMaiService;

    // 🔍 Lấy toàn bộ danh sách khuyến mãi
    @GetMapping
    public List<KhuyenMaiDTO> getAll() {
        return khuyenMaiService.getAll();
    }

    // 🔍 Lấy theo ID
    @GetMapping("/{id}")
    public ResponseEntity<KhuyenMaiDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(khuyenMaiService.getById(id));
    }

    // ➕ Thêm mới khuyến mãi
    @PostMapping
    public ResponseEntity<KhuyenMaiDTO> create(@RequestBody KhuyenMaiDTO dto) {
        return ResponseEntity.ok(khuyenMaiService.create(dto));
    }

    // ✏️ Cập nhật khuyến mãi
    @PutMapping("/{id}")
    public ResponseEntity<KhuyenMaiDTO> update(@PathVariable Integer id, @RequestBody KhuyenMaiDTO dto) {
        return ResponseEntity.ok(khuyenMaiService.update(id, dto));
    }

    // ❌ Xoá khuyến mãi
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        khuyenMaiService.delete(id);
        return ResponseEntity.ok().build();
    }

    // ✅ Áp dụng mã giảm giá
    @GetMapping("/ap-dung")
    public ResponseEntity<KhuyenMaiDTO> apDungVoucher(
            @RequestParam String code,
            @RequestParam BigDecimal tongTien) {
        return khuyenMaiService.apDungVoucher(code, tongTien)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }
}
