package com.eatnow.eatnow.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "chi_tiet_don_hang")
public class ChiTietDonHang {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_don_hang", nullable = false)
    private DonHang donHang;

    @ManyToOne
    @JoinColumn(name = "id_mon_an", nullable = false)
    private MonAn monAn;

    @Column(name = "so_luong", nullable = false)
    private Integer soLuong;

    @Column(name = "don_gia", nullable = false)
    private Double donGia;

    public ChiTietDonHang() {}

    public Double getThanhTien() {
        return donGia * soLuong;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public DonHang getDonHang() { return donHang; }
    public void setDonHang(DonHang donHang) { this.donHang = donHang; }

    public MonAn getMonAn() { return monAn; }
    public void setMonAn(MonAn monAn) { this.monAn = monAn; }

    public Integer getSoLuong() { return soLuong; }
    public void setSoLuong(Integer soLuong) { this.soLuong = soLuong; }

    public Double getDonGia() { return donGia; }
    public void setDonGia(Double donGia) { this.donGia = donGia; }
}