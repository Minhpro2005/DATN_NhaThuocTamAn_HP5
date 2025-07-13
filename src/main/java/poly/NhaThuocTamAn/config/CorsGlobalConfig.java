package poly.NhaThuocTamAn.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//Đánh dấu lớp CorsGlobalConfig là một lớp cấu hình trong Spring và thực thi các bean được định nghĩa trong lớp này.
@Configuration
public class CorsGlobalConfig {
	
  //Tạo một bean để cấu hình CORS sử dụng WebMvcConfigurer, giúp tùy chỉnh các thiết lập web MVC của Spring Boot.
    @Bean
  //cho phép frontend (chạy ở một domain khác) gọi API từ backend.
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:5173")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*");
            }
        };
    }
}
