package com.eatnow.eatnow.repository;

import com.eatnow.eatnow.entity.DonHang;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DonHangRepository extends JpaRepository<DonHang, Long> {
    List<DonHang> findByNguoiDungIdOrderByNgayDatDesc(Long nguoiDungId);
    List<DonHang> findByTrangThaiOrderByNgayDatAsc(String trangThai);
}