package at.technikum.reslience;

import at.technikum.reslience.util.WeatherFrame;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import javax.swing.*;

@SpringBootApplication
public class ReslienceApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder(ReslienceApplication.class)
				.headless(false)
				.run(args);
	}

	@PostConstruct
	public void loadFrame() throws Exception {
		new WeatherFrame();
	}
}
