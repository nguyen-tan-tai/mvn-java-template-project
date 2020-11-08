package caphe.net;

import java.net.InetSocketAddress;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;

public class ProxyRequest extends Request {

    public ProxyRequest(String proxyHost, Integer proxyPort) {
        this.proxyHost = proxyHost;
        this.proxyPort = proxyPort;
    }

    @Override
    public CloseableHttpClient getHttpClient() {
        HttpClientBuilder httpClientBuilder = HttpClients.custom();
        httpClientBuilder.setConnectionManager(SockPoolingHttpClientConnectionManager.create());
        httpClientBuilder.setDefaultRequestConfig(RequestConfig.custom().setConnectTimeout(timeout)
                .setConnectionRequestTimeout(timeout)
                .setSocketTimeout(timeout).build());
        return httpClientBuilder.build();
    }

    @Override
    public HttpClientContext getContext() {
        HttpClientContext context = HttpClientContext.create();
        context.setAttribute("socks.address",
                new InetSocketAddress(this.proxyHost, this.proxyPort));
        return context;
    }
}
