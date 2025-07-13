package poly.NhaThuocTamAn.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "KhuyenMai")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class KhuyenMai {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer maKM;

    private String tenKM;
    private String maCode;
    private String loaiKM;

    @Column(precision = 18, scale = 2)
    private BigDecimal giaTri;

    @Column(precision = 18, scale = 2)
    private BigDecimal giaTriToiDa;

    @Column(precision = 18, scale = 2)
    private BigDecimal donHangToiThieu;

    private Integer soLuong;

    private Integer daSuDung = 0;

    private LocalDate ngayBatDau;
    private LocalDate ngayKetThuc;

    private String moTa;

    private Boolean trangThai;
    
    public KhuyenMai(Integer maKM) {
        this.maKM = maKM;
    }

}

