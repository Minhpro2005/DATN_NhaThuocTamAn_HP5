package poly.NhaThuocTamAn.controller;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.UUID;

import com.example.demo.dto.DanhGiaDTO;
import poly.NhaThuocTamAn.service.DanhGiaService;
import poly.NhaThuocTamAn.repository.DanhGiaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/danh-gia")
public class DanhGiaController {
	@Autowired
	private DanhGiaService dgsrv;
	
	@Autowired
	private DanhGiaRepository dgrep;
	
	//Get all
	@GetMapping
	public List<DanhGiaDTO> getAll(){
		return dgsrv.getAllDanhGia();
	}
	
	//Get by variation
	@GetMapping("/bien-the/{maBienThe}")
    public List<DanhGiaDTO> getByBienThe(@PathVariable Integer maBienThe) {
        return dgsrv.getDanhGiaByBienThe(maBienThe);
    }
	
	//Get by product
    @GetMapping("/thuoc/{maThuoc}")
    public List<DanhGiaDTO> getByThuoc(@PathVariable Integer maThuoc) {
        return dgsrv.getDanhGiaByThuoc(maThuoc);
    }
    
    //Post
    @PostMapping
    public DanhGiaDTO add(@RequestBody DanhGiaDTO dto) {
        return dgsrv.addDanhGia(dto);
    }
		
//	//Delete
//	@DeleteMapping("/{maDG}")
//	public void delete(@PathVariable Integer maDG) {
//		dgsrv.deleteById(maDG);
//	}
}
