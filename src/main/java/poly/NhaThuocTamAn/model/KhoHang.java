package poly.NhaThuocTamAn.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "KhoHang")
public class KhoHang {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "maKhoHang")
    private Integer maKhoHang;

    @OneToOne
    @JoinColumn(name = "maThuoc", unique = true)
    private Thuoc thuoc;

    @OneToOne
    @JoinColumn(name = "maBienThe", unique = true)
    private BienTheThuoc bienTheThuoc;

    private Integer soLuongTon;

    @Temporal(TemporalType.TIMESTAMP)
    private Date ngayCapNhat;

    private String ghiChu;

    private Integer trangThai; // 0: Bình thường, 1: Sắp hết, 2: Hết hàng, 3: Ngừng KD
}

