package poly.NhaThuocTamAn.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.NhaCungCapDTO;

import poly.NhaThuocTamAn.model.NhaCungCap;
import poly.NhaThuocTamAn.repository.NhaCungCapRepository;
import poly.NhaThuocTamAn.service.NhaCungCapService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NhaCungCapServiceImpl implements NhaCungCapService {

    @Autowired
    private NhaCungCapRepository nhaCungCapRepository;

    private NhaCungCapDTO convertToDTO(NhaCungCap entity) {
        return new NhaCungCapDTO(entity.getMaNCC(), entity.getTenNCC(), entity.getDiaChi(), entity.getSoDienThoai(), entity.getEmail());
    }

    private NhaCungCap convertToEntity(NhaCungCapDTO dto) {
        return new NhaCungCap(dto.getMaNCC(), dto.getTenNCC(), dto.getDiaChi(), dto.getSoDienThoai(), dto.getEmail());
    }

    @Override
    public NhaCungCapDTO create(NhaCungCapDTO dto) {
        NhaCungCap entity = convertToEntity(dto);
        return convertToDTO(nhaCungCapRepository.save(entity));
    }

    @Override
    public NhaCungCapDTO update(Integer maNCC, NhaCungCapDTO dto) {
        Optional<NhaCungCap> optional = nhaCungCapRepository.findById(maNCC);
        if (optional.isPresent()) {
            NhaCungCap entity = optional.get();
            entity.setTenNCC(dto.getTenNCC());
            entity.setDiaChi(dto.getDiaChi());
            entity.setSoDienThoai(dto.getSoDienThoai());
            entity.setEmail(dto.getEmail());
            return convertToDTO(nhaCungCapRepository.save(entity));
        }
        throw new RuntimeException("Nhà cung cấp không tồn tại");
    }

    @Override
    public void delete(Integer maNCC) {
        nhaCungCapRepository.deleteById(maNCC);
    }

    @Override
    public NhaCungCapDTO getById(Integer maNCC) {
        NhaCungCap entity = nhaCungCapRepository.findById(maNCC)
                .orElseThrow(() -> new RuntimeException("Nhà cung cấp không tồn tại"));
        return convertToDTO(entity);
    }

    @Override
    public List<NhaCungCapDTO> getAll() {
        return nhaCungCapRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public List<NhaCungCapDTO> searchByTenNCC(String tenNCC) {
        return nhaCungCapRepository.findByTenNCCContainingIgnoreCase(tenNCC)
                .stream().map(this::convertToDTO).collect(Collectors.toList());
    }
}
