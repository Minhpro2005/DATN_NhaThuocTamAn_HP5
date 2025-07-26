package poly.NhaThuocTamAn.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.KhoHangDTO;

import jakarta.transaction.Transactional;
import poly.NhaThuocTamAn.model.KhoHang;
import poly.NhaThuocTamAn.model.Thuoc;
import poly.NhaThuocTamAn.model.BienTheThuoc;
import poly.NhaThuocTamAn.repository.KhoHangRepository;
import poly.NhaThuocTamAn.service.KhoHangService;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class KhoHangServiceImpl implements KhoHangService {

    @Autowired
    private KhoHangRepository khoHangRepo;

    public List<KhoHangDTO> getAll() {
        return khoHangRepo.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<KhoHangDTO> search(String keyword) {
        String kw = "%" + keyword.toLowerCase() + "%";
        return khoHangRepo.searchByKeyword(kw)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void themHoacCapNhat(Thuoc thuoc, BienTheThuoc bienThe, int soLuongThem) {
        if (thuoc == null && bienThe == null) {
            throw new IllegalArgumentException("Kho hàng phải có Thuốc hoặc Biến Thể Thuốc.");
        }

        KhoHang kho = null;

        if (thuoc != null) {
            kho = khoHangRepo.findByThuoc(thuoc).orElse(null);
        } else {
            kho = khoHangRepo.findByBienTheThuoc(bienThe).orElse(null);
        }

        if (kho == null) {
            kho = new KhoHang();
            kho.setSoLuongTon(0);
            kho.setNgayCapNhat(null);
            kho.setThuoc(thuoc);
            kho.setBienTheThuoc(bienThe);
        }

        int soLuongHienTai = kho.getSoLuongTon() != null ? kho.getSoLuongTon() : 0;
        int soLuongMoi = soLuongHienTai + soLuongThem;

        // ✅ Kiểm tra không cho âm kho
        if (soLuongMoi < 0) {
            throw new IllegalStateException("Không đủ hàng tồn kho. Hiện tại còn: " + soLuongHienTai);
        }

        kho.setSoLuongTon(soLuongMoi);
        kho.setNgayCapNhat(null);

        // ✅ Cập nhật trạng thái tồn kho
        if (soLuongMoi == 0) {
            kho.setTrangThai(2); // Hết hàng
        } else if (soLuongMoi <= 10) {
            kho.setTrangThai(1); // Sắp hết
        } else {
            kho.setTrangThai(0); // Còn hàng
        }

        khoHangRepo.save(kho);
    }

    // ✅ Convert sang DTO
    private KhoHangDTO toDTO(KhoHang k) {
        return KhoHangDTO.builder()
                .maKhoHang(k.getMaKhoHang())
                .maThuoc(k.getThuoc() != null ? ((KhoHangDTO) k.getThuoc()).getMaThuoc() : null)
                .tenThuoc(k.getThuoc() != null ? ((KhoHangDTO) k.getThuoc()).getTenThuoc() : null)
                .maBienThe(k.getBienTheThuoc() != null ? ((KhoHangDTO) k.getBienTheThuoc()).getMaBienThe() : null)
                .tenBienThe(k.getBienTheThuoc() != null ? ((KhoHangDTO) k.getBienTheThuoc()).getTenBienThe() : null)
                .soLuongTon(k.getSoLuongTon())
                .ngayCapNhat(k.getNgayCapNhat())
                .ghiChu(k.getGhiChu())
                .trangThai(k.getTrangThai())
                .build();
    }
    
    public int getSoLuongTon(Integer maThuoc, Integer maBienThe) {
        KhoHang kho = null;

        if (maThuoc != null) {
            kho = khoHangRepo.findByThuocId(maThuoc).orElse(null);
        } else if (maBienThe != null) {
            kho = khoHangRepo.findByBienTheId(maBienThe).orElse(null);
        }

        return kho != null && kho.getSoLuongTon() != null ? kho.getSoLuongTon() : 0;
    }

}
