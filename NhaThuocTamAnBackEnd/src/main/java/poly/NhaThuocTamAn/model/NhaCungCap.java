package poly.NhaThuocTamAn.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "NhaCungCap")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NhaCungCap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer maNCC;

    @Column(nullable = false)
    private String tenNCC;

    private String diaChi;
    private String soDienThoai;
    private String email;
}

