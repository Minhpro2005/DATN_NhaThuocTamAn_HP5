package com.example.demo.dto;


public class DangKyDTO {
	private String hoTen;
    private String matKhau;
    private String email;

    public DangKyDTO() {}

	public DangKyDTO(String hoTen, String matKhau, String email) {
		super();
		this.hoTen = hoTen;
		this.matKhau = matKhau;
		this.email = email;
	}

	public String getHoTen() {
		return hoTen;
	}

	public void setHoTen(String hoTen) {
		this.hoTen = hoTen;
	}

	public String getMatKhau() {
		return matKhau;
	}

	public void setMatKhau(String matKhau) {
		this.matKhau = matKhau;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

  

    
}

