package poly.NhaThuocTamAn.impl;

import com.example.demo.dto.DanhGiaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import poly.NhaThuocTamAn.model.BienTheThuoc;
import poly.NhaThuocTamAn.model.DanhGia;
import poly.NhaThuocTamAn.model.KhachHang;
import poly.NhaThuocTamAn.model.Thuoc;
import poly.NhaThuocTamAn.repository.*;

import poly.NhaThuocTamAn.service.DanhGiaService;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class DanhGiaServiceImpl implements DanhGiaService {

    @Autowired
    private DanhGiaRepository danhGiaRepo;

    @Autowired
    private KhachHangRepository khachHangRepo;

    @Autowired
    private BienTheThuocRepository bienTheRepo;

    @Autowired
    private ThuocRepository thuocRepo;

    @Autowired
    private DonHangChiTietRepository donHangChiTietRepo;

    @Override
    public List<DanhGiaDTO> getDanhGiaByBienThe(Integer maBienThe) {
        return danhGiaRepo.findByBienTheThuoc_MaBienThe(maBienThe)
                .stream()
                .filter(dg -> dg.getBienTheThuoc() != null)
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public DanhGiaDTO addDanhGia(DanhGiaDTO dto) {
        KhachHang kh = khachHangRepo.findById(dto.getMaKH()).orElse(null);
        DanhGia dg = new DanhGia();
        dg.setKhachHang(kh);
        dg.setHinhAnh(dto.getHinhAnh());
        dg.setSoSao(dto.getSoSao());
        dg.setBinhLuan(dto.getBinhLuan());

        // Ưu tiên lưu theo biến thể nếu có
        if (dto.getMaBienThe() != null) {
            BienTheThuoc bt = bienTheRepo.findById(dto.getMaBienThe()).orElse(null);
            dg.setBienTheThuoc(bt);
        } else if (dto.getMaThuoc() != null) {
            Thuoc thuoc = thuocRepo.findById(dto.getMaThuoc()).orElse(null);
            dg.setThuoc(thuoc);
        }

        return toDTO(danhGiaRepo.save(dg));
    }

    @Override
    public DanhGiaDTO updateDanhGia(Integer maDG, DanhGiaDTO dto) {
        DanhGia dg = danhGiaRepo.findById(maDG).orElseThrow();

        if (dto.getSoSao() != null) dg.setSoSao(dto.getSoSao());
        if (dto.getBinhLuan() != null) dg.setBinhLuan(dto.getBinhLuan());
        if (dto.getHinhAnh() != null) dg.setHinhAnh(dto.getHinhAnh());

        return toDTO(danhGiaRepo.save(dg));
    }

    @Override
    public void deleteDanhGia(Integer maDG) {
        danhGiaRepo.deleteById(maDG);
    }

    private DanhGiaDTO toDTO(DanhGia dg) {
        DanhGiaDTO dto = new DanhGiaDTO();
        dto.setMaDG(dg.getMaDG());
        dto.setMaKH(dg.getKhachHang().getMaKH());

        if (dg.getBienTheThuoc() != null) {
            dto.setMaBienThe(dg.getBienTheThuoc().getMaBienThe());
            dto.setTenBienThe(dg.getBienTheThuoc().getTenBienThe());
            if (dg.getBienTheThuoc().getThuoc() != null) {
                Thuoc thuoc = dg.getBienTheThuoc().getThuoc();
                dto.setMaThuoc(thuoc.getMaThuoc());
                dto.setTenThuoc(thuoc.getTenThuoc());
            }
        } else if (dg.getThuoc() != null) {
            dto.setMaThuoc(dg.getThuoc().getMaThuoc());
            dto.setTenThuoc(dg.getThuoc().getTenThuoc());
        }

        dto.setHinhAnh(dg.getHinhAnh());
        dto.setSoSao(dg.getSoSao());
        dto.setBinhLuan(dg.getBinhLuan());
        dto.setNgayDanhGia(dg.getNgayDanhGia().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        dto.setTenKhachHang(dg.getKhachHang().getHoTen());
        dto.setHinhAnhKH(dg.getKhachHang().getHinhAnh()); // ✅ avatar KH

        return dto;
    }


    @Override
    public List<DanhGiaDTO> getDanhGiaByThuoc(Integer maThuoc) {
        List<DanhGia> danhGiaThuoc = danhGiaRepo.findByThuoc_MaThuoc(maThuoc);
        List<DanhGia> danhGiaBienThe = danhGiaRepo.findByBienTheThuoc_Thuoc_MaThuoc(maThuoc);

        return Stream.concat(danhGiaThuoc.stream(), danhGiaBienThe.stream())
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<DanhGiaDTO> getAllDanhGia() {
        return danhGiaRepo.findAll()
            .stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }

}
