package com.eatnow.eatnow.service;

import com.eatnow.eatnow.entity.DanhMuc;
import com.eatnow.eatnow.repository.DanhMucRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DanhMucService {

    @Autowired
    private DanhMucRepository danhMucRepo;

    // Lấy tất cả danh mục
    public List<DanhMuc> layTatCa() {
        return danhMucRepo.findAll();
    }

    // Lấy theo id
    public DanhMuc layTheoId(Long id) {
        return danhMucRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy danh mục!"));
    }

    // Thêm danh mục mới
    public DanhMuc them(DanhMuc dm) {
        return danhMucRepo.save(dm);
    }

    // Xóa danh mục
    public void xoa(Long id) {
        danhMucRepo.deleteById(id);
    }
}