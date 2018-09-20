package angelina.quartz.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Angelina
 */
@SpringBootApplication(scanBasePackages = {"angelina.quartz.service"})
public class AngelinaQuartzServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AngelinaQuartzServiceApplication.class, args);
	}
}
