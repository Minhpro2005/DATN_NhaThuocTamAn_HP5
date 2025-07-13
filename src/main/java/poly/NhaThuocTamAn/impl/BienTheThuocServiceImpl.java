package poly.NhaThuocTamAn.impl;

import com.example.demo.dto.BienTheThuocDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import poly.NhaThuocTamAn.model.BienTheThuoc;
import poly.NhaThuocTamAn.repository.BienTheThuocRepository;
import poly.NhaThuocTamAn.repository.ThuocRepository;
import poly.NhaThuocTamAn.service.BienTheThuocService;
import poly.NhaThuocTamAn.service.FileStorageService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BienTheThuocServiceImpl implements BienTheThuocService {

    @Autowired
    private BienTheThuocRepository bienTheRepo;

    @Autowired
    private ThuocRepository thuocRepo;

    @Autowired
    private FileStorageService fileStorageService;

    @Override
    public BienTheThuocDTO save(BienTheThuocDTO dto, MultipartFile file) {
        BienTheThuoc btt = new BienTheThuoc();

        // Nếu có mã thì cập nhật, không thì tạo mới
        if (dto.getMaBienThe() != null) {
            btt = bienTheRepo.findById(dto.getMaBienThe()).orElse(new BienTheThuoc());
        }

        btt.setTenBienThe(dto.getTenBienThe());
        btt.setGiaBan(dto.getGiaBan());
        btt.setDonViTinh(dto.getDonViTinh());
        btt.setMoTa(dto.getMoTa());
        btt.setTrangThai(dto.getTrangThai() != null ? dto.getTrangThai() : true);

        // Lưu ảnh nếu có upload file mới
        if (file != null && !file.isEmpty()) {
            String path = fileStorageService.saveFile(file, "bienthethuoc");
            btt.setHinhAnh(path);
        } else {
            btt.setHinhAnh(dto.getHinhAnh()); // dùng lại ảnh cũ nếu không có file mới
        }

        btt.setThuoc(thuocRepo.findById(dto.getMaThuoc()).orElse(null));

        return toDTO(bienTheRepo.save(btt));
    }

    @Override
    public void delete(Integer id) {
        bienTheRepo.deleteById(id);
    }

    @Override
    public List<BienTheThuocDTO> getAll() {
        return bienTheRepo.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<BienTheThuocDTO> findByThuoc(Integer maThuoc) {
        return bienTheRepo.findByThuoc_MaThuoc(maThuoc).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private BienTheThuocDTO toDTO(BienTheThuoc btt) {
        return BienTheThuocDTO.builder()
                .maBienThe(btt.getMaBienThe())
                .tenBienThe(btt.getTenBienThe())
                .giaBan(btt.getGiaBan())
                .hinhAnh(btt.getHinhAnh())
                .donViTinh(btt.getDonViTinh())
                .moTa(btt.getMoTa())
                .trangThai(btt.getTrangThai())
                .maThuoc(btt.getThuoc() != null ? btt.getThuoc().getMaThuoc() : null)
                .tenThuoc(btt.getThuoc() != null ? btt.getThuoc().getTenThuoc() : "Không xác định")
                .build();
    }

}
