package at.technikum.reslience;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class ReslienceApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder(ReslienceApplication.class)
				.headless(false)
				.run(args);
	}
}
