package poly.NhaThuocTamAn.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.ChiTietDTO;
import com.example.demo.dto.DonHangChiTietDTO;
import com.example.demo.dto.DonHangDTO;

import jakarta.transaction.Transactional;
import poly.NhaThuocTamAn.model.BienTheThuoc;
import poly.NhaThuocTamAn.model.DonHang;
import poly.NhaThuocTamAn.model.DonHangChiTiet;
import poly.NhaThuocTamAn.model.KhachHang;
import poly.NhaThuocTamAn.model.KhuyenMai;
import poly.NhaThuocTamAn.model.LichSuDonHang;
import poly.NhaThuocTamAn.model.Thuoc;
import poly.NhaThuocTamAn.repository.AnhThuocRepository;
import poly.NhaThuocTamAn.repository.BienTheThuocRepository;
import poly.NhaThuocTamAn.repository.DonHangChiTietRepository;
import poly.NhaThuocTamAn.repository.DonHangRepository;
import poly.NhaThuocTamAn.repository.KhachHangRepository;
import poly.NhaThuocTamAn.repository.KhoHangRepository;
import poly.NhaThuocTamAn.repository.LichSuDonHangRepository;
import poly.NhaThuocTamAn.repository.ThuocRepository;
import poly.NhaThuocTamAn.repository.KhuyenMaiRepository;
import poly.NhaThuocTamAn.service.DonHangService;
import poly.NhaThuocTamAn.service.KhoHangService;

@Service
public class DonHangServiceImpl implements DonHangService {

    @Autowired private DonHangRepository donHangRepo;
    @Autowired private DonHangChiTietRepository chiTietRepo;
    @Autowired private LichSuDonHangRepository lichSuRepo;
    @Autowired private BienTheThuocRepository bienTheRepo;
    @Autowired private KhoHangRepository khoRepo;
    @Autowired private KhachHangRepository khachHangRepo;
    @Autowired private KhuyenMaiRepository khuyenMaiRepo;
    @Autowired private ThuocRepository thuocRepository;
    @Autowired private AnhThuocRepository anhThuocRepo;
    @Autowired
    private KhoHangService khoHangService;

    

    @Override
    @Transactional
    public DonHang create(DonHangDTO dto) {
        // Lấy khách hàng nếu có mã KH
        KhachHang khachHang = null;
        if (dto.getMaKH() != null) {
            khachHang = khachHangRepo.findById(dto.getMaKH())
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy khách hàng với ID: " + dto.getMaKH()));
        }

        // Lấy khuyến mãi nếu có
        KhuyenMai khuyenMai = null;
        if (dto.getMaKM() != null) {
            khuyenMai = khuyenMaiRepo.findById(dto.getMaKM())
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy khuyến mãi với ID: " + dto.getMaKM()));
        }

        // Tạo đơn hàng
        DonHang dh = new DonHang();
        dh.setKhachHang(khachHang); // null nếu khách vãng lai
        dh.setKhuyenMai(khuyenMai);
        dh.setNgayDat(LocalDateTime.now());
        dh.setTrangThai(0);
        dh.setDiaChiNhan(dto.getDiaChiNhan());
        dh.setSoDienThoaiNhan(dto.getSoDienThoaiNhan());
        dh.setHoTenNguoiNhan(dto.getHoTenNguoiNhan());
        dh.setHinhThucThanhToan(dto.getHinhThucThanhToan());
        dh.setGhiChu(dto.getGhiChu());

        BigDecimal tong = BigDecimal.ZERO;
        List<DonHangChiTiet> chiTiets = new ArrayList<>();

        for (ChiTietDTO ct : dto.getChiTiet()) {
            DonHangChiTiet ctEntity = new DonHangChiTiet();
            ctEntity.setSoLuong(ct.getSoLuong());
            ctEntity.setDonGia(ct.getDonGia());
            ctEntity.setDonHang(dh);

            if (ct.getMaBienThe() != null) {
                BienTheThuoc bienThe = bienTheRepo.findById(ct.getMaBienThe())
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy biến thể: " + ct.getMaBienThe()));
                ctEntity.setBienTheThuoc(bienThe);
            } else if (ct.getMaThuoc() != null) {
                Thuoc thuoc = thuocRepository.findById(ct.getMaThuoc())
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy thuốc: " + ct.getMaThuoc()));
                ctEntity.setThuoc(thuoc);
            } else {
                throw new IllegalArgumentException("Chi tiết đơn hàng thiếu maBienThe và maThuoc");
            }

            tong = tong.add(ct.getDonGia().multiply(BigDecimal.valueOf(ct.getSoLuong())));
            chiTiets.add(ctEntity);
        }

        // ✅ Tính giảm giá nếu có
        BigDecimal tienGiam = BigDecimal.ZERO;
        if (khuyenMai != null) {
            if ("percent".equalsIgnoreCase(khuyenMai.getLoaiKM())) {
                tienGiam = tong.multiply(khuyenMai.getGiaTri()).divide(BigDecimal.valueOf(100));
            } else {
                tienGiam = khuyenMai.getGiaTri();
            }

            if (khuyenMai.getGiaTriToiDa() != null) {
                tienGiam = tienGiam.min(khuyenMai.getGiaTriToiDa());
            }

            if (tienGiam.compareTo(tong) > 0) {
                tienGiam = tong;
            }

            // ✅ Cập nhật lượt sử dụng mã khuyến mãi
            khuyenMai.setDaSuDung(khuyenMai.getDaSuDung() + 1);
            khuyenMaiRepo.save(khuyenMai);
        }

        dh.setTongTien(tong);
        dh.setGiamGia(tienGiam);
        dh.setThanhToan(tong.subtract(tienGiam));
        dh.setDaThanhToan(false);

        dh.setChiTiet(chiTiets);
        donHangRepo.save(dh);
        chiTietRepo.saveAll(chiTiets);

        // Ghi lịch sử đơn hàng
        LichSuDonHang ls = new LichSuDonHang();
        ls.setDonHang(dh);
        ls.setTrangThaiCu(null);
        ls.setTrangThaiMoi(0);
        ls.setThoiGian(LocalDateTime.now());
        ls.setGhiChu("Tạo đơn hàng mới");
        lichSuRepo.save(ls);

        return dh;
    }


    
    @Override
    public DonHang findById(Integer id) {
        return donHangRepo.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy đơn hàng với ID: " + id));
    }

