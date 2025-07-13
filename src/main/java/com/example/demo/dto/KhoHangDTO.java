package com.example.demo.dto;

import lombok.*;
import java.util.Date;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KhoHangDTO {
    private Integer maKhoHang;
    private Integer maBienThe;
    private String tenBienThe;
    private Integer maThuoc;
    private String tenThuoc;
    private Integer soLuongTon;
    private Date ngayCapNhat;
    private String ghiChu;
    private Integer trangThai;
}


