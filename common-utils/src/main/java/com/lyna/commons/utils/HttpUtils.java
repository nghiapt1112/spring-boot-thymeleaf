package com.lyna.commons.utils;

import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.http.HttpStatus;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public final class HttpUtils {
    private final static int DEFAULT_TIMEOUT = 15; //seconds
    public final static String APPLICATION_JSON_VALUE = "application/json";
    public final static String APPLICATION_JSON_UTF8_VALUE = APPLICATION_JSON_VALUE + ";charset=UTF-8";

    private static HttpClientBuilder httpClient = HttpClientBuilder.create();
    private static HttpClientBuilder httpsClient = HttpClientBuilder.create();
    private static RequestConfig.Builder TIMEOUT_CONFIG_BUILDER = RequestConfig.custom();
    private static final List<Header> DEFAULT_HEADERS = new ArrayList<>();

    static {
        // That is the recommended way of configuring all three timeouts in a type-safe and readable manner.
        TIMEOUT_CONFIG_BUILDER
                .setConnectTimeout(DEFAULT_TIMEOUT) //the time to establish the connection with the remote host
                .setConnectionRequestTimeout(DEFAULT_TIMEOUT)
                .setSocketTimeout(DEFAULT_TIMEOUT); // the time waiting for data – after the connection was established

        DEFAULT_HEADERS.add(new BasicHeader(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_UTF8_VALUE));

        httpInit();
        httpsInit();
    }

    /**
     * Customize for HTTP
     */
    private static void httpInit() {
        httpClient
                .setDefaultRequestConfig(TIMEOUT_CONFIG_BUILDER.build())
                .setDefaultHeaders(DEFAULT_HEADERS);
    }

    /**
     * Customize for HTTPS
     */
    private static void httpsInit() {
        try {
            SSLContext sslContext = new SSLContextBuilder()
                    .loadTrustMaterial(null, (certificate, authType) -> true)
                    .build();

            httpsClient
                    .setDefaultRequestConfig(TIMEOUT_CONFIG_BUILDER.build())
                    .setDefaultHeaders(DEFAULT_HEADERS)
                    .setSSLContext(sslContext)
                    .setSSLHostnameVerifier(new NoopHostnameVerifier());

        } catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException e) {
            e.printStackTrace();
        }
    }

    public static <T> T httpGet(String url, Class<T> typed) {
        return exeGet(url, httpClient.build(), typed);
    }

    public static <T> T httpsGet(String url, Class<T> typed) {
        return exeGet(url, httpsClient.build(), typed);
    }

    public static <T> T exeGet(String url, CloseableHttpClient httpClient, Class<T> typed) {
        try {
            CloseableHttpResponse response = httpClient.execute(RequestBuilder.get().setUri(url).build());
            if (response.getStatusLine().getStatusCode() != HttpStatus.OK.value()) {
                return null;
            }
            String jsonStr = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
            if (typed == null || typed.equals(String.class)) {
                return (T) jsonStr;
            }
            return JsonUtils.fromJson(jsonStr, typed);
        } catch (IOException e) {
            e.printStackTrace(); // TODO: throw new ResourceException
        }
        return null;
    }

}
