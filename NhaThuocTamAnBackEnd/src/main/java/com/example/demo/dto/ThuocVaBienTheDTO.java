package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import poly.NhaThuocTamAn.model.BienTheThuoc;
import poly.NhaThuocTamAn.model.Thuoc;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ThuocVaBienTheDTO {
    private Thuoc thuoc;
    private BienTheThuoc bienThe;
}
