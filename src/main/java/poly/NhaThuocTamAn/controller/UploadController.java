package poly.NhaThuocTamAn.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/upload")
@CrossOrigin("*")
public class UploadController {

    private final String rootDir = System.getProperty("user.dir") + "/uploads/";

    @PostMapping
    public Map<String, String> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam(defaultValue = "others") String type
    ) {
        try {
            String subFolder = switch (type) {
                case "khachhang" -> "khachhang/";
                case "nhanvien" -> "nhanvien/";
                case "thuoc" -> "thuoc/";
                case "bienthethuoc" -> "bienthethuoc/";
                default -> "others/";
            };

            File dir = new File(rootDir + subFolder);
            if (!dir.exists()) dir.mkdirs();

            String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            file.transferTo(new File(dir, filename));

            Map<String, String> res = new HashMap<>();
            
            // ✅ Sửa tại đây: trả về đường dẫn đúng
            res.put("filename", "uploads/" + subFolder + filename); // ✔ chuẩn để Vue load ảnh
            return res;
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi upload file", e);
        }
    }
}
