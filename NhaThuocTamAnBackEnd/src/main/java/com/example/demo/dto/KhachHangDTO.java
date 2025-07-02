package com.example.demo.dto;


import java.time.LocalDateTime;

public class KhachHangDTO {

    private Integer maKH;
    private String hoTen;
    private String diaChi;
    private String email;
    private String soDienThoai;
    private String hinhAnh;
    private Boolean trangThai;

    private String maXacThuc;
    private boolean daXacThuc;
    private LocalDateTime thoiGianHetHanMa;

    public KhachHangDTO() {}

    public Integer getMaKH() {
        return maKH;
    }

    public void setMaKH(Integer maKH) {
        this.maKH = maKH;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public String getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
    }

    public Boolean getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(Boolean trangThai) {
        this.trangThai = trangThai;
    }

    public String getMaXacThuc() {
        return maXacThuc;
    }

    public void setMaXacThuc(String maXacThuc) {
        this.maXacThuc = maXacThuc;
    }

    public boolean isDaXacThuc() {
        return daXacThuc;
    }

    public void setDaXacThuc(boolean daXacThuc) {
        this.daXacThuc = daXacThuc;
    }

    public LocalDateTime getThoiGianHetHanMa() {
        return thoiGianHetHanMa;
    }

    public void setThoiGianHetHanMa(LocalDateTime thoiGianHetHanMa) {
        this.thoiGianHetHanMa = thoiGianHetHanMa;
    }
}


