package com.eatnow.eatnow.config;

import com.eatnow.eatnow.entity.DanhMuc;
import com.eatnow.eatnow.entity.MonAn;
import com.eatnow.eatnow.entity.NguoiDung;
import com.eatnow.eatnow.repository.DanhMucRepository;
import com.eatnow.eatnow.repository.MonAnRepository;
import com.eatnow.eatnow.repository.NguoiDungRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private NguoiDungRepository nguoiDungRepo;

    @Autowired
    private DanhMucRepository danhMucRepo;

    @Autowired
    private MonAnRepository monAnRepo;

    @Override
    public void run(String... args) {
        if (nguoiDungRepo.count() > 0) {
            System.out.println("✅ Database đã có dữ liệu, bỏ qua khởi tạo.");
            return;
        }

        System.out.println("🌱 Đang khởi tạo dữ liệu mẫu...");

        
        // ============ TẠO NGƯỜI DÙNG MẪU ============
        // Sinh viên: email @st.edu.vn
        nguoiDungRepo.save(new NguoiDung("Nguyễn Văn Sinh", "sinhvien@st.edu.vn", "123456", "0901111111", "SinhVien"));

        // Giảng viên: email @edu.vn
        nguoiDungRepo.save(new NguoiDung("Trần Thị Viên", "giangvien@edu.vn", "123456", "0902222222", "GiangVien"));

        // Nhân viên căn tin
        nguoiDungRepo.save(new NguoiDung("Phạm Thị Nhân", "nhanvien@canteen.vn", "123456", "0903333333", "NhanVien"));

        // Admin
        nguoiDungRepo.save(new NguoiDung("Hoàng Văn Trị", "admin@eatnow.vn", "123456", "0904444444", "Admin"));

        // ============ TẠO DANH MỤC ============
        DanhMuc dm1 = danhMucRepo.save(new DanhMuc("Món mặn", "Cơm, mì, bún, phở"));
        DanhMuc dm2 = danhMucRepo.save(new DanhMuc("Món chay", "Các món chay thanh đạm"));
        DanhMuc dm3 = danhMucRepo.save(new DanhMuc("Đồ uống", "Nước ngọt, trà, cà phê"));
        DanhMuc dm4 = danhMucRepo.save(new DanhMuc("Ăn vặt", "Bánh snack, trái cây"));

        // ============ TẠO MÓN ĂN ============
        monAnRepo.save(new MonAn("Cơm gà chiên", 35000.0, "com-ga.jpg", "Cơm trắng với gà chiên giòn", dm1));
        monAnRepo.save(new MonAn("Cơm sườn nướng", 40000.0, "com-suon.jpg", "Cơm với sườn nướng mật ong", dm1));
        monAnRepo.save(new MonAn("Phở bò", 45000.0, "pho-bo.jpg", "Phở bò truyền thống", dm1));
        monAnRepo.save(new MonAn("Bún chả cá", 35000.0, "bun-cha-ca.jpg", "Bún với chả cá thơm ngon", dm1));
        monAnRepo.save(new MonAn("Cơm chay thập cẩm", 30000.0, "com-chay.jpg", "Cơm với rau củ xào", dm2));
        monAnRepo.save(new MonAn("Bún chay", 30000.0, "bun-chay.jpg", "Bún chay thanh đạm", dm2));
        monAnRepo.save(new MonAn("Trà sữa trân châu", 25000.0, "tra-sua.jpg", "Trà sữa thơm béo", dm3));
        monAnRepo.save(new MonAn("Cà phê sữa đá", 20000.0, "ca-phe.jpg", "Cà phê sữa đá đậm vị", dm3));
        monAnRepo.save(new MonAn("Nước cam tươi", 25000.0, "nuoc-cam.jpg", "Nước cam vắt nguyên chất", dm3));
        monAnRepo.save(new MonAn("Bánh mì chả", 20000.0, "banh-mi.jpg", "Bánh mì với chả và pate", dm4));
        monAnRepo.save(new MonAn("Bánh flan", 15000.0, "banh-flan.jpg", "Bánh flan mềm mịn", dm4));

        MonAn khoaiTay = new MonAn("Khoai tây chiên", 20000.0, "khoai-tay.jpg", "Khoai tây chiên giòn", dm4);
        khoaiTay.setConHang(false);
        monAnRepo.save(khoaiTay);

        System.out.println("✅ Đã khởi tạo dữ liệu mẫu thành công!");
        System.out.println("👤 Tài khoản demo:");
        System.out.println("   - Sinh viên: sinhvien@st.edu.vn / 123456");
        System.out.println("   - Giảng viên: giangvien@edu.vn / 123456");
        System.out.println("   - Nhân viên: nhanvien@canteen.vn / 123456");
        System.out.println("   - Admin: admin@eatnow.vn / 123456");
    }
}