package com.example.employee.task.tracker.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;

public class RestUtil {
    public static boolean isApiClient(HttpServletRequest request) {
        String accept = request.getHeader("Accept");
        String xRequestedWith = request.getHeader("X-Requested-With");
        return (accept != null && accept.contains(MediaType.APPLICATION_JSON_VALUE))
                || "XMLHttpRequest".equalsIgnoreCase(xRequestedWith)
                || "true".equalsIgnoreCase(request.getHeader("X-API-CLIENT"));
    }
    public static boolean isMobileClient(HttpServletRequest request) {
        // heuristic: if frontend requested a deep-link (mobile), we can check user-agent or a query parameter
        String ua = request.getHeader("User-Agent");
        String mobileParam = request.getParameter("mobile");
        if ("true".equalsIgnoreCase(mobileParam)) return true;
        if (ua == null) return false;
        ua = ua.toLowerCase(Locale.ROOT);
        return ua.contains("android") || ua.contains("iphone") || ua.contains("ipad") || ua.contains("mobile");
    }


    public static void sendJsonError(HttpServletResponse response, int status, String error, String message) {
        try {
            SecurityContextHolder.clearContext();
            response.resetBuffer();
            response.setStatus(status);
            response.setContentType("application/json;charset=UTF-8");
            response.setHeader("Cache-Control", "no-store");
            String json = String.format("{\"error\":\"%s\",\"message\":\"%s\"}", escapeJson(error), escapeJson(message));
            try (PrintWriter w = response.getWriter()) {
                w.write(json);
                w.flush();
            }
        } catch (IOException ignored) { }
    }

    private static String escapeJson(String input) {
        if (input == null) return "";
        return input.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
