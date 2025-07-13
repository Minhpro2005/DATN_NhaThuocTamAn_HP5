package poly.NhaThuocTamAn.service;

import java.util.List;
import poly.NhaThuocTamAn.model.DonHang;
import poly.NhaThuocTamAn.model.LichSuDonHang;
import com.example.demo.dto.DonHangDTO;

public interface DonHangService {
    DonHang create(DonHangDTO dto);
    DonHang updateStatus(Integer maDonHang, Integer trangThaiMoi, String lyDoHuy);
    List<DonHang> findAll();
    DonHang findById(Integer id);
    List<LichSuDonHang> getLichSu(Integer maDonHang);
    void capNhatDaThanhToan(Integer maDonHang, boolean daThanhToan);
    List<DonHangDTO> findByMaKhachHang(Integer maKH);
    DonHangDTO findDTOById(Integer id); // Phải có trong interface

}
