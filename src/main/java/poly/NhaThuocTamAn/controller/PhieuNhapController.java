package poly.NhaThuocTamAn.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.PhieuNhapDTO;

import poly.NhaThuocTamAn.model.PhieuNhap;
import poly.NhaThuocTamAn.model.PhieuNhapChiTiet;
import poly.NhaThuocTamAn.service.PhieuNhapService;

@RestController
@RequestMapping("/api/phieu-nhap")
@CrossOrigin("*")
public class PhieuNhapController {

    @Autowired
    private PhieuNhapService phieuNhapService;

    // ✅ Tạo mới phiếu nhập
    @PostMapping
    public PhieuNhap create(@RequestBody PhieuNhapDTO dto) {
        return phieuNhapService.create(dto); // ✅ dùng "create", đúng tên
    }


    // ✅ Lấy toàn bộ phiếu nhập
    @GetMapping
    public List<PhieuNhap> getAll() {
        return phieuNhapService.getAll();
    }

    // ✅ Lấy danh sách chi tiết theo mã phiếu nhập
    @GetMapping("/{maPN}/chi-tiet")
    public List<PhieuNhapChiTiet> getChiTiet(@PathVariable int maPN) {
        return phieuNhapService.getChiTietByPhieuNhap(maPN);
    }
}
