package poly.NhaThuocTamAn.model;

import java.math.BigDecimal;

import org.hibernate.annotations.Formula;

import com.fasterxml.jackson.annotation.JsonBackReference;

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
@Table(name = "DonHangChiTiet")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DonHangChiTiet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer maDHCT;

    @ManyToOne
    @JoinColumn(name = "maDonHang")
    @JsonBackReference
    private DonHang donHang;

    @ManyToOne
    @JoinColumn(name = "maBienThe")
    private BienTheThuoc bienTheThuoc;

    private Integer soLuong;
    private BigDecimal donGia;

    @Formula("soLuong * donGia")
    private BigDecimal thanhTien;
    
    @ManyToOne
    @JoinColumn(name = "maThuoc")
    private Thuoc thuoc;
}

