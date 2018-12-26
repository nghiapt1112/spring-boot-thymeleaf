package com.lyna.commons.utils;

import com.lyna.commons.infrustructure.exception.DomainException;
import com.lyna.commons.infrustructure.exception.ResourceException;
import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpUtils.class);

    private final static int DEFAULT_TIMEOUT = 1; // millis
    public final static String APPLICATION_JSON_VALUE = "application/json";
    public final static String APPLICATION_JSON_UTF8_VALUE = APPLICATION_JSON_VALUE + ";charset=UTF-8";

    private static HttpClientBuilder httpClient = HttpClientBuilder.create();
    private static HttpClientBuilder httpsClient = HttpClientBuilder.create();
    private static RequestConfig TIMEOUT_CONFIG;
    private static final List<Header> DEFAULT_HEADERS = new ArrayList<>();

    static {
        TIMEOUT_CONFIG = RequestConfig.custom()
                .setConnectTimeout(DEFAULT_TIMEOUT) //the time to establish the connection with the remote host
                .setConnectionRequestTimeout(DEFAULT_TIMEOUT)
                .setSocketTimeout(DEFAULT_TIMEOUT)
                .build(); // the time waiting for data – after the connection was established

        DEFAULT_HEADERS.add(new BasicHeader(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_UTF8_VALUE));

        httpInit();
        httpsInit();
    }

    /**
     * Customize for HTTP
     */
    private static void httpInit() {
        httpClient
                .setDefaultRequestConfig(TIMEOUT_CONFIG)
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
                    .setDefaultRequestConfig(TIMEOUT_CONFIG)
                    .setDefaultHeaders(DEFAULT_HEADERS)
                    .setSSLContext(sslContext)
                    .setSSLHostnameVerifier(new NoopHostnameVerifier());

        } catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException e) {
            LOGGER.error("An error occur when initialize HTTPS configuration.");
            throw new ResourceException(DomainException.GENERAL_ERROR, "An error occur when initialize HTTPS configuration.", e);
        }
    }

    public static <T> T httpGet(String url, Class<T> typed) {
        return executeRequest(RequestBuilder.get().setUri(url).build(), httpClient.build(), typed);
    }

    public static <T> T httpsGet(String url, Class<T> typed) {
        return executeRequest(RequestBuilder.get().setUri(url).build(), httpsClient.build(), typed);
    }

    private static <T> T executeRequest(HttpUriRequest request, CloseableHttpClient httpClient, Class<T> typed) {
        try {
            CloseableHttpResponse response = httpClient.execute(request);
            if (response.getStatusLine().getStatusCode() != HttpStatus.OK.value()) {
                return null;
            }
            String jsonStr = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
            if (typed == null || typed.equals(String.class)) {
                return (T) jsonStr;
            }
            return JsonUtils.fromJson(jsonStr, typed);
        } catch (IOException e) {
            LOGGER.error("An error occur when request URL:\t {}", request.getURI());
            throw new ResourceException(DomainException.GENERAL_ERROR, "An error occur when request: " + request.getURI(), e);
        }
    }

    public static <T> T httpsPost(String url, Object requestObject, Class<T> typed) {
        String jsonRequest;
        if (requestObject instanceof String) {
            jsonRequest = (String) requestObject;
        } else {
            jsonRequest = JsonUtils.toJson(requestObject);
        }
        HttpUriRequest post = RequestBuilder
                .post()
                .setUri(url)
                .setEntity(new StringEntity(jsonRequest, StandardCharsets.UTF_8))
                .build();
        return executeRequest(post, httpsClient.build(), typed);
    }
}
