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
}

