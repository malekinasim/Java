package com.common.util;

import com.common.config.Context.RequestContext;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

public class RestApiUtil {


    public static  <T> ResponseEntity<T> get(String url, Map<String, String> headers, Class<T> responseType) {
        HttpHeaders httpHeaders = new HttpHeaders();
        if (headers != null) {
            headers.forEach(httpHeaders::set);
        }

        String token = RequestContext.getToken();
        if(token != null) {
            httpHeaders.setBearerAuth(token);
        }
        HttpEntity<Void> entity = new HttpEntity<>(httpHeaders);
        RestTemplate restTemplate=new RestTemplate();
        return restTemplate.exchange(url, HttpMethod.GET, entity, responseType);
    }

    public static  <T> ResponseEntity<T> post(String url, Object body, Map<String, String> headers, Class<T> responseType) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        if (headers != null) {
            headers.forEach(httpHeaders::set);
        }

        String token = RequestContext.getToken();
        if(token != null) {
            httpHeaders.setBearerAuth(token);
        }
        HttpEntity<Object> entity = new HttpEntity<>(body, httpHeaders);
        RestTemplate restTemplate=new RestTemplate();
        return restTemplate.exchange(url, HttpMethod.POST, entity, responseType);
    }

}
