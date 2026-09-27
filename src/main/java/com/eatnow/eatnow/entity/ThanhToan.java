package com.eatnow.eatnow.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "thanh_toan")
public class ThanhToan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "id_don_hang", nullable = false)
    private DonHang donHang;

    @Column(name = "so_tien", nullable = false)
    private Double soTien;

    @Column(name = "thoi_gian")
    private LocalDateTime thoiGian = LocalDateTime.now();

    @Column(name = "phuong_thuc", length = 50)
    private String phuongThuc = "ViEatNow";

    @Column(name = "trang_thai", length = 30)
    private String trangThai = "ThanhCong";

    public ThanhToan() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public DonHang getDonHang() { return donHang; }
    public void setDonHang(DonHang donHang) { this.donHang = donHang; }

    public Double getSoTien() { return soTien; }
    public void setSoTien(Double soTien) { this.soTien = soTien; }

    public LocalDateTime getThoiGian() { return thoiGian; }
    public void setThoiGian(LocalDateTime thoiGian) { this.thoiGian = thoiGian; }

    public String getPhuongThuc() { return phuongThuc; }
    public void setPhuongThuc(String phuongThuc) { this.phuongThuc = phuongThuc; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
}