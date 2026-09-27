package com.eatnow.eatnow.service;

import com.eatnow.eatnow.entity.MonAn;
import com.eatnow.eatnow.repository.MonAnRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MonAnService {

    @Autowired
    private MonAnRepository monAnRepo;

    // Lấy tất cả món ăn
    public List<MonAn> layTatCa() {
        return monAnRepo.findAll();
    }

    // Lấy món theo danh mục
    public List<MonAn> layTheoDanhMuc(Long idDanhMuc) {
        return monAnRepo.findByDanhMucId(idDanhMuc);
    }

    // Lấy món còn hàng
    public List<MonAn> layMonConHang() {
        return monAnRepo.findByConHangTrue();
    }

    // Tìm kiếm món theo tên
    public List<MonAn> timKiem(String keyword) {
        return monAnRepo.findByTenMonContainingIgnoreCase(keyword);
    }

    // Lấy theo id
    public MonAn layTheoId(Long id) {
        return monAnRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy món ăn!"));
    }

    // Thêm hoặc cập nhật món
    public MonAn themHoacCapNhat(MonAn mon) {
        return monAnRepo.save(mon);
    }

    // Xóa món
    public void xoa(Long id) {
        monAnRepo.deleteById(id);
    }
}