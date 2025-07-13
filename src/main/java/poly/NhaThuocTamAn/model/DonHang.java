package poly.NhaThuocTamAn.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "DonHang")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DonHang {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer maDonHang;

    @ManyToOne
    @JoinColumn(name = "maKH")
    private KhachHang khachHang;

    @ManyToOne
    @JoinColumn(name = "maNV")
    private NhanVien nhanVien;

    @ManyToOne
    @JoinColumn(name = "maKM")
    private KhuyenMai khuyenMai;

    private LocalDateTime ngayDat;
    private Integer trangThai;
    private BigDecimal tongTien;
    private BigDecimal giamGia;
    private BigDecimal thanhToan;
    private String hinhThucThanhToan;
    private Boolean daThanhToan;
    private LocalDateTime ngayThanhToan;
    private String diaChiNhan;
    private String soDienThoaiNhan;
    private String ghiChu;
    private String hoTenNguoiNhan;
    private String lyDoHuy;


    @OneToMany(mappedBy = "donHang", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<DonHangChiTiet> chiTiet;
}


