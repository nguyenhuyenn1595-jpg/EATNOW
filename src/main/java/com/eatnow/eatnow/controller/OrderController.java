package com.eatnow.eatnow.controller;

import com.eatnow.eatnow.entity.DonHang;
import com.eatnow.eatnow.entity.NguoiDung;
import com.eatnow.eatnow.service.DonHangService;
import com.eatnow.eatnow.service.ThanhToanService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class OrderController {

    @Autowired
    private DonHangService donHangService;

    @Autowired
    private ThanhToanService thanhToanService;

    // Xem lịch sử đơn hàng của mình
    @GetMapping("/don-hang/{id}")
public String chiTietDonHang(@PathVariable Long id,
                             HttpSession session,
                             Model model) {
    NguoiDung nd = (NguoiDung) session.getAttribute("nguoiDung");
    if (nd == null) return "redirect:/login";

    DonHang don = donHangService.layTheoId(id);
    
    // Lấy phương thức thanh toán
    String phuongThuc = thanhToanService.layPhuongThucTheoDon(id);

    model.addAttribute("donHang", don);
    model.addAttribute("nguoiDung", nd);
    model.addAttribute("phuongThuc", phuongThuc);
    return "order-detail";
}

    // Xem lịch sử đơn hàng của mình
@GetMapping("/don-hang-cua-toi")
public String lichSuDonHang(HttpSession session, Model model) {
    NguoiDung nd = (NguoiDung) session.getAttribute("nguoiDung");
    if (nd == null) return "redirect:/login";

    List<DonHang> dsDonHang = donHangService.layTheoNguoiDung(nd.getId());
    model.addAttribute("dsDonHang", dsDonHang);
    model.addAttribute("nguoiDung", nd);
    return "order-history";
}

    // Hủy đơn hàng (chỉ khi chưa thanh toán hoặc đang chờ xác nhận)
    @PostMapping("/don-hang/{id}/huy")
    public String huyDonHang(@PathVariable Long id,
                             @RequestParam(required = false) String lyDo,
                             HttpSession session) {
        NguoiDung nd = (NguoiDung) session.getAttribute("nguoiDung");
        if (nd == null) return "redirect:/login";

        DonHang don = donHangService.layTheoId(id);

        // Nếu đã thanh toán → hoàn tiền
        if ("DaThanhToan".equals(don.getTrangThai()) || "DangCheBien".equals(don.getTrangThai())) {
            thanhToanService.hoanTien(id);
        }

        donHangService.huyDon(id, lyDo != null ? lyDo : "Người dùng hủy đơn");
        return "redirect:/don-hang-cua-toi";
    }
}