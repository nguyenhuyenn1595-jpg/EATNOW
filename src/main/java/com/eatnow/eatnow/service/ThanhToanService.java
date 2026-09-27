package com.eatnow.eatnow.service;

import com.eatnow.eatnow.entity.DonHang;
import com.eatnow.eatnow.entity.ThanhToan;
import com.eatnow.eatnow.repository.ThanhToanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ThanhToanService {

    @Autowired
    private ThanhToanRepository thanhToanRepo;

    @Autowired
    private NguoiDungService nguoiDungService;

    @Autowired
    private DonHangService donHangService;

    public String layPhuongThucTheoDon(Long idDonHang) {
    return thanhToanRepo.findAll().stream()
            .filter(tt -> tt.getDonHang().getId().equals(idDonHang))
            .map(ThanhToan::getPhuongThuc)
            .findFirst()
            .orElse("Chưa thanh toán");
}

    // Thanh toán đơn hàng qua ví demo
    public ThanhToan thanhToanDonHang(Long idDonHang, String phuongThuc) {
    DonHang don = donHangService.layTheoId(idDonHang);
    Long idNguoiDung = don.getNguoiDung().getId();

    // Chỉ trừ ví nếu thanh toán qua ví
    if ("ViEatNow".equals(phuongThuc)) {
        nguoiDungService.truTienVi(idNguoiDung, don.getTongTien());
    }

    ThanhToan tt = new ThanhToan();
    tt.setDonHang(don);
    tt.setSoTien(don.getTongTien());
    tt.setThoiGian(LocalDateTime.now());
    tt.setPhuongThuc(phuongThuc);
    tt.setTrangThai("ViEatNow".equals(phuongThuc) ? "ThanhCong" : "ChoThanhToan");
    thanhToanRepo.save(tt);

    if ("ViEatNow".equals(phuongThuc)) {
    donHangService.capNhatTrangThai(idDonHang, "DaThanhToan");
} else {
    // Tiền mặt / Chuyển khoản → giữ "ChoThanhToan" nhưng staff vẫn thấy
    donHangService.capNhatTrangThai(idDonHang, "ChoThanhToan");
}

    return tt;
}

    // Hoàn tiền khi đơn bị hủy
    public void hoanTien(Long idDonHang) {
        DonHang don = donHangService.layTheoId(idDonHang);
        nguoiDungService.congTienVi(don.getNguoiDung().getId(), don.getTongTien());
    }
}