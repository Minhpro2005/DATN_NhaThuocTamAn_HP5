package poly.NhaThuocTamAn.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//File: poly.NhaThuocTamAn.model.KhachHang.java

@Entity
@Table(name = "KhachHang")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class KhachHang {

 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 private Integer maKH;

 @Column(nullable = false, length = 100)
 private String matKhau;

 @Column(nullable = false)
 private String hoTen;

 private String diaChi;

 @Column(unique = true)
 private String email;

 @Column(length = 15)
 private String soDienThoai;

 private String hinhAnh;

 private Boolean trangThai = true;

 private String maXacThuc;

 // ✅ Đổi kiểu boolean -> Boolean
 private Boolean daXacThuc;

 private LocalDateTime thoiGianHetHanMa;
 
 public KhachHang(Integer maKH) {
	    this.maKH = maKH;
	}

}
