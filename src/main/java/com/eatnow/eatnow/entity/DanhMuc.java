package com.eatnow.eatnow.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "danh_muc")
public class DanhMuc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ten_danh_muc", nullable = false, length = 100)
    private String tenDanhMuc;

    @Column(name = "mo_ta", length = 255)
    private String moTa;

    @OneToMany(mappedBy = "danhMuc", cascade = CascadeType.ALL)
    private List<MonAn> monAnList;

    public DanhMuc() {}

    public DanhMuc(String tenDanhMuc, String moTa) {
        this.tenDanhMuc = tenDanhMuc;
        this.moTa = moTa;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTenDanhMuc() { return tenDanhMuc; }
    public void setTenDanhMuc(String tenDanhMuc) { this.tenDanhMuc = tenDanhMuc; }

    public String getMoTa() { return moTa; }
    public void setMoTa(String moTa) { this.moTa = moTa; }

    public List<MonAn> getMonAnList() { return monAnList; }
    public void setMonAnList(List<MonAn> monAnList) { this.monAnList = monAnList; }
}