    @Override
    @Transactional
    public DonHang updateStatus(Integer maDonHang, Integer newStatus, String lyDoHuy) {
        DonHang dh = donHangRepo.findById(maDonHang)
            .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy đơn hàng với ID: " + maDonHang));

        int oldStatus = dh.getTrangThai();
        dh.setTrangThai(newStatus);

        // ✅ Nếu hủy đơn và có lý do
        if (newStatus == 4 && lyDoHuy != null && !lyDoHuy.trim().isEmpty()) {
            dh.setLyDoHuy(lyDoHuy.trim());
        }

        // ✅ Nếu chuyển sang trạng thái "ĐÃ XÁC NHẬN" thì kiểm tra và trừ kho
        if (newStatus == 1) {
            for (DonHangChiTiet ct : dh.getChiTiet()) {
                if (ct == null) continue;

                if (ct.getBienTheThuoc() != null) {
                    BienTheThuoc bienThe = bienTheRepo.findById(ct.getBienTheThuoc().getMaBienThe())
                        .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy biến thể thuốc"));

                    // ✅ Lấy tồn kho từ bảng KhoHang
                    int slTon = khoHangService.getSoLuongTon(null, bienThe.getMaBienThe());
                    if (slTon < ct.getSoLuong()) {
                        throw new IllegalStateException("Biến thể thuốc \"" + bienThe.getTenBienThe() + "\" không đủ số lượng trong kho.");
                    }

                    // ✅ Trừ trong kho
                    khoHangService.themHoacCapNhat(null, bienThe, -ct.getSoLuong());

                } else if (ct.getThuoc() != null) {
                    Thuoc thuoc = thuocRepository.findById(ct.getThuoc().getMaThuoc())
                        .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy thuốc"));

                    int slTon = khoHangService.getSoLuongTon(thuoc.getMaThuoc(), null);
                    if (slTon < ct.getSoLuong()) {
                        throw new IllegalStateException("Thuốc \"" + thuoc.getTenThuoc() + "\" không đủ số lượng trong kho.");
                    }

                    // ✅ Trừ trong kho
                    khoHangService.themHoacCapNhat(thuoc, null, -ct.getSoLuong());
                } else {
                    throw new IllegalStateException("Chi tiết đơn hàng không có thuốc hoặc biến thể.");
                }
            }
        }

        donHangRepo.save(dh);

        // ✅ Ghi lịch sử
        LichSuDonHang ls = new LichSuDonHang();
        ls.setDonHang(dh);
        ls.setTrangThaiCu(oldStatus);
        ls.setTrangThaiMoi(newStatus);
        ls.setThoiGian(LocalDateTime.now());
        ls.setGhiChu(newStatus == 4 ? "Hủy đơn hàng: " + lyDoHuy : "Cập nhật trạng thái đơn hàng");
        lichSuRepo.save(ls);

        return dh;
    }




    @Override
    public List<DonHang> findAll() {
        return donHangRepo.findAll();
    }

