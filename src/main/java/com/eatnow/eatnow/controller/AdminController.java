package com.eatnow.eatnow.controller;

import com.eatnow.eatnow.entity.DanhMuc;
import com.eatnow.eatnow.entity.DonHang;
import com.eatnow.eatnow.entity.MonAn;
import com.eatnow.eatnow.entity.NguoiDung;
import com.eatnow.eatnow.service.DanhMucService;
import com.eatnow.eatnow.service.DonHangService;
import com.eatnow.eatnow.service.MonAnService;
import com.eatnow.eatnow.service.NguoiDungService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private MonAnService monAnService;

    @Autowired
    private DanhMucService danhMucService;

    @Autowired
    private DonHangService donHangService;

    @Autowired
    private NguoiDungService nguoiDungService;

    // Kiểm tra quyền Admin
    private boolean laAdmin(HttpSession session) {
        NguoiDung nd = (NguoiDung) session.getAttribute("nguoiDung");
        return nd != null && "Admin".equals(nd.getVaiTro());
    }

    // Dashboard
    @GetMapping("/dashboard")
public String dashboard(@RequestParam(defaultValue = "1") int page,
                        HttpSession session,
                        Model model) {
    if (!laAdmin(session)) return "redirect:/login";

    NguoiDung nd = (NguoiDung) session.getAttribute("nguoiDung");

    List<DonHang> dsDonHangAll = donHangService.layTatCa();
    List<MonAn> dsMonAn = monAnService.layTatCa();
    List<NguoiDung> dsNguoiDung = nguoiDungService.layTatCa();

    double tongDoanhThu = dsDonHangAll.stream()
            .filter(d -> !"ChoThanhToan".equals(d.getTrangThai()) && !"DaHuy".equals(d.getTrangThai()))
            .mapToDouble(DonHang::getTongTien)
            .sum();

    // Phân trang
    int pageSize = 10;
    int totalPages = (int) Math.ceil((double) dsDonHangAll.size() / pageSize);
    int start = (page - 1) * pageSize;
    int end = Math.min(start + pageSize, dsDonHangAll.size());

    List<DonHang> dsDonHangPage = dsDonHangAll.subList(start, end);

    model.addAttribute("nguoiDung", nd);
    model.addAttribute("tongDonHang", dsDonHangAll.size());
    model.addAttribute("tongMonAn", dsMonAn.size());
    model.addAttribute("tongNguoiDung", dsNguoiDung.size());
    model.addAttribute("tongDoanhThu", tongDoanhThu);
    model.addAttribute("dsDonHang", dsDonHangPage);
    model.addAttribute("currentPage", page);
    model.addAttribute("totalPages", totalPages);
    return "admin/dashboard";
}

    // ================== QUẢN LÝ MÓN ĂN ==================

    @GetMapping("/mon-an")
