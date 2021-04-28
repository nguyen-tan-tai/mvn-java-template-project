package caphe.net;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class Request {
    protected int timeout = 10000;
    protected String proxyHost;
    protected Integer proxyPort;

    public Response doGet(URI uri) {
        return this.doGet(uri, new ArrayList<>());
    }

    public Request withTimeout(int timeout) {
        this.timeout = timeout;
        return this;
    }

    public Response doGet(URI uri, List<Header> headers) {
        HttpGet httpGet = new HttpGet(uri.toString());
        for (Header header : headers) {
            httpGet.addHeader(header);
        }
        return this.doRequest(httpGet);
    }

    public Response doPost(URI uri, List<Header> headers) {
        return this.doPost(uri, null, headers);
    }

    public Response doPost(URI uri) {
        return this.doPost(uri, null, new ArrayList<>());
    }

    public Response doPost(URI uri, HttpEntity entity) {
        return this.doPost(uri, entity, new ArrayList<>());
    }

    public Response doPost(URI uri, HttpEntity entity, List<Header> headers) {
        HttpPost httpPost = new HttpPost(uri.toString());
        for (Header header : headers) {
            httpPost.addHeader(header);
        }
        if (entity != null) {
            httpPost.setEntity(entity);
        }
        return this.doRequest(httpPost);
    }

    public Response doRequest(HttpUriRequest httpUriRequest) {
        try (CloseableHttpClient httpClient = this.getHttpClient()) {
            try (CloseableHttpResponse httpResponse = httpClient.execute(httpUriRequest, this.getContext())) {
                return new Response(httpResponse.getStatusLine().getStatusCode(),
                        Arrays.asList(httpResponse.getAllHeaders()),
                        EntityUtils.toString(httpResponse.getEntity(), StandardCharsets.UTF_8));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public CloseableHttpClient getHttpClient() {
        HttpClientBuilder httpClientBuilder = HttpClients.custom();
        httpClientBuilder.setDefaultRequestConfig(RequestConfig.custom().setConnectTimeout(timeout)
                .setConnectionRequestTimeout(timeout)
                .setSocketTimeout(timeout).build());
        return httpClientBuilder.build();
    }

    public HttpClientContext getContext() {
        return HttpClientContext.create();
    }
}
