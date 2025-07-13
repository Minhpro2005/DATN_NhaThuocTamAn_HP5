package poly.NhaThuocTamAn.model;

import java.math.BigDecimal;
import java.time.LocalDate;

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
@Table(name = "Thuoc")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Thuoc {
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Integer maThuoc;

	    private String tenThuoc;
	    private String donViTinh;
	    private String congDung;
	    private String moTaThem;
	    private String huongDanSuDung;
	    private String dangBaoChe;
	    private String quyCach;
	    private String xuatXu;
	    private String thanhPhan;
	    private LocalDate hanSuDung;
	    private LocalDate ngaySanXuat;
	    private BigDecimal giaBan;
	    private Boolean trangThai;
	    private String hinhAnhChinh;
	    private Integer soLuong;

	    @ManyToOne
	    @JoinColumn(name = "maDM")
	    private DanhMuc danhMuc;

	    @ManyToOne
	    @JoinColumn(name = "maNCC")
	    private NhaCungCap nhaCungCap;

}

