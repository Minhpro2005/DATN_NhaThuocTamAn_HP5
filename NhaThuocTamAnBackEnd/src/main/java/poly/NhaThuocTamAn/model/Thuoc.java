package poly.NhaThuocTamAn.model;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Thuoc")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Thuoc {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer maThuoc;
	
	private String tenThuoc;
	
	private String hoatChat;
	
	private String congDung;
	
	@Column(length = 20)
	private String donViTinh;
	
	private Double giaNhap;
	
	private Double giaBan;
	
	@Temporal(TemporalType.DATE)
	private Date hanSuDung;
	
	@Column(length = 100)
	private String xuatXu;
	
	@Temporal(TemporalType.DATE)
	private Date ngaySanXuat;
	
	private String moTaThem;
	
	@Column(length = 50)
	private String donViQuyDoi;
	
	private String huongDanSuDung;
	
	private Boolean trangThai;
	
	@ManyToOne
	@JoinColumn(name = "maDM", table = "DanhMuc")
	private DanhMuc maDM;
	
	@ManyToOne
	@JoinColumn(name = "maNCC", table = "NhaCungCap")
	private NhaCungCap maNCC;

	public Thuoc() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Thuoc(Integer maThuoc, String tenThuoc, String hoatChat, String congDung, String donViTinh, Double giaNhap,
			Double giaBan, Date hanSuDung, String xuatXu, Date ngaySanXuat, String moTaThem, String donViQuyDoi,
			String huongDanSuDung, Boolean trangThai, DanhMuc maDM, NhaCungCap maNCC) {
		super();
		this.maThuoc = maThuoc;
		this.tenThuoc = tenThuoc;
		this.hoatChat = hoatChat;
		this.congDung = congDung;
		this.donViTinh = donViTinh;
		this.giaNhap = giaNhap;
		this.giaBan = giaBan;
		this.hanSuDung = hanSuDung;
		this.xuatXu = xuatXu;
		this.ngaySanXuat = ngaySanXuat;
		this.moTaThem = moTaThem;
		this.donViQuyDoi = donViQuyDoi;
		this.huongDanSuDung = huongDanSuDung;
		this.trangThai = trangThai;
		this.maDM = maDM;
		this.maNCC = maNCC;
	}

	public Integer getMaThuoc() {
		return maThuoc;
	}

	public void setMaThuoc(Integer maThuoc) {
		this.maThuoc = maThuoc;
	}

	public String getTenThuoc() {
		return tenThuoc;
	}

	public void setTenThuoc(String tenThuoc) {
		this.tenThuoc = tenThuoc;
	}

	public String getHoatChat() {
		return hoatChat;
	}

	public void setHoatChat(String hoatChat) {
		this.hoatChat = hoatChat;
	}

	public String getCongDung() {
		return congDung;
	}

	public void setCongDung(String congDung) {
		this.congDung = congDung;
	}

	public String getDonViTinh() {
		return donViTinh;
	}

	public void setDonViTinh(String donViTinh) {
		this.donViTinh = donViTinh;
	}

	public Double getGiaNhap() {
		return giaNhap;
	}

	public void setGiaNhap(Double giaNhap) {
		this.giaNhap = giaNhap;
	}

	public Double getGiaBan() {
		return giaBan;
	}

	public void setGiaBan(Double giaBan) {
		this.giaBan = giaBan;
	}

	public Date getHanSuDung() {
		return hanSuDung;
	}

	public void setHanSuDung(Date hanSuDung) {
		this.hanSuDung = hanSuDung;
	}

	public String getXuatXu() {
		return xuatXu;
	}

	public void setXuatXu(String xuatXu) {
		this.xuatXu = xuatXu;
	}

	public Date getNgaySanXuat() {
		return ngaySanXuat;
	}

	public void setNgaySanXuat(Date ngaySanXuat) {
		this.ngaySanXuat = ngaySanXuat;
	}

	public String getMoTaThem() {
		return moTaThem;
	}

	public void setMoTaThem(String moTaThem) {
		this.moTaThem = moTaThem;
	}

	public String getDonViQuyDoi() {
		return donViQuyDoi;
	}

	public void setDonViQuyDoi(String donViQuyDoi) {
		this.donViQuyDoi = donViQuyDoi;
	}

	public String getHuongDanSuDung() {
		return huongDanSuDung;
	}

	public void setHuongDanSuDung(String huongDanSuDung) {
		this.huongDanSuDung = huongDanSuDung;
	}

	public Boolean getTrangThai() {
		return trangThai;
	}

	public void setTrangThai(Boolean trangThai) {
		this.trangThai = trangThai;
	}

	public DanhMuc getMaDM() {
		return maDM;
	}

	public void setMaDM(DanhMuc maDM) {
		this.maDM = maDM;
	}

	public NhaCungCap getMaNCC() {
		return maNCC;
	}

	public void setMaNCC(NhaCungCap maNCC) {
		this.maNCC = maNCC;
	}
	
	

}
