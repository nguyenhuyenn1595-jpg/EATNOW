package com.eatnow.eatnow.repository;

import com.eatnow.eatnow.entity.MonAn;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MonAnRepository extends JpaRepository<MonAn, Long> {
    List<MonAn> findByDanhMucId(Long danhMucId);
    List<MonAn> findByConHangTrue();
    List<MonAn> findByTenMonContainingIgnoreCase(String keyword);
}