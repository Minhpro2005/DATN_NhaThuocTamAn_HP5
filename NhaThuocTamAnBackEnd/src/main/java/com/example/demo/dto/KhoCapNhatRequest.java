package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;

//📁 com.example.demo.dto.KhoCapNhatRequest.java

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class KhoCapNhatRequest {
	private Integer maThuoc;
	private Integer maBienThe;
	private int soLuongThem;
	
	public KhoCapNhatRequest() {
		super();
		// TODO Auto-generated constructor stub
	}

	public KhoCapNhatRequest(Integer maThuoc, Integer maBienThe, int soLuongThem) {
		super();
		this.maThuoc = maThuoc;
		this.maBienThe = maBienThe;
		this.soLuongThem = soLuongThem;
	}

	public Integer getMaThuoc() {
		return maThuoc;
	}

	public void setMaThuoc(Integer maThuoc) {
		this.maThuoc = maThuoc;
	}

	public Integer getMaBienThe() {
		return maBienThe;
	}

	public void setMaBienThe(Integer maBienThe) {
		this.maBienThe = maBienThe;
	}

	public int getSoLuongThem() {
		return soLuongThem;
	}

	public void setSoLuongThem(int soLuongThem) {
		this.soLuongThem = soLuongThem;
	}
	
}

