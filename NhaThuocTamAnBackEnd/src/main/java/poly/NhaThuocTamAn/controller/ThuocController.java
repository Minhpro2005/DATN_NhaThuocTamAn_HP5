package poly.NhaThuocTamAn.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ThuocDTO;
import com.example.demo.dto.ThuocVaBienTheDTO;

import poly.NhaThuocTamAn.model.Thuoc;
import poly.NhaThuocTamAn.service.ThuocService;

@RestController
@RequestMapping("/api/thuoc")
@CrossOrigin
public class ThuocController {

	 @Autowired
	    private ThuocService thuocService;

	    @PostMapping
	    public ResponseEntity<Thuoc> create(@RequestBody ThuocDTO dto) {
	        return ResponseEntity.ok(thuocService.save(dto));
	    }

	    @PutMapping("/{id}")
	    public ResponseEntity<Thuoc> update(@PathVariable Integer id, @RequestBody ThuocDTO dto) {
	        return ResponseEntity.ok(thuocService.update(id, dto));
	    }

	    @DeleteMapping("/{id}")
	    public ResponseEntity<?> delete(@PathVariable Integer id) {
	        thuocService.delete(id);
	        return ResponseEntity.noContent().build();
	    }

	    @GetMapping("/search")
	    public ResponseEntity<List<ThuocDTO>> search(
	        @RequestParam(required = false) String keyword,
	        @RequestParam(required = false) Integer maDM,
	        @RequestParam(required = false) Boolean trangThai,
	        @RequestParam(required = false) Integer maNCC) {
	        return ResponseEntity.ok(thuocService.search(keyword, maDM, trangThai, maNCC));
	    }
	    
	    @GetMapping("/danh-muc/{maDM}")
	    public ResponseEntity<?> getThuocTheoDanhMuc(@PathVariable Integer maDM) {
	        List<ThuocVaBienTheDTO> list = thuocService.getThuocVaBienTheTheoDanhMuc(maDM);
	        return ResponseEntity.ok(list);
	    }
	    
	    @GetMapping("/{id}")
	    public ResponseEntity<ThuocDTO> getThuocById(@PathVariable Integer id) {
	        return ResponseEntity.ok(thuocService.findById(id));
	    }

}

