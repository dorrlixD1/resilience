package at.technikum.reslience;

import at.technikum.reslience.services.ForecastService;
import at.technikum.reslience.services.HttpService;
import at.technikum.reslience.services.WeatherService;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
public class CircuitBreakerTest {

    @MockBean
    private HttpService httpService;

    @Autowired
    private ForecastService forecastService;

    @Autowired
    private CircuitBreakerRegistry circuitBreakerRegistry;

    @BeforeEach
    public void setUp() {
        CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker("httpService");
        circuitBreaker.transitionToClosedState();
    }

    @Test
    public void checkIfCircuitBreakerChangesStateToOpen_AfterSixFailedCalls() {
        when(httpService.call(anyString(), any())).thenThrow(new RuntimeException());

        // Three calls because of Retry-mechanism
        assertThrows(RuntimeException.class, () -> forecastService.getWeatherForecastByCoordinates(0, 0));
        assertThrows(CallNotPermittedException.class, () -> forecastService.getWeatherForecastByCoordinates(0, 0));
    }

    @Test
    public void checkIfCircuitBreakerChangesStateToHalfOpen_AfterTenSeconds() {
        when(httpService.call(anyString(), any())).thenThrow(new RuntimeException());

        CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker("httpService");

        // Three calls because of Retry-mechanism
        assertThrows(RuntimeException.class, () -> forecastService.getWeatherForecastByCoordinates(0, 0));
        assertThrows(CallNotPermittedException.class, () -> forecastService.getWeatherForecastByCoordinates(0, 0));

        await().atMost(1, SECONDS).until(() -> circuitBreaker.getState().equals(CircuitBreaker.State.HALF_OPEN));
    }

    @Test
    public void checkIfCircuitBreakerChangesStateFromHalfOpenToClosed_AfterThreeSuccessfulCalls() {
        when(httpService.call(anyString(), any()))
                .thenThrow(new RuntimeException())
                .thenThrow(new RuntimeException())
                .thenThrow(new RuntimeException())
                .thenReturn(null);

        CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker("httpService");

        // Three calls because of Retry-mechanism
        assertThrows(RuntimeException.class, () -> forecastService.getWeatherForecastByCoordinates(0, 0));
        // Calls do not reach the service anymore - State OPEN
        assertThrows(CallNotPermittedException.class, () -> forecastService.getWeatherForecastByCoordinates(0, 0));

        await().atMost(1, SECONDS).until(() -> circuitBreaker.getState().equals(CircuitBreaker.State.HALF_OPEN));

        assertDoesNotThrow(() -> forecastService.getWeatherForecastByCoordinates(0, 0));
        assertEquals(CircuitBreaker.State.HALF_OPEN, circuitBreaker.getState());
        assertDoesNotThrow(() -> forecastService.getWeatherForecastByCoordinates(0, 0));
        // After third successfully call - circuit breaker closes
        assertEquals(CircuitBreaker.State.CLOSED, circuitBreaker.getState());
    }
}
