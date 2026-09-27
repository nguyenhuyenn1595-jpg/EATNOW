package com.eatnow.eatnow.service;

import com.eatnow.eatnow.entity.NguoiDung;
import com.eatnow.eatnow.repository.NguoiDungRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NguoiDungService {

    @Autowired
    private NguoiDungRepository nguoiDungRepo;

    // Đăng ký tài khoản mới - phân biệt vai trò qua domain email
    public NguoiDung dangKy(String hoTen, String email, String matKhau, String sdt) {
        if (nguoiDungRepo.existsByEmail(email)) {
            throw new RuntimeException("Email đã được sử dụng!");
        }

        // Phân biệt vai trò qua domain email
        String vaiTro;
        if (email.endsWith("@st.edu.vn")) {
            vaiTro = "SinhVien";
        } else if (email.endsWith("@edu.vn")) {
            vaiTro = "GiangVien";
        } else {
            vaiTro = "Khach";  // Khách ngoài trường
        }

        NguoiDung nd = new NguoiDung(hoTen, email, matKhau, sdt, vaiTro);
        return nguoiDungRepo.save(nd);
    }

    // Đăng nhập
    public NguoiDung dangNhap(String email, String matKhau) {
        Optional<NguoiDung> opt = nguoiDungRepo.findByEmail(email);
        if (opt.isEmpty()) {
            throw new RuntimeException("Email không tồn tại!");
        }
        NguoiDung nd = opt.get();
        if (!nd.getMatKhau().equals(matKhau)) {
            throw new RuntimeException("Mật khẩu không đúng!");
        }
        return nd;
    }

    // Lấy tất cả người dùng
    public List<NguoiDung> layTatCa() {
        return nguoiDungRepo.findAll();
    }

    // Lấy theo id
    public NguoiDung layTheoId(Long id) {
        return nguoiDungRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng!"));
    }

    // Cập nhật số dư ví
    public void capNhatSoDu(Long idNguoiDung, Double soDuMoi) {
        NguoiDung nd = layTheoId(idNguoiDung);
        nd.setSoDuVi(soDuMoi);
        nguoiDungRepo.save(nd);
    }

    // Trừ tiền ví (kiểm tra đủ tiền mới trừ)
    public void truTienVi(Long idNguoiDung, Double soTien) {
        NguoiDung nd = layTheoId(idNguoiDung);
        if (nd.getSoDuVi() < soTien) {
            throw new RuntimeException("Số dư ví không đủ!");
        }
        nd.setSoDuVi(nd.getSoDuVi() - soTien);
        nguoiDungRepo.save(nd);
    }

    // Cộng tiền ví (hoàn tiền khi hủy đơn)
    public void congTienVi(Long idNguoiDung, Double soTien) {
        NguoiDung nd = layTheoId(idNguoiDung);
        nd.setSoDuVi(nd.getSoDuVi() + soTien);
        nguoiDungRepo.save(nd);
    }
}