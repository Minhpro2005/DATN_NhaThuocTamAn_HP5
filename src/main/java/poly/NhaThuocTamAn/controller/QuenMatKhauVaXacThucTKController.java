package poly.NhaThuocTamAn.controller;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.DatLaiMatKhauDTO;
import com.example.demo.dto.EmailDTO;
import com.example.demo.dto.MaXacThucDTO;

import poly.NhaThuocTamAn.model.KhachHang;
import poly.NhaThuocTamAn.repository.KhachHangRepository;
import poly.NhaThuocTamAn.service.EmailService;

@RestController
@RequestMapping("/api/forgot")
@CrossOrigin("*")
public class QuenMatKhauVaXacThucTKController {

    @Autowired
    private KhachHangRepository repo;

    @Autowired
    private EmailService emailService;

    @PostMapping("/send-code")
    public ResponseEntity<?> sendCode(@RequestBody EmailDTO dto) {
        Optional<KhachHang> opt = repo.findByEmail(dto.getEmail());
        if (opt.isEmpty()) return ResponseEntity.badRequest().body(Map.of("message", "Email không tồn tại"));

        String code = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        KhachHang kh = opt.get();
        kh.setMaXacThuc(code);
        kh.setThoiGianHetHanMa(LocalDateTime.now().plusMinutes(10));
        repo.save(kh);

        emailService.sendCode(dto.getEmail(), "Khôi phục mật khẩu", "Mã xác thực: " + code);
        return ResponseEntity.ok(Map.of("message", "Đã gửi mã xác thực qua email"));
    }

    @PostMapping("/verify-code")
    public ResponseEntity<?> verifyCode(@RequestBody MaXacThucDTO dto) {
        Optional<KhachHang> opt = repo.findByEmail(dto.getEmail());
        if (opt.isEmpty()) return ResponseEntity.badRequest().body(Map.of("message", "Email không tồn tại"));

        KhachHang kh = opt.get();
        if (!kh.getMaXacThuc().equals(dto.getCode()))
            return ResponseEntity.badRequest().body(Map.of("message", "Mã xác thực không đúng"));
        if (kh.getThoiGianHetHanMa().isBefore(LocalDateTime.now()))
            return ResponseEntity.badRequest().body(Map.of("message", "Mã đã hết hạn"));

        kh.setDaXacThuc(true);
        repo.save(kh);
        return ResponseEntity.ok(Map.of("message", "Mã hợp lệ, tiếp tục đặt lại mật khẩu"));
    }

    @PostMapping("/reset-pass")
    public ResponseEntity<?> resetPass(@RequestBody DatLaiMatKhauDTO dto) {
        Optional<KhachHang> opt = repo.findByEmail(dto.getEmail());
        if (opt.isEmpty())
            return ResponseEntity.badRequest().body(Map.of("message", "Email không tồn tại"));

        KhachHang kh = opt.get();
        if (!kh.getDaXacThuc())
            return ResponseEntity.badRequest().body(Map.of("message", "Chưa xác thực mã"));

        kh.setMatKhau(dto.getNewPassword());
        kh.setMaXacThuc(null);
        kh.setThoiGianHetHanMa(null);

        repo.save(kh);

        return ResponseEntity.ok(Map.of("message", "Đặt lại mật khẩu thành công"));
    }

}