public String quanLyMonAn(@RequestParam(defaultValue = "1") int page,
                          HttpSession session,
                          Model model) {
    if (!laAdmin(session)) return "redirect:/login";

    NguoiDung nd = (NguoiDung) session.getAttribute("nguoiDung");
    List<MonAn> all = monAnService.layTatCa();

    // Phân trang 10 món/trang
    int pageSize = 10;
    int totalPages = (int) Math.ceil((double) all.size() / pageSize);
    int start = (page - 1) * pageSize;
    int end = Math.min(start + pageSize, all.size());
    List<MonAn> dsMonAnPage = all.subList(start, end);

    model.addAttribute("nguoiDung", nd);
    model.addAttribute("dsMonAn", dsMonAnPage);
    model.addAttribute("dsDanhMuc", danhMucService.layTatCa());
    model.addAttribute("currentPage", page);
    model.addAttribute("totalPages", totalPages);
    return "admin/manage-food";
}

    // Form thêm món mới
    @GetMapping("/mon-an/them")
    public String formThemMon(HttpSession session, Model model) {
        if (!laAdmin(session)) return "redirect:/login";

        NguoiDung nd = (NguoiDung) session.getAttribute("nguoiDung");  
    model.addAttribute("nguoiDung", nd);   

        model.addAttribute("monAn", new MonAn());
        model.addAttribute("dsDanhMuc", danhMucService.layTatCa());
        model.addAttribute("cheDo", "them");
        return "admin/food-form";
    }

    // Xử lý thêm món
    @PostMapping("/mon-an/them")
    public String xuLyThemMon(@RequestParam String tenMon,
                              @RequestParam Double gia,
                              @RequestParam(required = false) String hinhAnh,
                              @RequestParam(required = false) String moTa,
                              @RequestParam Long idDanhMuc,
                              @RequestParam(defaultValue = "false") Boolean conHang,
                              HttpSession session) {
        if (!laAdmin(session)) return "redirect:/login";

        DanhMuc dm = danhMucService.layTheoId(idDanhMuc);
        MonAn mon = new MonAn(tenMon, gia, hinhAnh, moTa, dm);
        mon.setConHang(conHang);
        monAnService.themHoacCapNhat(mon);

        return "redirect:/admin/mon-an";
    }

    // Form sửa món
    @GetMapping("/mon-an/sua/{id}")
    public String formSuaMon(@PathVariable Long id, HttpSession session, Model model) {
        if (!laAdmin(session)) return "redirect:/login";
        NguoiDung nd = (NguoiDung) session.getAttribute("nguoiDung");   
    model.addAttribute("nguoiDung", nd); 

        model.addAttribute("monAn", monAnService.layTheoId(id));
        model.addAttribute("dsDanhMuc", danhMucService.layTatCa());
        model.addAttribute("cheDo", "sua");
        return "admin/food-form";
    }

    // Xử lý sửa món
    @PostMapping("/mon-an/sua/{id}")
    public String xuLySuaMon(@PathVariable Long id,
                             @RequestParam String tenMon,
                             @RequestParam Double gia,
                             @RequestParam(required = false) String hinhAnh,
                             @RequestParam(required = false) String moTa,
                             @RequestParam Long idDanhMuc,
                             @RequestParam(defaultValue = "false") Boolean conHang,
                             HttpSession session) {
        if (!laAdmin(session)) return "redirect:/login";

        MonAn mon = monAnService.layTheoId(id);
        mon.setTenMon(tenMon);
        mon.setGia(gia);
        mon.setHinhAnh(hinhAnh);
        mon.setMoTa(moTa);
        mon.setDanhMuc(danhMucService.layTheoId(idDanhMuc));
        mon.setConHang(conHang);
        monAnService.themHoacCapNhat(mon);

        return "redirect:/admin/mon-an";
    }

    // Xóa món
    @PostMapping("/mon-an/xoa/{id}")
    public String xoaMon(@PathVariable Long id, HttpSession session) {
        if (!laAdmin(session)) return "redirect:/login";

        monAnService.xoa(id);
        return "redirect:/admin/mon-an";
    }

    // ================== QUẢN LÝ DANH MỤC ==================

    @GetMapping("/danh-muc")
public String quanLyDanhMuc(HttpSession session, Model model) {
    if (!laAdmin(session)) return "redirect:/login";
    NguoiDung nd = (NguoiDung) session.getAttribute("nguoiDung");
    model.addAttribute("nguoiDung", nd);
    model.addAttribute("dsDanhMuc", danhMucService.layTatCa());
    return "admin/manage-category";
}

@PostMapping("/danh-muc/them")
public String themDanhMuc(@RequestParam String tenDanhMuc,
                          @RequestParam(required = false) String moTa,
                          HttpSession session) {
    if (!laAdmin(session)) return "redirect:/login";
    danhMucService.them(new DanhMuc(tenDanhMuc, moTa));
    return "redirect:/admin/danh-muc";
}

@PostMapping("/danh-muc/xoa/{id}")
public String xoaDanhMuc(@PathVariable Long id, HttpSession session) {
    if (!laAdmin(session)) return "redirect:/login";
    danhMucService.xoa(id);
    return "redirect:/admin/danh-muc";
}
}