package com.example.demo.dto;

import lombok.*;

@Data
@Builder   
@NoArgsConstructor
@AllArgsConstructor
public class NhaCungCapDTO {
	private Integer maNCC;
    private String tenNCC;
    private String diaChi;
    private String soDienThoai;
    private String email;
    
	public NhaCungCapDTO(Integer maNCC, String tenNCC, String diaChi, String soDienThoai, String email) {
		super();
		this.maNCC = maNCC;
		this.tenNCC = tenNCC;
		this.diaChi = diaChi;
		this.soDienThoai = soDienThoai;
		this.email = email;
	}

	public NhaCungCapDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Integer getMaNCC() {
		return maNCC;
	}

	public void setMaNCC(Integer maNCC) {
		this.maNCC = maNCC;
	}

	public String getTenNCC() {
		return tenNCC;
	}

	public void setTenNCC(String tenNCC) {
		this.tenNCC = tenNCC;
	}

	public String getDiaChi() {
		return diaChi;
	}

	public void setDiaChi(String diaChi) {
		this.diaChi = diaChi;
	}

	public String getSoDienThoai() {
		return soDienThoai;
	}

	public void setSoDienThoai(String soDienThoai) {
		this.soDienThoai = soDienThoai;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
    
	
    
}

