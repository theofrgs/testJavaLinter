package com.outside.api;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.exception.ExceptionUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.outside.Service;

@org.springframework.stereotype.Service
public class GoogleService extends Service {

    public GoogleService() {
        this.initLogger(GoogleService.class);
    }

    private static interface Bindings {
        public final static String CODE = "code";
        public final static String GRANT_TYPE = "grant_type";
        public final static String CLIENT_ID = "client_id";
        public final static String CLIENT_SECRET = "client_secret";
        public final static String REDIRECT_URI = "redirect_uri";
        public final static String AUTHORIZATION_CODE = "authorization_code";
        public final static String REFRESH_TOKEN = "refresh_token";
        public final static String AUTH_URL = "https://accounts.google.com/o/oauth2/token";
        public final static String X_WWW_ENCODED = "application/x-www-form-urlencoded";
        public final static String CONTENT_TYPE = "Content-Type";
        public final static String USER_INFO_URL = "https://www.googleapis.com/oauth2/v3/userinfo?&access_token=";
    }

    public HttpRequest.BodyPublisher encode(Map<String, String> body) {
        String form = body
                .entrySet()
                .stream()
                .map(entry -> String.join("=",
                        URLEncoder.encode(entry.getKey().toString(), StandardCharsets.UTF_8),
                        URLEncoder.encode(entry.getValue().toString(), StandardCharsets.UTF_8)))
                .collect(Collectors.joining("&"));
        return HttpRequest.BodyPublishers.ofString(form);
    }

    /**
     * Create body to be sent to do Google API call
     *
     * @param codeKey Bindings.CODE or Bindings.REFRESH_TOKEN
     * @param codeValue code or refresh_token
     * @param grantType
     * @return
     */
    public Map<String, String> createBody(String codeKey, String codeValue, String grantType) {
        return Map.of(codeKey, codeValue,
                Bindings.GRANT_TYPE, grantType,
                Bindings.CLIENT_ID, properties.getProperty("google.client.id"),
                Bindings.CLIENT_SECRET, properties.getProperty("google.client.secret"),
                Bindings.REDIRECT_URI, properties.getProperty("google.redirect.uri"));
    }

    public Map<String, Object> getRefreshToken(String authorizationCode) {
        HttpResponse<String> response = null;
        HttpClient client = HttpClient.newHttpClient();
        var body = createBody(Bindings.CODE, authorizationCode, Bindings.AUTHORIZATION_CODE);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(Bindings.AUTH_URL))
                .headers(Bindings.CONTENT_TYPE, Bindings.X_WWW_ENCODED)
                .POST(this.encode(body))
                .build();

        try {
            response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            this.LOGGER.error(ExceptionUtils.getMessage(e));
        }

        Map<String, Object> map = new Gson().fromJson(response.body(), new TypeToken<Map<String, Object>>() {
        }.getType());

        return map.get("error_description") != null ? Map.of("success", false, "errorMsg", map.get("error_description"))
                : Map.of("success", true, "data", map);
    }

    public Map<String, Object> getAccessToken(String refreshToken) {
        HttpResponse<String> response = null;
        HttpClient client = HttpClient.newHttpClient();
        var body = createBody(Bindings.REFRESH_TOKEN, refreshToken, Bindings.REFRESH_TOKEN);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(Bindings.AUTH_URL))
                .headers(Bindings.CONTENT_TYPE, Bindings.X_WWW_ENCODED)
                .POST(this.encode(body))
                .build();

        try {
            response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            this.LOGGER.error(ExceptionUtils.getMessage(e));
        }

        Map<String, Object> map = new Gson().fromJson(response.body(), new TypeToken<Map<String, Object>>() {
        }.getType());

        return map.get("error_description") != null ? Map.of("success", false, "errorMsg", map.get("error_description"))
                : Map.of("success", true, "access_token", map.get("access_token"));
    }

    public Map<String, Object> getProfilInfo(String accessToken) {
        String url = Bindings.USER_INFO_URL + accessToken;
        HttpResponse<String> response = null;
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .headers(Bindings.CONTENT_TYPE, Bindings.X_WWW_ENCODED)
                .GET()
                .build();

        try {
            response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            this.LOGGER.error(ExceptionUtils.getMessage(e));
        }

        Map<String, Object> map = new Gson().fromJson(response.body(), new TypeToken<Map<String, Object>>() {
        }.getType());

        return map.get("error") != null ? Map.of("success", false, "errorMsg", map.get("error_description"))
                : Map.of("success", true, "profile", map);
    }
}