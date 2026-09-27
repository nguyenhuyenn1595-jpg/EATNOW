package com.eatnow.eatnow.controller;

import com.eatnow.eatnow.entity.DonHang;
import com.eatnow.eatnow.entity.NguoiDung;
import com.eatnow.eatnow.service.DonHangService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/staff")
public class StaffController {

    @Autowired
    private DonHangService donHangService;

    // Kiểm tra quyền nhân viên
    private boolean laNhanVien(HttpSession session) {
        NguoiDung nd = (NguoiDung) session.getAttribute("nguoiDung");
        return nd != null && ("NhanVien".equals(nd.getVaiTro()) || "Admin".equals(nd.getVaiTro()));
    }

    // Trang chính - danh sách đơn mới
    @GetMapping
    public String trangStaff(HttpSession session, Model model) {
        if (!laNhanVien(session)) return "redirect:/login";

        NguoiDung nd = (NguoiDung) session.getAttribute("nguoiDung");
        List<DonHang> dsDaThanhToan = donHangService.layTheoTrangThai("DaThanhToan");
List<DonHang> dsChoThanhToan = donHangService.layTheoTrangThai("ChoThanhToan");
List<DonHang> dsDonMoi = new ArrayList<>();
dsDonMoi.addAll(dsDaThanhToan);
dsDonMoi.addAll(dsChoThanhToan);
        List<DonHang> dsDangCheBien = donHangService.layTheoTrangThai("DangCheBien");
        List<DonHang> dsSanSang = donHangService.layTheoTrangThai("DaSanSang");

        model.addAttribute("nguoiDung", nd);
        model.addAttribute("dsDonMoi", dsDonMoi);
        model.addAttribute("dsDangCheBien", dsDangCheBien);
         model.addAttribute("dsSanSang", dsSanSang);
        return "staff";
    }

    // Xác nhận đơn → chuyển sang Đang chế biến
    @PostMapping("/xac-nhan/{id}")
public String xacNhanDon(@PathVariable Long id, HttpSession session) {
    if (!laNhanVien(session)) return "redirect:/login";
    donHangService.capNhatTrangThai(id, "DangCheBien");
   return "redirect:/staff?tab=don-moi";    
}

    // Từ chối đơn → hủy + hoàn tiền
    @PostMapping("/tu-choi/{id}")
    public String tuChoiDon(@PathVariable Long id,
                            @RequestParam(required = false) String lyDo,
                            HttpSession session) {
        if (!laNhanVien(session)) return "redirect:/login";

        DonHang don = donHangService.layTheoId(id);
        // Nếu đã thanh toán → hoàn tiền
        if ("DaThanhToan".equals(don.getTrangThai())) {
            // Sẽ hoàn tiền qua service
        }
        donHangService.huyDon(id, lyDo != null ? lyDo : "Căn tin từ chối đơn");
        return "redirect:/staff";
    }

    // Cập nhật "Đã sẵn sàng"
    @PostMapping("/san-sang/{id}")
public String danhDauSanSang(@PathVariable Long id, HttpSession session) {
    if (!laNhanVien(session)) return "redirect:/login";
    donHangService.capNhatTrangThai(id, "DaSanSang");
   return "redirect:/staff?tab=dang-lam"; 
}

    // Cập nhật "Đã giao"
    @PostMapping("/da-giao/{id}")
public String danhDauDaGiao(@PathVariable Long id, HttpSession session) {
    if (!laNhanVien(session)) return "redirect:/login";
    donHangService.capNhatTrangThai(id, "DaGiao");
     return "redirect:/staff?tab=da-xong";
}
}