package at.technikum.resilience;

import at.technikum.resilience.services.ForecastService;
import at.technikum.resilience.services.HttpService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource("classpath:application-timelimiter.properties")
class TimeLimiterTest {

    @MockBean
    private HttpService httpService;

    @Autowired
    private ForecastService forecastService;

    @Test
    void test() {

    }
}
