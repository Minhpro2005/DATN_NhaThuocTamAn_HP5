package poly.NhaThuocTamAn.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.PhieuNhapDTO;

import jakarta.transaction.Transactional;
import poly.NhaThuocTamAn.model.*;
import poly.NhaThuocTamAn.repository.*;
import poly.NhaThuocTamAn.service.KhoHangService;
import poly.NhaThuocTamAn.service.PhieuNhapService;

@Service
public class PhieuNhapServiceImpl implements PhieuNhapService {

    @Autowired
    private PhieuNhapRepository phieuNhapRepo;

    @Autowired
    private PhieuNhapChiTietRepository chiTietRepo;

    @Autowired
    private KhoHangRepository khoHangRepo;

    @Autowired
    private NhanVienRepository nhanVienRepo;

    @Autowired
    private NhaCungCapRepository nhaCungCapRepo;

    @Autowired
    private ThuocRepository thuocRepo;

    @Autowired
    private BienTheThuocRepository bienTheRepo;

    @Autowired
    private KhoHangService khoHangService;

    @Override
    @Transactional
    public PhieuNhap create(PhieuNhapDTO dto) {
        NhanVien nv = nhanVienRepo.findById(dto.getMaNV()).orElseThrow();
        NhaCungCap ncc = nhaCungCapRepo.findById(dto.getMaNCC()).orElseThrow();

        PhieuNhap pn = new PhieuNhap();
        pn.setNhanVien(nv);
        pn.setNhaCungCap(ncc);
        pn.setNgayNhap(LocalDateTime.now());
        pn.setTongTien(BigDecimal.ZERO);
        pn = phieuNhapRepo.save(pn);

        BigDecimal tongTien = BigDecimal.ZERO;
        List<PhieuNhapChiTiet> chiTietList = new ArrayList<>();

        for (PhieuNhapDTO.ChiTietDTO ct : dto.getChiTiet()) {
            if (ct.getMaThuoc() == null && ct.getMaBienThe() == null) {
                System.out.println("⚠️ Không có mã thuốc hoặc mã biến thể, bỏ qua.");
                continue;
            }

            PhieuNhapChiTiet c = new PhieuNhapChiTiet();
            c.setPhieuNhap(pn);
            c.setSoLuong(ct.getSoLuong());
            c.setDonGiaNhap(ct.getDonGiaNhap());

            tongTien = tongTien.add(
                ct.getDonGiaNhap().multiply(BigDecimal.valueOf(ct.getSoLuong()))
            );

            Thuoc thuoc = null;
            BienTheThuoc bienThe = null;

            if (ct.getMaThuoc() != null) {
                thuoc = thuocRepo.findById(ct.getMaThuoc()).orElse(null);
                if (thuoc == null) {
                    System.out.println("❌ Không tìm thấy thuốc có mã: " + ct.getMaThuoc());
                    continue;
                }
                c.setThuoc(thuoc);
            } else if (ct.getMaBienThe() != null) {
                bienThe = bienTheRepo.findById(ct.getMaBienThe()).orElse(null);
                if (bienThe == null) {
                    System.out.println("❌ Không tìm thấy biến thể thuốc có mã: " + ct.getMaBienThe());
                    continue;
                }
                c.setBienTheThuoc(bienThe);
            }

            // ✅ Cập nhật kho hàng
            khoHangService.themHoacCapNhat(thuoc, bienThe, ct.getSoLuong());

            chiTietList.add(c);
        }

        pn.setTongTien(tongTien);
        phieuNhapRepo.save(pn);
        chiTietRepo.saveAll(chiTietList);

        return pn;
    }


    @Override
    public List<PhieuNhap> getAll() {
        return phieuNhapRepo.findAll();
    }

    @Override
    public List<PhieuNhapChiTiet> getChiTietByPhieuNhap(int maPN) {
        return chiTietRepo.findByPhieuNhap_maPN(maPN);
    }
}
