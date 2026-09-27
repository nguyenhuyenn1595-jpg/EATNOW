package com.eatnow.eatnow.service;

import com.eatnow.eatnow.entity.ChiTietDonHang;
import com.eatnow.eatnow.entity.DonHang;
import com.eatnow.eatnow.entity.MonAn;
import com.eatnow.eatnow.entity.NguoiDung;
import com.eatnow.eatnow.repository.DonHangRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class DonHangService {

    @Autowired
    private DonHangRepository donHangRepo;

    @Autowired
    private MonAnService monAnService;

    // Tạo đơn hàng mới từ danh sách (idMonAn, soLuong)
    public DonHang taoDonHang(NguoiDung nguoiDung, String thoiGianNhan,
                              List<Long> idMonAnList, List<Integer> soLuongList) {
        DonHang don = new DonHang();
        don.setNguoiDung(nguoiDung);
        don.setThoiGianNhan(thoiGianNhan);
        don.setNgayDat(LocalDateTime.now());
        don.setTrangThai("ChoThanhToan");
        don.setTongTien(0.0);

        double tongTien = 0;
        List<ChiTietDonHang> chiTietList = new ArrayList<>();

        for (int i = 0; i < idMonAnList.size(); i++) {
            MonAn mon = monAnService.layTheoId(idMonAnList.get(i));
            int soLuong = soLuongList.get(i);

            ChiTietDonHang ct = new ChiTietDonHang();
            ct.setDonHang(don);
            ct.setMonAn(mon);
            ct.setSoLuong(soLuong);
            ct.setDonGia(mon.getGia());

            tongTien += mon.getGia() * soLuong;
            chiTietList.add(ct);
        }

        don.setTongTien(tongTien);
        don.setChiTietList(chiTietList);

        return donHangRepo.save(don);
    }

    // Lấy theo id
    public DonHang layTheoId(Long id) {
        return donHangRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng!"));
    }

    // Lấy đơn hàng của 1 người dùng (mới nhất trước)
    public List<DonHang> layTheoNguoiDung(Long idNguoiDung) {
        return donHangRepo.findByNguoiDungIdOrderByNgayDatDesc(idNguoiDung);
    }

    // Lấy đơn hàng theo trạng thái
    public List<DonHang> layTheoTrangThai(String trangThai) {
        return donHangRepo.findByTrangThaiOrderByNgayDatAsc(trangThai);
    }

    // Lấy tất cả đơn hàng
    public List<DonHang> layTatCa() {
        return donHangRepo.findAll();
    }

    // Cập nhật trạng thái đơn hàng
    public DonHang capNhatTrangThai(Long idDonHang, String trangThaiMoi) {
        DonHang don = layTheoId(idDonHang);
        don.setTrangThai(trangThaiMoi);
        return donHangRepo.save(don);
    }

    // Hủy đơn hàng kèm lý do
    public DonHang huyDon(Long idDonHang, String lyDo) {
        DonHang don = layTheoId(idDonHang);
        don.setTrangThai("DaHuy");
        don.setLyDoHuy(lyDo);
        return donHangRepo.save(don);
    }
}