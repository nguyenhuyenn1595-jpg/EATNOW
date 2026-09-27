package com.eatnow.eatnow.repository;

import com.eatnow.eatnow.entity.DanhGia;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DanhGiaRepository extends JpaRepository<DanhGia, Long> {
    List<DanhGia> findByMonAnId(Long monAnId);
}