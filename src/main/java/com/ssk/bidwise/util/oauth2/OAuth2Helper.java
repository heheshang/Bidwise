package com.ssk.bidwise.util.oauth2;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * OAuth2 工具类
 * 主要用于从 HTTP Basic Auth 中提取 clientId 和 clientSecret
 *
 * @author Bidwise
 */
@Slf4j
public class OAuth2Helper {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BASIC_PREFIX = "Basic ";

    /**
     * 从 HTTP Basic Auth 中提取 clientId
     *
     * @param request HttpServletRequest
     * @return clientId，转换为 Long
     */
    public static Long getClientId(HttpServletRequest request) {
        String[] credentials = parseBasicAuth(request);
        if (credentials == null) {
            return null;
        }
        try {
            return Long.parseLong(credentials[0]);
        } catch (NumberFormatException e) {
            log.warn("clientId 格式错误: {}", credentials[0]);
            return null;
        }
    }

    /**
     * 从 HTTP Basic Auth 中提取 clientSecret
     *
     * @param request HttpServletRequest
     * @return clientSecret
     */
    public static String getClientSecret(HttpServletRequest request) {
        String[] credentials = parseBasicAuth(request);
        if (credentials == null) {
            return null;
        }
        return credentials[1];
    }

    /**
     * 解析 HTTP Basic Auth
     *
     * @param request HttpServletRequest
     * @return String[0] = username, String[1] = password，解析失败返回 null
     */
    private static String[] parseBasicAuth(HttpServletRequest request) {
        String authHeader = request.getHeader(AUTHORIZATION_HEADER);
        if (authHeader == null || !authHeader.startsWith(BASIC_PREFIX)) {
            return null;
        }

        try {
            String base64Credentials = authHeader.substring(BASIC_PREFIX.length());
            String credentials = new String(Base64.getDecoder().decode(base64Credentials), StandardCharsets.UTF_8);
            int colonPos = credentials.indexOf(':');
            if (colonPos == -1) {
                return null;
            }
            String username = credentials.substring(0, colonPos);
            String password = credentials.substring(colonPos + 1);
            return new String[]{username, password};
        } catch (IllegalArgumentException e) {
            log.warn("解析 Basic Auth 失败", e);
            return null;
        }
    }

    private OAuth2Helper() {
    }
}
