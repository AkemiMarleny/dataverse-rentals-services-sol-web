package com.dataverse.dataverserentals.common.helpers;

import jakarta.servlet.http.HttpServletRequest;

public final class ClientInfoUtil {

    private static final String X_FORWARDED_FOR = "X-Forwarded-For";
    private static final String USER_AGENT = "User-Agent";

    private ClientInfoUtil() {
    }

    public static String extractIp(HttpServletRequest request) {
        String xfwd = request.getHeader(X_FORWARDED_FOR);
        if (xfwd != null && !xfwd.isBlank()) {
            return xfwd.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }

    public static String extractUserAgent(HttpServletRequest request) {
        return request.getHeader(USER_AGENT);
    }
}
