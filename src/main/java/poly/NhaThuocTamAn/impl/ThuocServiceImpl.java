package poly.NhaThuocTamAn.impl;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.ThuocBanChayDTO;
import com.example.demo.dto.ThuocDTO;
import com.example.demo.dto.ThuocVaBienTheDTO;

import poly.NhaThuocTamAn.model.Thuoc;
import poly.NhaThuocTamAn.repository.AnhThuocRepository;
import poly.NhaThuocTamAn.repository.DanhMucRepository;
import poly.NhaThuocTamAn.repository.NhaCungCapRepository;
import poly.NhaThuocTamAn.repository.ThuocRepository;
import poly.NhaThuocTamAn.service.ThuocService;

@Service
public class ThuocServiceImpl implements ThuocService {

    @Autowired
    private ThuocRepository thuocRepo;

    @Autowired
    private DanhMucRepository danhMucRepo;

    @Autowired
    private NhaCungCapRepository nhaCungCapRepo;

    @Autowired
    private AnhThuocRepository anhThuocRepo;

    private ThuocDTO toDTO(Thuoc t) {
        ThuocDTO dto = new ThuocDTO();
        BeanUtils.copyProperties(t, dto);
        dto.setMaDM(t.getDanhMuc() != null ? t.getDanhMuc().getMaDM() : null);
        dto.setMaNCC(t.getNhaCungCap() != null ? t.getNhaCungCap().getMaNCC() : null);
        dto.setTenDanhMuc(t.getDanhMuc() != null ? t.getDanhMuc().getTenDanhMuc() : null);
        dto.setTenNhaCungCap(t.getNhaCungCap() != null ? t.getNhaCungCap().getTenNCC() : null);

        // Hình ảnh chính
        anhThuocRepo.findAnhChinhByMaThuoc(t.getMaThuoc())
            .ifPresent(anh -> dto.setHinhAnhChinh(anh.getHinhAnh()));

        // Ảnh phụ
        dto.setThumbnails(
            anhThuocRepo.findByThuoc_MaThuoc(t.getMaThuoc()).stream()
                .filter(anh -> !Boolean.TRUE.equals(anh.getAnhChinh()))
                .map(a -> a.getHinhAnh())
                .collect(Collectors.toList())
        );

        return dto;
    }

    
    @Override
    public List<ThuocDTO> getAll() {
        return thuocRepo.findAll().stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }

    
    @Override
    public Thuoc save(ThuocDTO dto) {
        Thuoc t = new Thuoc();
        BeanUtils.copyProperties(dto, t);
        t.setDanhMuc(danhMucRepo.findById(dto.getMaDM()).orElse(null));
        t.setNhaCungCap(nhaCungCapRepo.findById(dto.getMaNCC()).orElse(null));
        return thuocRepo.save(t);
    }

    @Override
    public Thuoc update(Integer id, ThuocDTO dto) {
        Thuoc t = thuocRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Không tìm thấy thuốc với ID: " + id));

        if (dto.getMaDM() == null) {
            throw new IllegalArgumentException("Mã danh mục (maDM) không được null");
        }
        if (dto.getMaNCC() == null) {
            throw new IllegalArgumentException("Mã nhà cung cấp (maNCC) không được null");
        }

        BeanUtils.copyProperties(dto, t);
        t.setDanhMuc(danhMucRepo.findById(dto.getMaDM())
            .orElseThrow(() -> new RuntimeException("Không tìm thấy danh mục với ID: " + dto.getMaDM())));
        t.setNhaCungCap(nhaCungCapRepo.findById(dto.getMaNCC())
            .orElseThrow(() -> new RuntimeException("Không tìm thấy nhà cung cấp với ID: " + dto.getMaNCC())));

        return thuocRepo.save(t);
    }

    @Override
    public void delete(Integer id) {
        thuocRepo.deleteById(id);
    }

    @Override
    public List<ThuocDTO> search(String keyword, Integer maDM, Boolean trangThai, Integer maNCC) {
        return thuocRepo.searchThuoc(keyword, maDM, trangThai, maNCC)
            .stream().map(this::toDTO).toList();
    }

    @Override
    public List<ThuocVaBienTheDTO> getThuocVaBienTheTheoDanhMuc(Integer maDM) {
        return thuocRepo.findThuocVaBienTheTheoDanhMuc(maDM);
    }

    @Override
    public ThuocDTO findById(Integer id) {
        Thuoc t = thuocRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Không tìm thấy thuốc với ID: " + id));
        return toDTO(t);
    }
    
    @Override
    public List<ThuocBanChayDTO> getTop10ThuocBanChay() {
        return thuocRepo.findTop10ThuocBanChay(org.springframework.data.domain.PageRequest.of(0, 10));
    }

}
