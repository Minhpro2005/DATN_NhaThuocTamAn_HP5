package poly.NhaThuocTamAn.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "DanhMuc")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DanhMuc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer maDM;

    @Column(name = "tenDanhMuc", nullable = false)
    private String tenDanhMuc;

    @Column(name = "moTa")
    private String moTa;
}
