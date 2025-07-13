package poly.NhaThuocTamAn.model;
import java.math.BigDecimal;

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
public class PhieuNhapChiTiet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer maPNCT;

    @ManyToOne
    @JoinColumn(name = "maPN")
    private PhieuNhap phieuNhap;

    @ManyToOne
    @JoinColumn(name = "maBienThe")
    private BienTheThuoc bienTheThuoc;

    @ManyToOne
    @JoinColumn(name = "maThuoc")
    private Thuoc thuoc;

    private Integer soLuong;

    private BigDecimal donGiaNhap;
}

