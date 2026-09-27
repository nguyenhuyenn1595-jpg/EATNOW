package com.eatnow.eatnow.controller;

import com.eatnow.eatnow.entity.DanhGia;
import com.eatnow.eatnow.entity.MonAn;
import com.eatnow.eatnow.entity.NguoiDung;
import com.eatnow.eatnow.service.DanhGiaService;
import com.eatnow.eatnow.service.DanhMucService;
import com.eatnow.eatnow.service.MonAnService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpSession;
import java.util.Map;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class MenuController {

    @Autowired
    private MonAnService monAnService;

    @Autowired
    private DanhMucService danhMucService;

    @Autowired
    private DanhGiaService danhGiaService;

    // Trang chủ - hiển thị thực đơn
    @GetMapping({"/", "/menu"})
    public String trangMenu(@RequestParam(required = false) Long danhMuc,
                            @RequestParam(required = false) String keyword,
                             HttpSession session, 
                            Model model) {
        List<MonAn> dsMonAn;

         NguoiDung nd = (NguoiDung) session.getAttribute("nguoiDung");
    if (nd == null) return "redirect:/login";

        if (keyword != null && !keyword.isEmpty()) {
            dsMonAn = monAnService.timKiem(keyword);
        } else if (danhMuc != null) {
            dsMonAn = monAnService.layTheoDanhMuc(danhMuc);
        } else {
            dsMonAn = monAnService.layTatCa();
        }

        model.addAttribute("dsMonAn", dsMonAn);
        model.addAttribute("dsDanhMuc", danhMucService.layTatCa());
        model.addAttribute("danhMucDangChon", danhMuc);
        model.addAttribute("keyword", keyword);
        model.addAttribute("nguoiDung", nd); 
        // Lấy số lượng món trong giỏ hàng từ session
        Map<Long, Integer> gioHang = (Map<Long, Integer>) session.getAttribute("gioHang");
        int soLuongGio = (gioHang != null) ? gioHang.values().stream().mapToInt(Integer::intValue).sum() : 0;

        model.addAttribute("soLuongGio", soLuongGio);

        return "menu";
    }

    // Chi tiết món ăn
    @GetMapping("/mon-an/{id}")
public String chiTietMonAn(@PathVariable Long id, 
                           HttpSession session,
                           Model model) {
    // Kiểm tra đăng nhập
    NguoiDung nd = (NguoiDung) session.getAttribute("nguoiDung");
    if (nd == null) {
        return "redirect:/login";
    }

    MonAn mon = monAnService.layTheoId(id);
    List<DanhGia> dsDanhGia = danhGiaService.layTheoMonAn(id);

    model.addAttribute("mon", mon);
    model.addAttribute("dsDanhGia", dsDanhGia);
    model.addAttribute("nguoiDung", nd);       // ← THÊM DÒNG NÀY

    return "food-detail";
}

    // Gửi đánh giá món ăn
    @PostMapping("/mon-an/{id}/danh-gia")
    public String guiDanhGia(@PathVariable Long id,
                             @RequestParam Integer soSao,
                             @RequestParam String noiDung,
                             HttpSession session) {
        NguoiDung nd = (NguoiDung) session.getAttribute("nguoiDung");
        if (nd == null) {
            return "redirect:/login";
        }

        MonAn mon = monAnService.layTheoId(id);
        DanhGia dg = new DanhGia();
        dg.setNguoiDung(nd);
        dg.setMonAn(mon);
        dg.setSoSao(soSao);
        dg.setNoiDung(noiDung);
        danhGiaService.them(dg);

        return "redirect:/mon-an/" + id;
    }
}