package at.technikum.resilience;

import at.technikum.resilience.services.ForecastService;
import at.technikum.resilience.services.HttpService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
@TestPropertySource("classpath:application-retry.properties")
class RetryTest {

    @MockBean
    private HttpService httpService;

    @Autowired
    private ForecastService forecastService;

    @Test
    void checkIfRetryIsTriggered_IfErrorIsThrown() {
        when(httpService.call(anyString(), any()))
                .thenThrow(new RuntimeException())
                .thenThrow(new RuntimeException())
                .thenReturn(null);

        assertDoesNotThrow(() -> forecastService.getWeatherForecastByCoordinates(0, 0).get());
        verify(httpService, times(3)).call(anyString(), any());
    }

    @Test
    void checkIfRetryIsNotTriggered_IfNoErrorIsThrown() {
        when(httpService.call(anyString(), any())).thenReturn(null);

        assertDoesNotThrow(() -> forecastService.getWeatherForecastByCoordinates(0, 0).get());
        verify(httpService, times(1)).call(anyString(), any());
    }
}
