package org.artursbm.demo.infrastructure;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.opensearch.client.RestClient;
import org.opensearch.client.RestHighLevelClient;

import java.net.URI;

public class RestHighLevelClientFactory {

    private static RestHighLevelClient client;

    public static RestHighLevelClient createOpenSearchClient() {
        var connectionString = "http://localhost:9200";
        URI connUri = URI.create(connectionString);

        String userInfo = connUri.getUserInfo();

        if (userInfo == null) {
            // REST client with no security
            client = new RestHighLevelClient(RestClient.builder(new HttpHost(connUri.getHost(), connUri.getPort(),
                    connUri.getScheme())));
        } else {
            var auth = userInfo.split(":");
            var cp = new BasicCredentialsProvider();
            cp.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(auth[0], auth[1]));

            client = new RestHighLevelClient(
                    RestClient.builder(new HttpHost(connUri.getHost(), connUri.getPort(), connUri.getScheme()))
                            .setHttpClientConfigCallback(asyncCallback ->
                                    asyncCallback
                                            .setDefaultCredentialsProvider(cp)
                                            .setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy())));
        }

        return client;
    }
}
