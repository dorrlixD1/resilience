package at.technikum.resilience.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class HttpService {

    public <T> T call(String url, Class<T> clazz) {
        log.info("Fetching data for '{}' and return type {}", url, clazz.getSimpleName());
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<T> result = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(null), clazz);
        return result.getBody();
    }
}
