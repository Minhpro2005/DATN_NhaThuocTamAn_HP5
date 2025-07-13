package poly.NhaThuocTamAn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.DoiMatKhauRequest;
import com.example.demo.dto.KhachHangDTO;

import poly.NhaThuocTamAn.model.KhachHang;
import poly.NhaThuocTamAn.repository.KhachHangRepository;
import poly.NhaThuocTamAn.service.KhachHangService;

import java.util.List;

@RestController
@RequestMapping("/api/khachhang")
@CrossOrigin("*")
public class KhachHangController {
	
	@Autowired
	private KhachHangRepository khachHangRepository;

    @Autowired
    private KhachHangService service;

    @GetMapping
    public List<KhachHangDTO> getAll() {
        return service.getAll();
    }

    @GetMapping("/search")
    public List<KhachHangDTO> search(
            @RequestParam(required = false) String hoTen,
            @RequestParam(required = false) String sdt,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String diaChi) {
        return service.search(hoTen, sdt, email, diaChi);
    }

    @GetMapping("/{id}")
    public KhachHangDTO getById(@PathVariable Integer id) {
        return service.getById(id);
    }

    @PutMapping("/trang-thai/{id}")
    public KhachHangDTO updateStatus(@PathVariable Integer id, @RequestParam boolean status) {
        return service.updateStatus(id, status);
    }
    
    @PutMapping("/{id}")
    public KhachHangDTO updateInfo(@PathVariable Integer id, @RequestBody KhachHangDTO dto) {
        return service.update(id, dto);
    }
    
    @PostMapping("/{id}/avatar")
    public KhachHangDTO uploadAvatar(@PathVariable Integer id, @RequestParam("file") MultipartFile file) {
        return service.uploadAvatar(id, file);
    }
    
 // API đổi mật khẩu
    @PutMapping("/{maKH}/doi-mat-khau")
    public ResponseEntity<?> doiMatKhau(@PathVariable Integer maKH, @RequestBody DoiMatKhauRequest request) {
        KhachHang kh = khachHangRepository.findById(maKH).orElse(null);
        if (kh == null) {
            return ResponseEntity.notFound().build();
        }

        if (!kh.getMatKhau().equals(request.getOldPassword())) {
            return ResponseEntity.badRequest().body("Mật khẩu hiện tại không đúng");
        }

        kh.setMatKhau(request.getNewPassword());
        khachHangRepository.save(kh);

        return ResponseEntity.ok("Đổi mật khẩu thành công");
    }

}

