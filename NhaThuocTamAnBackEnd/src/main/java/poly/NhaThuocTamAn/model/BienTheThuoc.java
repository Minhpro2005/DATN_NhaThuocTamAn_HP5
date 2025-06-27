package poly.NhaThuocTamAn.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BienTheThuoc {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer maBienThe;

    private String tenBienThe;
    private BigDecimal giaBan;
    private String hinhAnh;
    private String moTa;
    private String donViTinh;
    private Boolean trangThai = true;

    @ManyToOne
    @JoinColumn(name = "maThuoc")
    private Thuoc thuoc;
}
