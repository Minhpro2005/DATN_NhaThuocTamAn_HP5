package poly.NhaThuocTamAn.controller;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.DangKyDTO;
import com.example.demo.dto.DangNhapDTO;
import com.example.demo.dto.MaXacThucDTO;

import poly.NhaThuocTamAn.model.KhachHang;
import poly.NhaThuocTamAn.model.NhanVien;
import poly.NhaThuocTamAn.repository.KhachHangRepository;
import poly.NhaThuocTamAn.repository.NhanVienRepository;
import poly.NhaThuocTamAn.service.EmailService;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin("*")
public class AuthController {

    @Autowired
    private KhachHangRepository khachHangRepo;

    @Autowired
    private NhanVienRepository nhanVienRepo;

    @Autowired
    private EmailService emailService;

    // Đăng ký KH
    @PostMapping("/dang-ky")
    public ResponseEntity<?> dangKy(@RequestBody DangKyDTO dto) {
        if (khachHangRepo.existsByEmail(dto.getEmail())) {
            return ResponseEntity.badRequest().body(Map.of("message", "Email đã được đăng ký tài khoản"));
        }

        String code = UUID.randomUUID().toString().substring(0, 6).toUpperCase();

        KhachHang kh = KhachHang.builder()
            .hoTen(dto.getHoTen())
            .email(dto.getEmail())
            .matKhau(dto.getMatKhau())
            .maXacThuc(code)
            .thoiGianHetHanMa(LocalDateTime.now().plusMinutes(10))
            .trangThai(true)
            .daXacThuc(false)
            .build();

        khachHangRepo.save(kh);

        emailService.sendCode(dto.getEmail(), "Mã xác thực đăng ký", "Mã xác thực của bạn là: " + code);
        return ResponseEntity.ok(Map.of("message", "Đăng ký thành công! Mã xác thực đã gửi email."));
    }

    // ✅ Đăng nhập cho cả KH và NV
    @PostMapping("/dang-nhap")
    public ResponseEntity<?> dangNhap(@RequestBody DangNhapDTO dto) {
        // 1. Check khách hàng
        Optional<KhachHang> optKH = khachHangRepo.findByEmailAndMatKhau(dto.getEmail(), dto.getMatKhau());
        if (optKH.isPresent()) {
            KhachHang kh = optKH.get();

            if (!kh.getTrangThai()) {
                return ResponseEntity.status(403).body(Map.of("message", "❌ Tài khoản của bạn đang bị khóa!"));
            }

            if (!kh.getDaXacThuc()) {
                return ResponseEntity.status(403).body(Map.of(
                    "message", "❌ Tài khoản chưa được xác thực!",
                    "email", kh.getEmail()
                ));
            }

            return ResponseEntity.ok(Map.of(
                "maKH", kh.getMaKH(),
                "hoTen", kh.getHoTen(),
                "email", kh.getEmail(),
                "hinhAnh", kh.getHinhAnh() != null ? kh.getHinhAnh() : "https://cdn-icons-png.flaticon.com/512/149/149071.png",

                "vaiTro", 3 // vaiTro khách hàng
            ));
        }

        // 2. Check nhân viên
        Optional<NhanVien> optNV = nhanVienRepo.findByEmailAndMatKhau(dto.getEmail(), dto.getMatKhau());
        if (optNV.isPresent()) {
            NhanVien nv = optNV.get();

            if (!nv.getTrangThai()) {
                return ResponseEntity.status(403).body(Map.of("message", "❌ Tài khoản nhân viên bị khóa!"));
            }

            return ResponseEntity.ok(Map.of(
                "id", nv.getMaNV(),
                "hoTen", nv.getHoTen(),
                "email", nv.getEmail(),
                "hinhAnh", nv.getHinhAnh() != null ? nv.getHinhAnh() : "https://cdn-icons-png.flaticon.com/512/149/149071.png",
                "vaiTro", nv.getVaiTro() // 1 = admin, 2 = nhân viên
            ));
        }

        return ResponseEntity.status(401).body(Map.of("message", "Email hoặc mật khẩu không đúng!"));
    }

    // Xác thực email
    @PostMapping("/xac-thuc-email")
    public ResponseEntity<?> xacThucEmail(@RequestBody MaXacThucDTO dto) {
        Optional<KhachHang> opt = khachHangRepo.findByEmail(dto.getEmail());
        if (opt.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Email không tồn tại"));
        }

        KhachHang kh = opt.get();
        if (!kh.getMaXacThuc().equals(dto.getCode())) {
            return ResponseEntity.badRequest().body(Map.of("message", "Mã xác thực không đúng"));
        }

        if (kh.getThoiGianHetHanMa().isBefore(LocalDateTime.now())) {
            return ResponseEntity.badRequest().body(Map.of("message", "Mã xác thực đã hết hạn"));
        }

        kh.setDaXacThuc(true);
        kh.setMaXacThuc(null);
        kh.setThoiGianHetHanMa(null);
        khachHangRepo.save(kh);

        return ResponseEntity.ok(Map.of("message", "Xác thực thành công!"));
    }
}
