package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SanPhamCanhBaoDTO {
    private String tenBienThe;
    private int soLuong;
    
	public SanPhamCanhBaoDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public SanPhamCanhBaoDTO(String tenBienThe, int soLuong) {
		super();
		this.tenBienThe = tenBienThe;
		this.soLuong = soLuong;
	}

	public String getTenBienThe() {
		return tenBienThe;
	}

	public void setTenBienThe(String tenBienThe) {
		this.tenBienThe = tenBienThe;
	}

	public int getSoLuong() {
		return soLuong;
	}

	public void setSoLuong(int soLuong) {
		this.soLuong = soLuong;
	}
    
	
}
