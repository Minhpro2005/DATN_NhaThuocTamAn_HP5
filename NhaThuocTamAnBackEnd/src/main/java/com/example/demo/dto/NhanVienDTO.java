package com.example.demo.dto;

import lombok.*;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NhanVienDTO {
    private Integer maNV;
    private String hoTen;
    private String email;
    private String soDienThoai;
    private String cccd;
    private Date ngaySinh;
    private Boolean gioiTinh;
    private String diaChi;
    private String hinhAnh;
    private Integer vaiTro;
    private Boolean trangThai;
}

