package at.technikum.reslience;

import at.technikum.reslience.services.HttpService;
import at.technikum.reslience.services.ForecastService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ReslienceApplicationTests {

	@Mock
	private HttpService httpService;

	@InjectMocks
	private ForecastService weatherService;

	@Test
	void contextLoads() {
	}
}
