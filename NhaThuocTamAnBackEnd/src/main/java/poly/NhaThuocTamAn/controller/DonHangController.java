package poly.NhaThuocTamAn.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.DonHangDTO;

import poly.NhaThuocTamAn.model.DonHang;
import poly.NhaThuocTamAn.model.LichSuDonHang;
import poly.NhaThuocTamAn.service.DonHangService;

@RestController
@RequestMapping("/api/donhang")
@CrossOrigin("*")
public class DonHangController {

    @Autowired
    private DonHangService donHangService;
    
    // ✅ Tạo đơn hàng
    @PostMapping("/create")
    public ResponseEntity<DonHang> create(@RequestBody DonHangDTO dto) {
        DonHang result = donHangService.create(dto);
        return ResponseEntity.ok(result);
    }

    // ✅ Cập nhật trạng thái
    @PutMapping("/update-status")
    public ResponseEntity<DonHang> updateStatus(
            @RequestParam("id") Integer maDonHang,
            @RequestParam("status") Integer trangThaiMoi,
            @RequestParam(value = "lyDo", required = false) String lyDoHuy) {
        DonHang updated = donHangService.updateStatus(maDonHang, trangThaiMoi, lyDoHuy);
        return ResponseEntity.ok(updated);
    }


    // ✅ Lấy danh sách tất cả đơn hàng
    @GetMapping
    public ResponseEntity<List<DonHang>> getAll() {
        return ResponseEntity.ok(donHangService.findAll());
    }

    // ✅ Lấy chi tiết đơn hàng theo ID
    @GetMapping("/{id}")
    public ResponseEntity<DonHang> getById(@PathVariable("id") Integer id) {
        DonHang dh = donHangService.findById(id);
        return ResponseEntity.ok(dh);
    }

    // ✅ Lấy lịch sử trạng thái đơn hàng
    @GetMapping("/lich-su/{id}")
    public ResponseEntity<List<LichSuDonHang>> getLichSu(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(donHangService.getLichSu(id));
    }
    
    @PutMapping("/cap-nhat-thanh-toan")
    public ResponseEntity<?> capNhatThanhToan(
        @RequestParam("id") Integer maDonHang,
        @RequestParam("daThanhToan") boolean daThanhToan
    ) {
        donHangService.capNhatDaThanhToan(maDonHang, daThanhToan);
        return ResponseEntity.ok().build();
    }
    
 // ✅ Lấy danh sách đơn hàng theo mã khách hàng
    @GetMapping("/khach-hang/{maKH}")
    public ResponseEntity<List<DonHangDTO>> getByMaKhachHang(@PathVariable("maKH") Integer maKH) {
        return ResponseEntity.ok(donHangService.findByMaKhachHang(maKH));
    }

 // ✅ Lấy đơn hàng DTO theo ID (đã có đầy đủ tên người nhận, giảm giá,...)
    @GetMapping("/dto/{id}")
    public ResponseEntity<DonHangDTO> getDTOById(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(donHangService.findDTOById(id));
    }

}
