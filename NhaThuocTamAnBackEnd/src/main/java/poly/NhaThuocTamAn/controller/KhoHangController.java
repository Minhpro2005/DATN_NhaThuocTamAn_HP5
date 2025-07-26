package poly.NhaThuocTamAn.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.demo.dto.KhoCapNhatRequest;
import com.example.demo.dto.KhoHangDTO;

import poly.NhaThuocTamAn.model.BienTheThuoc;
import poly.NhaThuocTamAn.model.Thuoc;
import poly.NhaThuocTamAn.repository.BienTheThuocRepository;
import poly.NhaThuocTamAn.repository.ThuocRepository;
import poly.NhaThuocTamAn.service.KhoHangService;

@RestController
@RequestMapping("/api/kho-hang")
@CrossOrigin("*")
public class KhoHangController {
	@Autowired
	private KhoHangService khosrv;
	
	@Autowired
    private ThuocRepository thuocrep;

    @Autowired
    private BienTheThuocRepository bientherep;
	
	//Get all
	@GetMapping
	public List<KhoHangDTO> getAll(){
		return khosrv.getAll();
	}
	
	//Search
    @GetMapping("/search")
    public List<KhoHangDTO> searchKho(@RequestParam String keyword) {
        return khosrv.search(keyword);
    }
	
    //Update
    @PostMapping("/cap-nhat")
    public ResponseEntity<?> capNhatKho(@RequestBody KhoCapNhatRequest req) {
        try {
            Thuoc thuoc = null;
            BienTheThuoc bienThe = null;

            if (req.getMaThuoc() != null) {
                thuoc = thuocrep.findById(req.getMaThuoc()).orElse(null);
            }
            if (req.getMaBienThe() != null) {
                bienThe = bientherep.findById(req.getMaBienThe()).orElse(null);
            }

            if (thuoc == null && bienThe == null) {
                return ResponseEntity.badRequest().body("❌ Vui lòng cung cấp mã thuốc hoặc mã biến thể!");
            }

            khosrv.themHoacCapNhat(thuoc, bienThe, req.getSoLuongThem());
            return ResponseEntity.ok("✅ Cập nhật kho thành công!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("❌ Server lỗi: " + e.getMessage());
        }
    }
    
    @GetMapping("/so-luong-ton")
    public ResponseEntity<Integer> getSoLuongTon(
        @RequestParam(required = false) Integer maThuoc,
        @RequestParam(required = false) Integer maBienThe
    ) {
        int soLuong = khosrv.getSoLuongTon(maThuoc, maBienThe);
        return ResponseEntity.ok(soLuong);
    }

}
