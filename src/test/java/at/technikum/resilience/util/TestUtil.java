package at.technikum.resilience.util;

import lombok.val;
import org.junit.jupiter.api.function.Executable;

import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestUtil {

    public static <T> void assertCompletableFutureException(Class<T> exceptionClass, Executable executable) {
        val exception = assertThrows(ExecutionException.class, executable);
        assertInstanceOf(exceptionClass, exception.getCause());
    }
}
