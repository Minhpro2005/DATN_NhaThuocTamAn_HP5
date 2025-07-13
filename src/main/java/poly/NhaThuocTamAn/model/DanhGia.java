package poly.NhaThuocTamAn.model;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "DanhGia")
public class DanhGia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer maDG;

    @ManyToOne
    @JoinColumn(name = "maKH")
    private KhachHang khachHang;

    @ManyToOne
    @JoinColumn(name = "maBienThe")
    private BienTheThuoc bienTheThuoc;

    @ManyToOne
    @JoinColumn(name = "maThuoc")
    private Thuoc thuoc;

    private String hinhAnh;

    private Integer soSao;

    private String binhLuan;

    private LocalDate ngayDanhGia = LocalDate.now();
}

