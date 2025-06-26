package poly.NhaThuocTamAn.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.NhanVienDTO;

import poly.NhaThuocTamAn.model.NhanVien;
import poly.NhaThuocTamAn.repository.NhanVienRepository;
import poly.NhaThuocTamAn.service.NhanVienService;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NhanVienServiceImpl implements NhanVienService {

    @Autowired
    private NhanVienRepository repo;

    private final String uploadDir = System.getProperty("user.dir") + "/uploads/nhanvien/";

    private NhanVienDTO convertToDTO(NhanVien nv) {
        return new NhanVienDTO(
                nv.getMaNV(), nv.getHoTen(), nv.getEmail(), nv.getSoDienThoai(),
                nv.getCccd(), nv.getNgaySinh(), nv.getGioiTinh(), nv.getDiaChi(),
                nv.getHinhAnh(), nv.getVaiTro(), nv.getTrangThai());
    }

    private NhanVien convertToEntity(NhanVienDTO dto) {
        NhanVien nv = new NhanVien();
        nv.setMaNV(dto.getMaNV());
        nv.setHoTen(dto.getHoTen());
        nv.setEmail(dto.getEmail());
        nv.setSoDienThoai(dto.getSoDienThoai());
        nv.setCccd(dto.getCccd());
        nv.setNgaySinh(dto.getNgaySinh());
        nv.setGioiTinh(dto.getGioiTinh());
        nv.setDiaChi(dto.getDiaChi());
        nv.setHinhAnh(dto.getHinhAnh());
        nv.setVaiTro(dto.getVaiTro());
        nv.setTrangThai(dto.getTrangThai());
        return nv;
    }

    @Override
    public List<NhanVienDTO> getAll() {
        return repo.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public NhanVienDTO getById(Integer id) {
        return repo.findById(id).map(this::convertToDTO).orElse(null);
    }

    @Override
    public NhanVienDTO create(NhanVienDTO dto) {
        NhanVien nv = convertToEntity(dto);
        nv.setMatKhau("123456");
        return convertToDTO(repo.save(nv));
    }

    @Override
    public NhanVienDTO update(Integer id, NhanVienDTO dto) {
        Optional<NhanVien> opt = repo.findById(id);
        if (opt.isPresent()) {
            NhanVien nv = opt.get();
            nv.setHoTen(dto.getHoTen());
            nv.setEmail(dto.getEmail());
            nv.setSoDienThoai(dto.getSoDienThoai());
            nv.setCccd(dto.getCccd());
            nv.setNgaySinh(dto.getNgaySinh());
            nv.setGioiTinh(dto.getGioiTinh());
            nv.setDiaChi(dto.getDiaChi());
            nv.setHinhAnh(dto.getHinhAnh());
            nv.setVaiTro(dto.getVaiTro());
            nv.setTrangThai(dto.getTrangThai());
            return convertToDTO(repo.save(nv));
        }
        return null;
    }

    @Override
    public void delete(Integer id) {
        repo.deleteById(id);
    }

    @Override
    public NhanVienDTO updateStatus(Integer id, Boolean status) {
        Optional<NhanVien> opt = repo.findById(id);
        if (opt.isPresent()) {
            NhanVien nv = opt.get();
            nv.setTrangThai(status);
            return convertToDTO(repo.save(nv));
        }
        return null;
    }

    @Override
    public List<NhanVienDTO> search(String keyword) {
        return repo.search(keyword).stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public NhanVienDTO uploadAvatar(Integer id, MultipartFile file) throws IOException {
        Optional<NhanVien> opt = repo.findById(id);
        if (opt.isEmpty()) {
            return null;
        }
        NhanVien nv = opt.get();

        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename().replaceAll("\\s+", "_");
        File dest = new File(directory, filename);
        file.transferTo(dest);

        nv.setHinhAnh("/uploads/nhanvien/" + filename);
        return convertToDTO(repo.save(nv));
    }
}
