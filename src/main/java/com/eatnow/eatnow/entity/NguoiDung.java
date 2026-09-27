package com.eatnow.eatnow.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "nguoi_dung")
public class NguoiDung {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ho_ten", nullable = false, length = 100)
    private String hoTen;

    @Column(name = "email", unique = true, nullable = false, length = 100)
    private String email;

    @Column(name = "mat_khau", nullable = false)
    private String matKhau;

    @Column(name = "sdt", length = 15)
    private String sdt;

    @Column(name = "vai_tro", length = 20)
    private String vaiTro = "SinhVien";

    @Column(name = "so_du_vi")
    private Double soDuVi = 100000.0;

    @OneToMany(mappedBy = "nguoiDung", cascade = CascadeType.ALL)
    private List<DonHang> donHangList;

    public NguoiDung() {}

    public NguoiDung(String hoTen, String email, String matKhau, String sdt, String vaiTro) {
        this.hoTen = hoTen;
        this.email = email;
        this.matKhau = matKhau;
        this.sdt = sdt;
        this.vaiTro = vaiTro;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getHoTen() { return hoTen; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getMatKhau() { return matKhau; }
    public void setMatKhau(String matKhau) { this.matKhau = matKhau; }

    public String getSdt() { return sdt; }
    public void setSdt(String sdt) { this.sdt = sdt; }

    public String getVaiTro() { return vaiTro; }
    public void setVaiTro(String vaiTro) { this.vaiTro = vaiTro; }

    public Double getSoDuVi() { return soDuVi; }
    public void setSoDuVi(Double soDuVi) { this.soDuVi = soDuVi; }

    public List<DonHang> getDonHangList() { return donHangList; }
    public void setDonHangList(List<DonHang> donHangList) { this.donHangList = donHangList; }
}