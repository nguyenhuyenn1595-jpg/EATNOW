package com.eatnow.eatnow.service;

import com.eatnow.eatnow.entity.DanhGia;
import com.eatnow.eatnow.repository.DanhGiaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DanhGiaService {

    @Autowired
    private DanhGiaRepository danhGiaRepo;

    // Lấy đánh giá theo món ăn
    public List<DanhGia> layTheoMonAn(Long idMonAn) {
        return danhGiaRepo.findByMonAnId(idMonAn);
    }

    // Thêm đánh giá mới
    public DanhGia them(DanhGia dg) {
        dg.setNgayTao(LocalDateTime.now());
        return danhGiaRepo.save(dg);
    }

    // Lấy tất cả đánh giá
    public List<DanhGia> layTatCa() {
        return danhGiaRepo.findAll();
    }
}