    @Override
    public DonHangDTO findDTOById(Integer id) {
        DonHang dh = donHangRepo.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy đơn hàng với ID: " + id));

        DonHangDTO dto = new DonHangDTO();
        dto.setMaDonHang(dh.getMaDonHang());
        dto.setNgayDat(dh.getNgayDat());
        dto.setTrangThai(dh.getTrangThai());
        dto.setThanhToan(dh.getThanhToan());
        dto.setHoTenNguoiNhan(dh.getHoTenNguoiNhan());
        dto.setSoDienThoaiNhan(dh.getSoDienThoaiNhan());
        dto.setDiaChiNhan(dh.getDiaChiNhan());
        dto.setGhiChu(dh.getGhiChu());
        dto.setHinhThucThanhToan(dh.getHinhThucThanhToan());
        dto.setGiamGia(dh.getGiamGia());

        if (dh.getKhachHang() != null) {
            dto.setTenKH(dh.getKhachHang().getHoTen()); 
        }

        List<DonHangChiTietDTO> chiTietHienThi = new ArrayList<>();
        for (DonHangChiTiet ct : dh.getChiTiet()) {
            DonHangChiTietDTO ctDTO = new DonHangChiTietDTO();
            ctDTO.setSoLuong(ct.getSoLuong());
            ctDTO.setDonGia(ct.getDonGia().doubleValue());

            if (ct.getBienTheThuoc() != null) {
                ctDTO.setMaBienThe(ct.getBienTheThuoc().getMaBienThe());
                ctDTO.setTenThuoc(ct.getBienTheThuoc().getTenBienThe());
                ctDTO.setHinhAnh(ct.getBienTheThuoc().getHinhAnh());
            } else if (ct.getThuoc() != null) {
                Thuoc thuoc = ct.getThuoc();
                ctDTO.setMaThuoc(thuoc.getMaThuoc());
                ctDTO.setTenThuoc(thuoc.getTenThuoc());

                String hinhAnhChinh = anhThuocRepo.findAnhChinhByMaThuoc(thuoc.getMaThuoc())
                    .map(a -> a.getHinhAnh())
                    .orElse(null);

                ctDTO.setHinhAnhChinh(hinhAnhChinh);
            }

            chiTietHienThi.add(ctDTO);
        }

        dto.setChiTietHienThi(chiTietHienThi);
        return dto;
    }


    @Override
    public List<LichSuDonHang> getLichSu(Integer maDonHang) {
        return lichSuRepo.findAll().stream()
            .filter(ls -> ls.getDonHang() != null && ls.getDonHang().getMaDonHang().equals(maDonHang))
            .collect(Collectors.toList());
    }
    
    @Override
    public void capNhatDaThanhToan(Integer maDonHang, boolean daThanhToan) {
        DonHang dh = donHangRepo.findById(maDonHang)
            .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy đơn hàng"));
        dh.setDaThanhToan(daThanhToan);
        donHangRepo.save(dh);

        // Ghi lịch sử
        LichSuDonHang ls = new LichSuDonHang();
        ls.setDonHang(dh);
        ls.setTrangThaiCu(dh.getTrangThai());
        ls.setTrangThaiMoi(dh.getTrangThai());
        ls.setThoiGian(LocalDateTime.now());
        ls.setGhiChu(daThanhToan ? "Đã thanh toán online" : "Hủy thanh toán online");
        lichSuRepo.save(ls);
    }

    @Override
    public List<DonHangDTO> findByMaKhachHang(Integer maKH) {
        List<DonHang> ds = donHangRepo.findByKhachHang_MaKH(maKH);
        List<DonHangDTO> result = new ArrayList<>();

        for (DonHang dh : ds) {
            DonHangDTO dto = new DonHangDTO();
            dto.setMaDonHang(dh.getMaDonHang());
            dto.setNgayDat(dh.getNgayDat());
            dto.setTrangThai(dh.getTrangThai());
            dto.setThanhToan(dh.getThanhToan());

            List<DonHangChiTietDTO> chiTietHienThi = new ArrayList<>();
            for (DonHangChiTiet ct : dh.getChiTiet()) {
                DonHangChiTietDTO ctDTO = new DonHangChiTietDTO();
                ctDTO.setSoLuong(ct.getSoLuong());
                ctDTO.setDonGia(ct.getDonGia().doubleValue());

                if (ct.getBienTheThuoc() != null) {
                    ctDTO.setMaBienThe(ct.getBienTheThuoc().getMaBienThe());
                    ctDTO.setTenThuoc(ct.getBienTheThuoc().getTenBienThe());
                    ctDTO.setHinhAnh(ct.getBienTheThuoc().getHinhAnh());
                } else if (ct.getThuoc() != null) {
                    Thuoc thuoc = ct.getThuoc();
                    ctDTO.setMaThuoc(thuoc.getMaThuoc());
                    ctDTO.setTenThuoc(thuoc.getTenThuoc());

                    // ✅ Lấy ảnh chính từ bảng AnhThuoc
                    String hinhAnhChinh = anhThuocRepo.findAnhChinhByMaThuoc(thuoc.getMaThuoc())
                        .map(a -> a.getHinhAnh())
                        .orElse(null);

                    ctDTO.setHinhAnhChinh(hinhAnhChinh); // ✅ set ảnh chính vào DTO
                }

                chiTietHienThi.add(ctDTO);
            }

            dto.setChiTietHienThi(chiTietHienThi);
            result.add(dto);
        }

        return result;
    }


}
