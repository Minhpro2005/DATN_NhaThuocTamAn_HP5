package poly.NhaThuocTamAn.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.KhuyenMaiDTO;

import poly.NhaThuocTamAn.model.KhuyenMai;
import poly.NhaThuocTamAn.repository.KhuyenMaiRepository;
import poly.NhaThuocTamAn.service.KhuyenMaiService;

@Service
public class KhuyenMaiServiceImpl implements KhuyenMaiService {

    @Autowired
    private KhuyenMaiRepository repo;

    private final ModelMapper mapper = new ModelMapper();

    private KhuyenMaiDTO toDTO(KhuyenMai km) {
        return mapper.map(km, KhuyenMaiDTO.class);
    }

    private KhuyenMai toEntity(KhuyenMaiDTO dto) {
        return mapper.map(dto, KhuyenMai.class);
    }

    @Override
    public List<KhuyenMaiDTO> getAll() {
        return repo.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public KhuyenMaiDTO getById(Integer id) {
        return repo.findById(id).map(this::toDTO).orElseThrow();
    }

    @Override
    public KhuyenMaiDTO create(KhuyenMaiDTO dto) {
        KhuyenMai entity = toEntity(dto);
        entity.setDaSuDung(0);
        return toDTO(repo.save(entity));
    }

    @Override
    public KhuyenMaiDTO update(Integer id, KhuyenMaiDTO dto) {
        dto.setMaKM(id);
        return toDTO(repo.save(toEntity(dto)));
    }

    @Override
    public void delete(Integer id) {
        repo.deleteById(id);
    }

    @Override
    public Optional<KhuyenMaiDTO> apDungVoucher(String code, BigDecimal tongTien) {
        return repo.findByMaCodeIgnoreCase(code).filter(km -> {
            LocalDate today = LocalDate.now();
            return Boolean.TRUE.equals(km.getTrangThai())
                && today.compareTo(km.getNgayBatDau()) >= 0
                && today.compareTo(km.getNgayKetThuc()) <= 0
                && km.getSoLuong() > km.getDaSuDung()
                && tongTien.compareTo(km.getDonHangToiThieu()) >= 0;
        }).map(this::toDTO);
    }
}

