package com.common.service.client;


import com.common.util.RestApiUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AuthProviderClientImpl implements AuthProviderClient {
    @Value("api.auth.service.url")
    private String providerListApiUrl;
    @Override
    public List<AuthProviderDto> fetchAllProvidersByType(AuthProviderDto.ProviderType type) {
        ResponseEntity<List<AuthProviderDto>> responseEntity=
                RestApiUtil.get(providerListApiUrl,null,List.class);
        return List.of();
    }
}
