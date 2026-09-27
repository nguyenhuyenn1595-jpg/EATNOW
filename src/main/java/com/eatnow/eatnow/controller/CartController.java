package com.eatnow.eatnow.controller;

import com.eatnow.eatnow.entity.DonHang;
import com.eatnow.eatnow.entity.MonAn;
import com.eatnow.eatnow.entity.NguoiDung;
import com.eatnow.eatnow.service.DonHangService;
import com.eatnow.eatnow.service.MonAnService;
import com.eatnow.eatnow.service.NguoiDungService;
import com.eatnow.eatnow.service.ThanhToanService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.eatnow.eatnow.entity.MonAn;
import com.eatnow.eatnow.service.NguoiDungService;
import java.util.HashMap;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class CartController {

    @Autowired
    private MonAnService monAnService;

    @Autowired
    private DonHangService donHangService;

    @Autowired
    private ThanhToanService thanhToanService;
    
    @Autowired
    private NguoiDungService nguoiDungService;

    

    // Lấy giỏ hàng từ session (dạng Map: idMonAn -> soLuong)
    @SuppressWarnings("unchecked")
    private Map<Long, Integer> layGioHang(HttpSession session) {
        Map<Long, Integer> gioHang = (Map<Long, Integer>) session.getAttribute("gioHang");
        if (gioHang == null) {
            gioHang = new HashMap<>();
            session.setAttribute("gioHang", gioHang);
        }
        return gioHang;
    }

    // Thêm món vào giỏ
   @PostMapping("/gio-hang/them/{idMonAn}")
public String themVaoGio(@PathVariable Long idMonAn,
                         @RequestParam(required = false) Long danhMuc,
                         @RequestParam(required = false) String keyword,
                         @RequestParam(required = false) String canh,
                         @RequestParam(required = false) String ghiChu,
                         @RequestParam(required = false) String fromDetail,
                         HttpSession session,
                         RedirectAttributes redirectAttributes) {

    // Thêm món vào giỏ
    Map<Long, Integer> gioHang = layGioHang(session);
    gioHang.put(idMonAn, gioHang.getOrDefault(idMonAn, 0) + 1);

    // Nối canh + ghi chú thành 1 chuỗi
    StringBuilder sb = new StringBuilder();
    if (canh != null && !canh.isEmpty()) {
        sb.append(canh);
    }
    if (ghiChu != null && !ghiChu.trim().isEmpty()) {
        if (sb.length() > 0) sb.append(" - ");
        sb.append(ghiChu.trim());
    }

    // Lưu vào session
    if (sb.length() > 0) {
        Map<Long, String> ghiChuMap = (Map<Long, String>) session.getAttribute("ghiChuMap");
        if (ghiChuMap == null) {
            ghiChuMap = new HashMap<>();
            session.setAttribute("ghiChuMap", ghiChuMap);
        }
        ghiChuMap.put(idMonAn, sb.toString());
    }

    // Thông báo
    MonAn mon = monAnService.layTheoId(idMonAn);
    redirectAttributes.addFlashAttribute("thongBao",
            "Đã thêm \"" + mon.getTenMon() + "\" vào giỏ hàng thành công!");

    // Redirect
    if (danhMuc != null) {
        return "redirect:/menu?danhMuc=" + danhMuc + "#thuc-don";
    }
    if (keyword != null && !keyword.isEmpty()) {
        return "redirect:/menu?keyword=" + keyword + "#thuc-don";
    }
    return "redirect:/menu#thuc-don";
}

    // Xem giỏ hàng
    @GetMapping("/gio-hang")
    public String xemGioHang(HttpSession session, Model model) {
        NguoiDung nd = (NguoiDung) session.getAttribute("nguoiDung");
        if (nd == null) {
            return "redirect:/login";
        }

        Map<Long, Integer> gioHang = layGioHang(session);
        List<Map<String, Object>> dsChiTiet = new ArrayList<>();
        double tongTien = 0;

        for (Map.Entry<Long, Integer> entry : gioHang.entrySet()) {
            MonAn mon = monAnService.layTheoId(entry.getKey());
            int soLuong = entry.getValue();
            double thanhTien = mon.getGia() * soLuong;

            Map<String, Object> item = new HashMap<>();
            item.put("mon", mon);
            item.put("soLuong", soLuong);
            item.put("thanhTien", thanhTien);
            dsChiTiet.add(item);

            tongTien += thanhTien;
        }

        model.addAttribute("dsChiTiet", dsChiTiet);
        model.addAttribute("tongTien", tongTien);
        model.addAttribute("nguoiDung", nd);
         Map<Long, String> ghiChuMap = (Map<Long, String>) session.getAttribute("ghiChuMap");
    model.addAttribute("ghiChuMap", ghiChuMap);
        return "cart";
    }

    // Tăng số lượng
    @PostMapping("/gio-hang/tang/{idMonAn}")
    public String tangSoLuong(@PathVariable Long idMonAn, HttpSession session) {
        Map<Long, Integer> gioHang = layGioHang(session);
        gioHang.put(idMonAn, gioHang.getOrDefault(idMonAn, 0) + 1);
        return "redirect:/gio-hang";
    }

    // Giảm số lượng
    @PostMapping("/gio-hang/giam/{idMonAn}")
    public String giamSoLuong(@PathVariable Long idMonAn, HttpSession session) {
        Map<Long, Integer> gioHang = layGioHang(session);
        int soLuong = gioHang.getOrDefault(idMonAn, 0);
        if (soLuong > 1) {
            gioHang.put(idMonAn, soLuong - 1);
        } else {
            gioHang.remove(idMonAn);
        }
        return "redirect:/gio-hang";
    }

    // Xóa món khỏi giỏ
    @PostMapping("/gio-hang/xoa/{idMonAn}")
    public String xoaKhoiGio(@PathVariable Long idMonAn, HttpSession session) {
        Map<Long, Integer> gioHang = layGioHang(session);
        gioHang.remove(idMonAn);
        return "redirect:/gio-hang";
    }

    // Trang xác nhận đặt món (chọn giờ nhận)
    @GetMapping("/dat-mon")
    public String trangDatMon(HttpSession session, Model model) {
        NguoiDung nd = (NguoiDung) session.getAttribute("nguoiDung");
        if (nd == null) return "redirect:/login";

        Map<Long, Integer> gioHang = layGioHang(session);
        if (gioHang.isEmpty()) return "redirect:/gio-hang";

        double tongTien = 0;
        for (Map.Entry<Long, Integer> entry : gioHang.entrySet()) {
            MonAn mon = monAnService.layTheoId(entry.getKey());
            tongTien += mon.getGia() * entry.getValue();
        }

        model.addAttribute("tongTien", tongTien);
        model.addAttribute("nguoiDung", nd);
        return "checkout";
    }

    // Xác nhận đặt món - tạo đơn hàng
    @PostMapping("/dat-mon/xac-nhan")
public String xacNhanDatMon(@RequestParam String thoiGianNhan,
                            HttpSession session,
                            Model model) {
    NguoiDung nd = (NguoiDung) session.getAttribute("nguoiDung");
    if (nd == null) return "redirect:/login";

    Map<Long, Integer> gioHang = layGioHang(session);
    if (gioHang.isEmpty()) return "redirect:/gio-hang";

    List<Long> idMonAnList = new ArrayList<>(gioHang.keySet());
    List<Integer> soLuongList = new ArrayList<>();
    for (Long id : idMonAnList) {
        soLuongList.add(gioHang.get(id));
    }

    DonHang don = donHangService.taoDonHang(nd, thoiGianNhan, idMonAnList, soLuongList);

    // Xóa giỏ hàng sau khi đặt
    

    return "redirect:/thanh-toan/" + don.getId();
}

    // Trang thanh toán
    @GetMapping("/thanh-toan/{idDonHang}")
    public String trangThanhToan(@PathVariable Long idDonHang,
                                  HttpSession session,
                                  Model model) {
        NguoiDung nd = (NguoiDung) session.getAttribute("nguoiDung");
        if (nd == null) return "redirect:/login";

        DonHang don = donHangService.layTheoId(idDonHang);
        model.addAttribute("donHang", don);
        model.addAttribute("nguoiDung", nd);
        return "payment";
    }

    // Xử lý thanh toán
   @PostMapping("/thanh-toan/{idDonHang}/xac-nhan")
public String xuLyThanhToan(@PathVariable Long idDonHang,
                             @RequestParam String phuongThuc,
                             HttpSession session,
                             RedirectAttributes redirectAttributes,
                             Model model) {
    NguoiDung nd = (NguoiDung) session.getAttribute("nguoiDung");
    if (nd == null) return "redirect:/login";

    try {
        thanhToanService.thanhToanDonHang(idDonHang, phuongThuc);

        // Cập nhật số dư ví trong session
        NguoiDung ndMoi = nguoiDungService.layTheoId(nd.getId());
        session.setAttribute("nguoiDung", ndMoi);

        // Xóa giỏ hàng
        session.removeAttribute("gioHang");
        session.removeAttribute("ghiChuMap");

        // Thông báo thành công
        redirectAttributes.addFlashAttribute("thongBao", 
            "🎉 Đặt hàng thành công! Đơn hàng #DH" + idDonHang + " đã được tạo.");

        return "redirect:/menu";
    } catch (RuntimeException e) {
        model.addAttribute("loi", e.getMessage());
        model.addAttribute("donHang", donHangService.layTheoId(idDonHang));
        model.addAttribute("nguoiDung", nd);
        return "payment";
    }

}
    // Trang nạp tiền
@GetMapping("/nap-tien")
public String trangNapTien(HttpSession session, Model model) {
    NguoiDung nd = (NguoiDung) session.getAttribute("nguoiDung");
    if (nd == null) return "redirect:/login";
    model.addAttribute("nguoiDung", nd);
    return "nap-tien";
}

// Xác nhận nạp tiền
@GetMapping("/nap-tien/xac-nhan")
public String xacNhanNapTien(@RequestParam Long amount,
                              HttpSession session,
                              RedirectAttributes ra) {
    NguoiDung nd = (NguoiDung) session.getAttribute("nguoiDung");
    if (nd == null) return "redirect:/login";

    nd.setSoDuVi(nd.getSoDuVi() + amount);
    nguoiDungService.capNhatSoDu(nd.getId(), nd.getSoDuVi());
    session.setAttribute("nguoiDung", nd);

    ra.addFlashAttribute("thongBao", "Nạp thành công " + String.format("%,d", amount) + "đ!");
    return "redirect:/menu";
}
    

}