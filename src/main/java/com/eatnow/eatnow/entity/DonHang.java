package com.eatnow.eatnow.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "don_hang")
public class DonHang {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_nguoi_dung", nullable = false)
    private NguoiDung nguoiDung;

    @Column(name = "ngay_dat")
    private LocalDateTime ngayDat = LocalDateTime.now();

    @Column(name = "thoi_gian_nhan", length = 50)
    private String thoiGianNhan;

    @Column(name = "trang_thai", length = 30)
    private String trangThai = "ChoThanhToan";

    @Column(name = "tong_tien")
    private Double tongTien = 0.0;

    @Column(name = "ly_do_huy")
    private String lyDoHuy;

    @OneToMany(mappedBy = "donHang", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ChiTietDonHang> chiTietList;

    public DonHang() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public NguoiDung getNguoiDung() { return nguoiDung; }
    public void setNguoiDung(NguoiDung nguoiDung) { this.nguoiDung = nguoiDung; }

    public LocalDateTime getNgayDat() { return ngayDat; }
    public void setNgayDat(LocalDateTime ngayDat) { this.ngayDat = ngayDat; }

    public String getThoiGianNhan() { return thoiGianNhan; }
    public void setThoiGianNhan(String thoiGianNhan) { this.thoiGianNhan = thoiGianNhan; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

    public Double getTongTien() { return tongTien; }
    public void setTongTien(Double tongTien) { this.tongTien = tongTien; }

    public String getLyDoHuy() { return lyDoHuy; }
    public void setLyDoHuy(String lyDoHuy) { this.lyDoHuy = lyDoHuy; }

    public List<ChiTietDonHang> getChiTietList() { return chiTietList; }
    public void setChiTietList(List<ChiTietDonHang> chiTietList) { this.chiTietList = chiTietList; }
}