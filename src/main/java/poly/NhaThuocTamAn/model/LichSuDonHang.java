package poly.NhaThuocTamAn.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "LichSuDonHang")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LichSuDonHang {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer maLSDH;

    @ManyToOne
    @JoinColumn(name = "maDonHang")
    private DonHang donHang;

    private Integer trangThaiCu;
    private Integer trangThaiMoi;
    private LocalDateTime thoiGian;
    private String ghiChu;
}
