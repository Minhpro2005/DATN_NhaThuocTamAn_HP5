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
}

