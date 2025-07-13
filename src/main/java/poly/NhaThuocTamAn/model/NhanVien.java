package poly.NhaThuocTamAn.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "NhanVien")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NhanVien {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer maNV;

    @Column(nullable = false)
    private String matKhau;

    @Column(nullable = false)
    private String hoTen;

    private String email;

    private String soDienThoai;

    @Column(unique = true)
    private String cccd;

    private Date ngaySinh;

    private Boolean gioiTinh;  // true: Nam, false: Nữ

    private String diaChi;

    private String hinhAnh;

    @Column(nullable = false)
    private Integer vaiTro; // 1: Quản lý, 2: Nhân viên

    @Column(columnDefinition = "bit default 1")
    private Boolean trangThai = true;
}

