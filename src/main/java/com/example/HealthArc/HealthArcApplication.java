package com.example.HealthArc;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class HealthArcApplication {
	public static void main(String[] args) {
		SpringApplication.run(HealthArcApplication.class, args);
	}

	@Value("${cors.allowedOrigins}")
	private String allowedOrigins;


	// cors config
	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**").allowedHeaders("*").exposedHeaders("*").allowedOrigins(allowedOrigins).allowCredentials(true);
			}
		};
	}

	public String[] getAllowedOrigins() {
		// You can potentially process the allowedOrigins string here (e.g., split it)
		return allowedOrigins.split(",");
	}
}