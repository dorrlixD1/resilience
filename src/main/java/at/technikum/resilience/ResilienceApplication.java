package at.technikum.resilience;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class ResilienceApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder(ResilienceApplication.class)
				.headless(false)
				.run(args);
	}
}
