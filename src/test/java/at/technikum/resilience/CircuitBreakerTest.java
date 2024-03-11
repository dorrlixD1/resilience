package at.technikum.resilience;

import at.technikum.resilience.services.ForecastService;
import at.technikum.resilience.services.HttpService;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.concurrent.ExecutionException;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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

    private CircuitBreaker circuitBreaker;

    @BeforeEach
    public void setUp() {
        circuitBreaker = circuitBreakerRegistry.circuitBreaker("httpService");
        circuitBreaker.transitionToClosedState();
    }

    @Test
    public void checkIfCircuitBreakerChangesStateToOpen_AfterSixFailedCalls() throws Exception {
        when(httpService.call(anyString(), any())).thenThrow(new RuntimeException());

        // Three calls because of Retry-mechanism
        assertCompletableFutureException(RuntimeException.class, () -> forecastService.getWeatherForecastByCoordinates(0, 0).get());
        assertCompletableFutureException(CallNotPermittedException.class, () -> forecastService.getWeatherForecastByCoordinates(0, 0).get());

        assertEquals(CircuitBreaker.State.OPEN, circuitBreaker.getState());
    }

    @Test
    public void checkIfCircuitBreakerChangesStateToHalfOpen_AfterTenSeconds() {
        when(httpService.call(anyString(), any())).thenThrow(new RuntimeException());

        // Three calls because of Retry-mechanism
        assertCompletableFutureException(RuntimeException.class, () -> forecastService.getWeatherForecastByCoordinates(0, 0).get());
        assertCompletableFutureException(CallNotPermittedException.class, () -> forecastService.getWeatherForecastByCoordinates(0, 0).get());

        await().atMost(1, SECONDS).until(() -> circuitBreaker.getState().equals(CircuitBreaker.State.HALF_OPEN));
    }

    @Test
    public void checkIfCircuitBreakerChangesStateFromHalfOpenToClosed_AfterThreeSuccessfulCalls() {
        when(httpService.call(anyString(), any()))
                .thenThrow(new RuntimeException())
                .thenThrow(new RuntimeException())
                .thenThrow(new RuntimeException())
                .thenReturn(null);

        // Three calls because of Retry-mechanism
        assertCompletableFutureException(RuntimeException.class, () -> forecastService.getWeatherForecastByCoordinates(0, 0).get());
        // Calls do not reach the service anymore - State OPEN
        assertCompletableFutureException(CallNotPermittedException.class, () -> forecastService.getWeatherForecastByCoordinates(0, 0).get());

        await().atMost(1, SECONDS).until(() -> circuitBreaker.getState().equals(CircuitBreaker.State.HALF_OPEN));

        assertDoesNotThrow(() -> forecastService.getWeatherForecastByCoordinates(0, 0).get());
        assertEquals(CircuitBreaker.State.HALF_OPEN, circuitBreaker.getState());
        assertDoesNotThrow(() -> forecastService.getWeatherForecastByCoordinates(0, 0).get());
        // After third successfully call - circuit breaker closes
        assertEquals(CircuitBreaker.State.CLOSED, circuitBreaker.getState());
    }

    private <T>void assertCompletableFutureException(Class<T>exceptionClass, Executable executable){
        val exception = assertThrows(ExecutionException.class, executable);
        assertInstanceOf(exceptionClass, exception.getCause());
    }
}
