package poly.NhaThuocTamAn.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "AnhThuoc")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnhThuoc {
	  @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Integer maAnhThuoc;

	    private String hinhAnh;

	    private Boolean anhChinh = false;

	    @ManyToOne
	    @JoinColumn(name = "maThuoc")
	    private Thuoc thuoc;
}

