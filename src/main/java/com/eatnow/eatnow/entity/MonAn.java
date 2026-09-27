package com.eatnow.eatnow.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "mon_an")
public class MonAn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ten_mon", nullable = false, length = 150)
    private String tenMon;

    @Column(name = "gia", nullable = false)
    private Double gia;

    @Column(name = "hinh_anh")
    private String hinhAnh;

    @Column(name = "mo_ta", columnDefinition = "TEXT")
    private String moTa;

    @Column(name = "con_hang")
    private Boolean conHang = true;
    @Column(name = "so_sao_trung_binh")
    private Double soSaoTrungBinh = 0.0;

    @Column(name = "luot_mua")
    private Integer luotMua = 0;

    @ManyToOne
    @JoinColumn(name = "id_danh_muc")
    private DanhMuc danhMuc;

    public MonAn() {}

    public MonAn(String tenMon, Double gia, String hinhAnh, String moTa, DanhMuc danhMuc) {
        this.tenMon = tenMon;
        this.gia = gia;
        this.hinhAnh = hinhAnh;
        this.moTa = moTa;
        this.danhMuc = danhMuc;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTenMon() { return tenMon; }
    public void setTenMon(String tenMon) { this.tenMon = tenMon; }

    public Double getGia() { return gia; }
    public void setGia(Double gia) { this.gia = gia; }

    public String getHinhAnh() { return hinhAnh; }
    public void setHinhAnh(String hinhAnh) { this.hinhAnh = hinhAnh; }

    public String getMoTa() { return moTa; }
    public void setMoTa(String moTa) { this.moTa = moTa; }

    public Boolean getConHang() { return conHang; }
    public void setConHang(Boolean conHang) { this.conHang = conHang; }

    public DanhMuc getDanhMuc() { return danhMuc; }
    public void setDanhMuc(DanhMuc danhMuc) { this.danhMuc = danhMuc; }

    public Double getSoSaoTrungBinh() { return soSaoTrungBinh; }
    public void setSoSaoTrungBinh(Double soSaoTrungBinh) { this.soSaoTrungBinh = soSaoTrungBinh; }

    public Integer getLuotMua() { return luotMua; }
    public void setLuotMua(Integer luotMua) { this.luotMua = luotMua; }
}