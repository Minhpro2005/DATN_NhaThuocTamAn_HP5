package poly.NhaThuocTamAn.impl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.DanhMucDTO;

import poly.NhaThuocTamAn.model.DanhMuc;
import poly.NhaThuocTamAn.repository.DanhMucRepository;
import poly.NhaThuocTamAn.service.DanhMucService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DanhMucServiceImpl implements DanhMucService {

    @Autowired
    private DanhMucRepository danhMucRepository;

    private DanhMucDTO convertToDTO(DanhMuc entity) {
        return new DanhMucDTO(entity.getMaDM(), entity.getTenDanhMuc(), entity.getMoTa());
    }

    private DanhMuc convertToEntity(DanhMucDTO dto) {
        return new DanhMuc(dto.getMaDM(), dto.getTenDanhMuc(), dto.getMoTa());
    }

    @Override
    public DanhMucDTO create(DanhMucDTO dto) {
        DanhMuc entity = convertToEntity(dto);
        return convertToDTO(danhMucRepository.save(entity));
    }

    @Override
    public DanhMucDTO update(Integer maDM, DanhMucDTO dto) {
        Optional<DanhMuc> optional = danhMucRepository.findById(maDM);
        if (optional.isPresent()) {
            DanhMuc entity = optional.get();
            entity.setTenDanhMuc(dto.getTenDanhMuc());
            entity.setMoTa(dto.getMoTa());
            return convertToDTO(danhMucRepository.save(entity));
        }
        throw new RuntimeException("Danh mục không tồn tại");
    }

    @Override
    public void delete(Integer maDM) {
        danhMucRepository.deleteById(maDM);
    }

    @Override
    public DanhMucDTO getById(Integer maDM) {
        DanhMuc entity = danhMucRepository.findById(maDM)
                .orElseThrow(() -> new RuntimeException("Danh mục không tồn tại"));
        return convertToDTO(entity);
    }

    @Override
    public List<DanhMucDTO> getAll() {
        return danhMucRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public List<DanhMucDTO> searchByTenDanhMuc(String tenDanhMuc) {
        return danhMucRepository.findByTenDanhMucContainingIgnoreCase(tenDanhMuc)
                .stream().map(this::convertToDTO).collect(Collectors.toList());
    }
}

