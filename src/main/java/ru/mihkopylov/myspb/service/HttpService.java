package ru.mihkopylov.myspb.service;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.mihkopylov.myspb.exception.BaseServiceException;
import ru.mihkopylov.myspb.interceptor.SessionContext;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.function.Supplier;

@Service
@AllArgsConstructor
@Slf4j
public class HttpService {
    @NonNull
    private final SessionContext sessionContext;

    @NonNull
    public <T> T get(@NonNull String url, @NonNull Class<T> responseClass) {
        return get(url, ParameterizedTypeReference.forType(responseClass), Map.of());
    }

    @NonNull
    public <T> T get(@NonNull String url, @NonNull Class<T> responseClass,
                     @NonNull Map<String, Object> queryParameters) {
        return get(url, ParameterizedTypeReference.forType(responseClass), queryParameters);
    }

    @NonNull
    public <T> T get(@NonNull String url, @NonNull ParameterizedTypeReference<T> responseType,
                     @NonNull Map<String, Object> queryParameters) {
        RestTemplate restTemplate = new RestTemplate();
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url);
        queryParameters.forEach(uriBuilder::queryParam);
        URI uri = uriBuilder.build().toUri();
        RequestEntity<String> requestEntity = new RequestEntity<>(createAuthHeaders(), HttpMethod.GET, uri);
        return wrapLoggingClientException(() -> restTemplate.exchange(requestEntity, responseType).getBody());
    }

    @NonNull
    public <T> T post(@NonNull String url, @NonNull MultiValueMap<String, Object> multipartFormBody,
                      @NonNull Class<T> responseClass) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = createAuthHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(multipartFormBody, headers);
        return wrapLoggingClientException(
                () -> restTemplate.postForEntity(URI.create(url), httpEntity, responseClass).getBody());
    }

    @NonNull
    private HttpHeaders createAuthHeaders() {
        HttpHeaders headers = new HttpHeaders();
        sessionContext.getToken().ifPresent(value -> headers.setBearerAuth(value.getAccessToken()));
        return headers;
    }

    @NonNull
    private <T> T wrapLoggingClientException(@NonNull Supplier<T> supplier) {
        try {
            return supplier.get();
        } catch (HttpClientErrorException.Unauthorized e) {
            throw e;
        } catch (HttpStatusCodeException e) {
            //can't use getResponseBodyAsString because want to use UTF_8 charset while the former uses DEFAULT_CHARSET = StandardCharsets.ISO_8859_1
            String message = new String(e.getResponseBodyAsByteArray(), StandardCharsets.UTF_8);
            throw new BaseServiceException(e.getStatusCode(), message);
        }
    }
}
