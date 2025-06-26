package poly.NhaThuocTamAn.impl;  

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import poly.NhaThuocTamAn.service.FileStorageService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    private final String uploadFolder = System.getProperty("user.dir") + "/uploads/";

    @Override
    public String saveFile(MultipartFile file, String type) {
        try {
            String subFolder = switch (type) {
                case "nhanvien" -> "nhanvien/";
                case "khachhang" -> "khachhang/";
                case "thuoc" -> "thuoc/";
                case "bienthethuoc" -> "bienthethuoc/";
                default -> "others/";
            };

            File dir = new File(uploadFolder + subFolder);
            if (!dir.exists()) dir.mkdirs();

            String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
            file.transferTo(new File(dir, filename));

            return "uploads/" + subFolder + filename; // Trả về path cho frontend
        } catch (IOException e) {
            throw new RuntimeException("Lỗi khi lưu file", e);
        }
    }
}

