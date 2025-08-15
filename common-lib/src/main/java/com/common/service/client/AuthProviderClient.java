package com.common.service.client;

import java.util.List;

public interface AuthProviderClient {
List<AuthProviderDto> fetchAllProvidersByType(AuthProviderDto.ProviderType type);
}
