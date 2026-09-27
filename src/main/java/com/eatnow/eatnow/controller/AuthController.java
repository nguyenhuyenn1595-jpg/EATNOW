package com.eatnow.eatnow.controller;

import com.eatnow.eatnow.entity.NguoiDung;
import com.eatnow.eatnow.service.NguoiDungService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    @Autowired
    private NguoiDungService nguoiDungService;

    // Trang đăng nhập
    @GetMapping("/login")
    public String trangDangNhap() {
        return "login";
    }

    // Xử lý đăng nhập
    @PostMapping("/login")
    public String xuLyDangNhap(@RequestParam String email,
                                @RequestParam String matKhau,
                                HttpSession session,
                                Model model) {
        try {
            NguoiDung nd = nguoiDungService.dangNhap(email, matKhau);
            session.setAttribute("nguoiDung", nd);

            // Phân quyền điều hướng
            if ("Admin".equals(nd.getVaiTro())) {
                return "redirect:/admin/dashboard";
            } else if ("NhanVien".equals(nd.getVaiTro())) {
                return "redirect:/staff";
            } else {
                return "redirect:/menu";
            }
        } catch (RuntimeException e) {
            model.addAttribute("loi", e.getMessage());
            return "login";
        }
    }

    // Trang đăng ký
    @GetMapping("/register")
    public String trangDangKy() {
        return "register";
    }

    // Xử lý đăng ký
    @PostMapping("/register")
    public String xuLyDangKy(@RequestParam String hoTen,
                             @RequestParam String email,
                             @RequestParam String matKhau,
                             @RequestParam(required = false) String sdt,
                             Model model) {
        try {
            nguoiDungService.dangKy(hoTen, email, matKhau, sdt);
            model.addAttribute("thanhCong", "Đăng ký thành công! Vui lòng đăng nhập.");
            return "login";
        } catch (RuntimeException e) {
            model.addAttribute("loi", e.getMessage());
            return "register";
        }
    }

    // Đăng xuất
    @GetMapping("/logout")
    public String dangXuat(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}