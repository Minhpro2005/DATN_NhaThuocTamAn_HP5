package poly.NhaThuocTamAn.controller;

import com.example.demo.dto.DanhGiaDTO;
import poly.NhaThuocTamAn.repository.DanhGiaRepository;
import poly.NhaThuocTamAn.service.DanhGiaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/danh-gia")
public class DanhGiaController {

    @Autowired
    private DanhGiaService danhGiaService;

    @Autowired
    private DanhGiaRepository danhGiaRepository;

    // ✅ Lấy danh sách đánh giá theo biến thể thuốc
    @GetMapping("/bien-the/{maBienThe}")
    public List<DanhGiaDTO> getByBienThe(@PathVariable Integer maBienThe) {
        return danhGiaService.getDanhGiaByBienThe(maBienThe);
    }

    // ✅ Lấy danh sách đánh giá theo thuốc (không có biến thể)
    @GetMapping("/thuoc/{maThuoc}")
    public List<DanhGiaDTO> getByThuoc(@PathVariable Integer maThuoc) {
        return danhGiaService.getDanhGiaByThuoc(maThuoc);
    }

    // ✅ Gửi đánh giá không có ảnh
    @PostMapping
    public DanhGiaDTO add(@RequestBody DanhGiaDTO dto) {
        return danhGiaService.addDanhGia(dto);
    }

    // ✅ Gửi đánh giá có ảnh (multipart/form-data)
    @PostMapping("/upload")
    public DanhGiaDTO upload(
        @RequestParam("maKH") Integer maKH,
        @RequestParam(value = "maBienThe", required = false) Integer maBienThe,
        @RequestParam(value = "maThuoc", required = false) Integer maThuoc,
        @RequestParam("soSao") Integer soSao,
        @RequestParam(value = "binhLuan", required = false) String binhLuan,
        @RequestParam(value = "hinhAnh", required = false) MultipartFile hinhAnh
    ) throws IOException {
        String imageUrl = null;
        if (hinhAnh != null && !hinhAnh.isEmpty()) {
            String filename = UUID.randomUUID() + "_" + hinhAnh.getOriginalFilename();
            Path uploadPath = Paths.get("uploads");
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            Path filePath = uploadPath.resolve(filename);
            Files.copy(hinhAnh.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            imageUrl = "/images/" + filename;
        }

        DanhGiaDTO dto = new DanhGiaDTO();
        dto.setMaKH(maKH);
        dto.setMaBienThe(maBienThe);
        dto.setMaThuoc(maThuoc);
        dto.setSoSao(soSao);
        dto.setBinhLuan(binhLuan);
        dto.setHinhAnh(imageUrl);

        return danhGiaService.addDanhGia(dto);
    }

    // ✅ Cập nhật đánh giá
    @PutMapping("/{maDG}")
    public DanhGiaDTO update(@PathVariable Integer maDG, @RequestBody DanhGiaDTO dto) {
        return danhGiaService.updateDanhGia(maDG, dto);
    }

    // ✅ Xoá đánh giá
    @DeleteMapping("/{maDG}")
    public void delete(@PathVariable Integer maDG) {
        danhGiaService.deleteDanhGia(maDG);
    }

    // ✅ Cập nhật có ảnh
    @PutMapping("/upload/{maDG}")
    public DanhGiaDTO updateWithImage(
        @PathVariable Integer maDG,
        @RequestParam(value = "soSao", required = false) Integer soSao,
        @RequestParam(value = "binhLuan", required = false) String binhLuan,
        @RequestParam(value = "hinhAnh", required = false) MultipartFile hinhAnh
    ) throws IOException {
        String imageUrl = null;
        if (hinhAnh != null && !hinhAnh.isEmpty()) {
            String filename = UUID.randomUUID() + "_" + hinhAnh.getOriginalFilename();
            Path uploadPath = Paths.get("uploads");
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            Path filePath = uploadPath.resolve(filename);
            Files.copy(hinhAnh.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            imageUrl = "/images/" + filename;
        }

        DanhGiaDTO dto = new DanhGiaDTO();
        dto.setSoSao(soSao);
        dto.setBinhLuan(binhLuan);
        dto.setHinhAnh(imageUrl);

        return danhGiaService.updateDanhGia(maDG, dto);
    }

    // ✅ Kiểm tra đã từng đánh giá sản phẩm chưa (theo maBienThe hoặc maThuoc)
    @GetMapping("/da-danh-gia")
    public boolean daDanhGia(@RequestParam Integer maKH,
                             @RequestParam(required = false) Integer maBienThe,
                             @RequestParam(required = false) Integer maThuoc) {
        if (maBienThe != null) {
            return danhGiaRepository.existsByKhachHang_MaKHAndBienTheThuoc_MaBienThe(maKH, maBienThe);
        } else if (maThuoc != null) {
            return danhGiaRepository.existsByKhachHang_MaKHAndThuoc_MaThuoc(maKH, maThuoc);
        }
        return false;
    }
    
    @GetMapping
    public List<DanhGiaDTO> getAll() {
    	return danhGiaService.getAllDanhGia(); // ✅ đảm bảo trả về List<DanhGiaDTO>

    }

}
