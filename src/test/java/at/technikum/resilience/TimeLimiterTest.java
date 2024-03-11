package at.technikum.resilience;

import at.technikum.resilience.services.ForecastService;
import at.technikum.resilience.services.HttpService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static at.technikum.resilience.util.TestUtil.assertCompletableFutureException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestPropertySource("classpath:application-timelimiter.properties")
class TimeLimiterTest {

    @MockBean
    private HttpService httpService;

    @Autowired
    private ForecastService forecastService;

    @Test
    void checkIfTimeLimiterDoesThrow_IfNotReturnsInTime() {
        when(httpService.call(anyString(), any())).thenAnswer((invocationOnMock) -> {
            TimeUnit.MILLISECONDS.sleep(3000);
            return null;
        });
        assertCompletableFutureException(TimeoutException.class, () -> forecastService.getWeatherForecastByCoordinates(0, 0).get());
    }

    @Test
    void checkIfTimeLimiterDoesNotThrow_IfReturnsInTime() {
        when(httpService.call(anyString(), any())).thenAnswer((invocationOnMock) -> {
            TimeUnit.MILLISECONDS.sleep(200);
            return null;
        });
        assertDoesNotThrow(() -> forecastService.getWeatherForecastByCoordinates(0, 0).get());
    }
}
