/* SmartICT Bilisim A.S. (C) 2022 */
package com.smartict.elk.service.impl.util;

import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class RequestHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(RequestHelper.class);
    private static RestTemplate restTemplate;

    public RequestHelper() {
        this.restTemplate = new RestTemplate();
    }

    @SuppressWarnings("squid:S4507")
    public static <R> ResponseEntity<R> doRequest(
        String url,
        HttpMethod method,
        Object parameterObject,
        Class<R> responseClass,
        Map<String, String> headerValues,
        MediaType mediaType
    ) {
        try {
            HttpHeaders headers = new HttpHeaders();
            setHeaders(headers, headerValues);
            headers.setContentType(mediaType);

            HttpEntity<Object> httpEntity = new HttpEntity<>(parameterObject, headers);
            ResponseEntity<R> responseEntity = restTemplate.exchange(url, method, httpEntity, responseClass);
            return responseEntity;
        } catch (Exception e) {
            LOGGER.warn("Request işlemi yaplırken bir hata meydana geldi");
            LOGGER.warn("Hata sebebi: {}", e.getLocalizedMessage());
            e.printStackTrace();
            return null;
        }
    }

    private static void setHeaders(HttpHeaders headers, Map<String, String> headerValues) {
        if (Objects.nonNull(headerValues) && !headerValues.isEmpty()) {
            Iterator<Map.Entry<String, String>> itr = headerValues.entrySet().iterator();
            while (itr.hasNext()) {
                Map.Entry<String, String> headerIterationStep = itr.next();
                headers.add(headerIterationStep.getKey(), headerIterationStep.getValue());
            }
        }
    }
}
