package org.artursbm.demo;

import org.artursbm.demo.infrastructure.RestHighLevelClientFactory;
import org.opensearch.client.RequestOptions;
import org.opensearch.client.indices.CreateIndexRequest;
import org.opensearch.client.indices.GetIndexRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class OpenSearchConsumer {


    public static void main(String[] args) throws IOException {

        final Logger logger = LoggerFactory.getLogger(OpenSearchConsumer.class.getSimpleName());

        // create an OpenSearch client and add index to it
        try (var openSearchClient = RestHighLevelClientFactory.createOpenSearchClient()) {
            if (!openSearchClient.indices().exists(new GetIndexRequest("wikimedia"), RequestOptions.DEFAULT)) {
                openSearchClient.indices().create(new CreateIndexRequest("wikimedia"), RequestOptions.DEFAULT);
                logger.info("index created successfully!");
            }

            logger.info("index already exists, continuing execution.");
        }

        // create a kafka client

        // main code logic

        // close things
    }

}
