package poly.NhaThuocTamAn.model;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PhieuNhap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer maPN;

    private LocalDateTime ngayNhap;

    @ManyToOne
    @JoinColumn(name = "maNV")
    private NhanVien nhanVien;

    @ManyToOne
    @JoinColumn(name = "maNCC")
    private NhaCungCap nhaCungCap;

    private BigDecimal tongTien;
}

