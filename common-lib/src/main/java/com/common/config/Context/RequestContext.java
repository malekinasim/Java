package com.common.config.Context;


public class RequestContext {

    // ThreadLocal برای JWT فعلی
    private static final ThreadLocal<String> currentToken = new ThreadLocal<>();

    // ThreadLocal برای Tenant یا هر context دیگه
    private static final ThreadLocal<String> tenant = new ThreadLocal<>();

    // --- Setter و Getter ---
    public static void setToken(String token) {
        currentToken.set(token);
    }

    public static String getToken() {
        return currentToken.get();
    }

    public static void setTenant(String t) {
        tenant.set(t);
    }

    public static String getTenant() {
        return tenant.get();
    }

    // پاک کردن ThreadLocal بعد از پایان request
    public static void clear() {
        currentToken.remove();
        tenant.remove();
    }
}

