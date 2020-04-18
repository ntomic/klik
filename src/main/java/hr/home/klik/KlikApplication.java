package hr.home.klik;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class KlikApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(KlikApplication.class, args);
	}
	
}
