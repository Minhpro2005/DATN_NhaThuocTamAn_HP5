package poly.NhaThuocTamAn.impl;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.AnhThuocDTO;

import poly.NhaThuocTamAn.model.AnhThuoc;
import poly.NhaThuocTamAn.model.Thuoc;
import poly.NhaThuocTamAn.repository.AnhThuocRepository;
import poly.NhaThuocTamAn.repository.ThuocRepository;
import poly.NhaThuocTamAn.service.AnhThuocService;
import poly.NhaThuocTamAn.service.FileStorageService;

@Service
public class AnhThuocServiceImpl implements AnhThuocService {

    @Autowired
    private AnhThuocRepository anhThuocRepo;

    @Autowired
    private ThuocRepository thuocRepo;

    @Autowired
    private FileStorageService fileStorageService;

    @Override
    public List<AnhThuoc> getAll() {
        return anhThuocRepo.findAll();
    }

    @Override
    public List<AnhThuoc> findByMaThuoc(Integer maThuoc) {
        return anhThuocRepo.findByThuoc_MaThuoc(maThuoc);
    }

    @Override
    public AnhThuoc save(AnhThuocDTO dto) {
        // Kiểm tra tồn tại của thuốc
        Thuoc thuoc = thuocRepo.findById(dto.getMaThuoc())
            .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy thuốc có mã: " + dto.getMaThuoc()));

        // Nếu là ảnh chính, bỏ ảnh chính hiện tại
        if (Boolean.TRUE.equals(dto.getAnhChinh())) {
            List<AnhThuoc> others = anhThuocRepo.findByThuoc_MaThuoc(dto.getMaThuoc());
            for (AnhThuoc a : others) {
                if (!a.getMaAnhThuoc().equals(dto.getMaAnhThuoc()) && Boolean.TRUE.equals(a.getAnhChinh())) {
                    a.setAnhChinh(false);
                    anhThuocRepo.save(a);
                }
            }
        }

        AnhThuoc anh = AnhThuoc.builder()
                .maAnhThuoc(dto.getMaAnhThuoc())
                .hinhAnh(dto.getHinhAnh())
                .anhChinh(dto.getAnhChinh() != null ? dto.getAnhChinh() : false)
                .thuoc(thuoc)
                .build();

        return anhThuocRepo.save(anh);
    }


    @Override
    public void deleteById(Integer id) {
        AnhThuoc anh = anhThuocRepo.findById(id).orElse(null);
        if (anh != null && anh.getHinhAnh() != null) {
            File file = new File(System.getProperty("user.dir") + "/" + anh.getHinhAnh());
            if (file.exists()) file.delete();
        }
        anhThuocRepo.deleteById(id);
    }

    @Override
    public String uploadAnh(MultipartFile file) {
        return fileStorageService.saveFile(file, "thuoc");
    }
}

