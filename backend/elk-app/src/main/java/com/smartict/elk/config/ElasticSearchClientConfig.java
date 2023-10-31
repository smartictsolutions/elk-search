/* SmartICT Bilisim A.S. (C) 2023 */
package com.smartict.elk.config;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.RestHighLevelClientBuilder;
import org.elasticsearch.client.sniff.SniffOnFailureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticSearchClientConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(ElasticSearchClientConfig.class);

    @Value("${elk.elasticSearch.username}")
    private String username;

    @Value("${elk.elasticSearch.password}")
    private String password;

    @Value("${elk.elasticSearch.host}")
    private String host;

    @Bean
    public RestHighLevelClient client() {
        long clientStartTime = new Date().getTime();
        LOGGER.info("ELK-SEARCH LOG -> RestHighLevelClient oluşturuluyor. ");
        final CredentialsProvider credentialsProvider = username == null ? null : new BasicCredentialsProvider();
        if (credentialsProvider != null) {
            credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));
        }

        SniffOnFailureListener sniffOnFailureListener = new SniffOnFailureListener();

        URL url;
        try {
            url = new URL(host);
        } catch (MalformedURLException e) {
            LOGGER.error("ELK-SEARCH LOG -> URL oluşturulamadı. ", e);
            throw new RuntimeException(e);
        }
        HttpHost httpHost = new HttpHost(url.getHost(), url.getPort(), url.getProtocol());

        RestClient httpClient = RestClient.builder(new HttpHost(httpHost.getHostName(), httpHost.getPort()))
            .setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
                @Override
                public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpClientBuilder) {
                    if (credentialsProvider != null) {
                        httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
                    }
                    return httpClientBuilder;
                }
            })
            .build();
        RestHighLevelClient esClient = new RestHighLevelClientBuilder(httpClient).setApiCompatibilityMode(true).build();

        // TODO: evrim.oguzhan clientı dinlemek ve bir hata oluşmadı durumunda bu hatayı takip etmek için sniffer configrasyonu eklenecektir.

        /* Sniffer sniffer = Sniffer.builder(restHighLevelClient.getLowLevelClient()) .setSniffAfterFailureDelayMillis(30000) .build();
         * sniffOnFailureListener.setSniffer(sniffer); */

        long clientEndTime = new Date().getTime();

        LOGGER.info("ELK-SEARCH LOG -> RestHighLevelClient hazır. Süre:" + (clientEndTime - clientStartTime));
        return esClient;
    }
}
