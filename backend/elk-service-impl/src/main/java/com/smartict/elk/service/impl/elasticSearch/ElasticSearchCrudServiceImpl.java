/* SmartICT Bilisim A.S. (C) 2023 */
package com.smartict.elk.service.impl.elasticSearch;

import java.io.IOException;
import java.util.*;

import com.smartict.elk.constant.Constants;
import com.smartict.elk.dto.elasticSearch.ElasticSearchDataDto;
import com.smartict.elk.service.elasticSearch.ElasticSearchCrudService;
import com.smartict.elk.service.elasticSearch.ElasticSearchService;
import com.smartict.elk.service.impl.util.ObjectMapperHelper;

import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.xcontent.XContentBuilder;
import org.elasticsearch.xcontent.XContentFactory;
import org.elasticsearch.xcontent.XContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ElasticSearchCrudServiceImpl implements ElasticSearchCrudService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ElasticSearchCrudServiceImpl.class);

    private final RestHighLevelClient client;
    private final ObjectMapperHelper objectMapperHelper;
    private final ElasticSearchService elasticSearchService;

    public ElasticSearchCrudServiceImpl(RestHighLevelClient client, ObjectMapperHelper objectMapperHelper, ElasticSearchService elasticSearchService) {
        this.client = client;
        this.objectMapperHelper = objectMapperHelper;
        this.elasticSearchService = elasticSearchService;
    }

    /**
     * Birden fazla verinin elastic searche aktarımını sağlayan metottur. Parametre olarak aktarımı yapılacak veri listesi istenmektedir.
     *
     * @param  dataList
     * @return
     */
    @Override
    public Boolean insertBulkData(List<ElasticSearchDataDto> dataList) {
        long bulkInsertStartTime = new Date().getTime();
        LOGGER.info("ELK-SEARCH LOG -> Bulk Insert yapılıyor");

        BulkRequest bulkRequest = new BulkRequest();

        for (ElasticSearchDataDto data : dataList) {
            ElasticSearchDataDto obj = this.objectMapperHelper.convertValue(data, ElasticSearchDataDto.class);
            bulkRequest.add(new IndexRequest(Constants.INDEX_NAME).source(this.objectMapperHelper.fromJsonObjectToString(obj), XContentType.JSON));
        }

        try {
            client.bulk(bulkRequest, RequestOptions.DEFAULT);
            long bulkInsertEndTime = new Date().getTime();
            LOGGER.info("ELK-SEARCH LOG -> Bulk Insert tamamlandı:" + (bulkInsertEndTime - bulkInsertStartTime));

            return true;
        } catch (IOException e) {
            LOGGER.error("ELK-SEARCH LOG -> Bulk Insert tamamlanamadı ", e.getLocalizedMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public Boolean insertData(ElasticSearchDataDto data) {
        long insertStartTime = new Date().getTime();
        LOGGER.info("ELK-SEARCH LOG -> Veri kaydediliyor");

        IndexRequest indexRequest = new IndexRequest(Constants.INDEX_NAME);

        try {
            indexRequest.source(this.objectMapperHelper.fromJsonObjectToString(data), XContentType.JSON);
            client.index(indexRequest, RequestOptions.DEFAULT);

            long insertEndTime = new Date().getTime();
            LOGGER.info("ELK-SEARCH LOG -> Veri kayıt tamamlandı:" + (insertEndTime - insertStartTime));

            return true;

        } catch (IOException e) {
            LOGGER.error("ELK-SEARCH LOG -> Veri kaydedilemedi ", e.getLocalizedMessage());
            throw new RuntimeException(e);
        } catch (Exception e) {
            LOGGER.error("ELK-SEARCH LOG -> Veri kaydedilemedi ", e.getLocalizedMessage());
            throw e;
        }
    }

    /**
     * Indexte tutulan documentteki verileri günceller. Güncellenmek istenen verilerin indecteki karşılık gelen unique idleri bulunur, bu idler ve yeni data ile
     * updateRequest oluşturulur.
     *
     * @param  dataList
     * @return
     */
    @Override
    public Boolean updateBulkData(List<ElasticSearchDataDto> dataList) {
        long bulkUpdateStartTime = new Date().getTime();
        LOGGER.info("ELK-SEARCH LOG -> Bulk Update yapılıyor");

        BulkRequest bulkRequest = new BulkRequest();

        // elastiğe aktarılan veriye ait idler ile elastikteki document idlerine erişimi sağlar.
        List<String> dataIdList = new ArrayList<>();
        dataList.forEach(elasticSearchDataDto -> dataIdList.add(String.valueOf(elasticSearchDataDto.getId())));
        SearchHit[] hits = elasticSearchService.searchBySourceDataId(dataIdList);

        dataList.forEach(data -> Arrays.stream(hits).forEach(hit -> {
            ElasticSearchDataDto elasticSearchData = this.objectMapperHelper.convertValue(hit.getSourceAsMap(), ElasticSearchDataDto.class);
            if (data.getId().equals(elasticSearchData.getId())) {
                Date createdOn = new Date(data.getRegistrationDate().getTime());
                try {
                    XContentBuilder contentBuilder = XContentFactory.jsonBuilder()
                        .startObject()
                        .field("id", String.valueOf(data.getId()))
                        .field("title", data.getTitle())
                        .field("type", data.getType())
                        .field("registrationDate", createdOn)
                        .field("userId", String.valueOf(data.getUserId()))
                        .field("customerId", String.valueOf(data.getCustomerId()))
                        .endObject();
                    bulkRequest.add(new UpdateRequest(Constants.INDEX_NAME, hit.getId()).doc(contentBuilder));

                } catch (IOException e) {
                    LOGGER.error("ELK-SEARCH LOG -> Bulk Update Request oluşturulurken hata oluştu ", e.getLocalizedMessage());
                    throw new RuntimeException(e);
                }
            }
        }));

        try {
            client.bulk(bulkRequest, RequestOptions.DEFAULT);
            long bulkUpdateEndTime = new Date().getTime();
            LOGGER.info("ELK-SEARCH LOG -> Bulk Update tamamlandı:" + (bulkUpdateEndTime - bulkUpdateStartTime));

            return true;
        } catch (IOException e) {
            LOGGER.error("ELK-SEARCH LOG -> Bulk Update tamamlanamadı ", e.getLocalizedMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public Boolean updateData(ElasticSearchDataDto data) {
        long updateStartTime = new Date().getTime();
        LOGGER.info("ELK-SEARCH LOG -> Veri güncelleniyor");

        // elastiğe aktarılan veriye ait idler ile elastikteki document idlerine erişimi sağlar.
        SearchHit[] hits = elasticSearchService.searchBySourceDataId(Collections.singletonList(String.valueOf(data.getId())));

        if (hits.length > 0) {
            UpdateRequest updateRequest = new UpdateRequest(Constants.INDEX_NAME, hits[0].getId());
            updateRequest.doc(this.objectMapperHelper.fromJsonObjectToString(data), XContentType.JSON);

            try {
                client.update(updateRequest, RequestOptions.DEFAULT);
            } catch (IOException e) {
                LOGGER.error("ELK-SEARCH LOG -> " + "Veri güncellenemedi ", e);
                throw new RuntimeException(e);
            }
        }

        long updateEndTime = new Date().getTime();
        LOGGER.info("ELK-SEARCH LOG -> Veri güncellendi Süre:" + (updateEndTime - updateStartTime));
        return true;
    }

    /**
     * Elastic search üzerinde kayıtlı olan verileri siler. Silinmek istenen verilerin document id bilgileri bulunur, bu document idler indexten silinir.
     *
     * @param  dataList
     * @return
     */
    @Override
    public Boolean deleteBulkData(List<ElasticSearchDataDto> dataList) {
        long bulkDeleteStartTime = new Date().getTime();
        LOGGER.info("ELK-SEARCH LOG -> Bulk Delete yapılıyor");

        BulkRequest bulkRequest = new BulkRequest();

        // elastiğe aktarılan veriye ait idler ile elastikteki document idlerine erişimi sağlar.
        List<String> dataIdList = new ArrayList<>();
        dataList.forEach(elasticSearchDataDto -> dataIdList.add(String.valueOf(elasticSearchDataDto.getId())));
        SearchHit[] hits = elasticSearchService.searchBySourceDataId(dataIdList);

        List<String> documentIdList = new ArrayList<>();
        Arrays.stream(hits).forEach(hit -> {
            documentIdList.add(hit.getId());
        });

        for (String documentId : documentIdList) {
            bulkRequest.add(new DeleteRequest(Constants.INDEX_NAME, documentId));
        }

        try {
            client.bulk(bulkRequest, RequestOptions.DEFAULT);
            long bulkDeleteEndTime = new Date().getTime();
            LOGGER.info("ELK-SEARCH LOG -> Veriler silindi:" + (bulkDeleteEndTime - bulkDeleteStartTime));

            return true;
        } catch (IOException e) {
            LOGGER.error("ELK-SEARCH LOG -> Veriler silinemedi ", e.getLocalizedMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public Boolean deleteData(ElasticSearchDataDto data) {
        long deleteStartTime = new Date().getTime();
        LOGGER.info("ELK-SEARCH LOG -> Veri siliniyor");

        // elastiğe aktarılan veriye ait idler ile elastikteki document idlerine erişimi sağlar.
        SearchHit[] hits = elasticSearchService.searchBySourceDataId(Collections.singletonList(String.valueOf(data.getId())));

        if (hits.length > 0) {
            DeleteRequest deleteRequest = new DeleteRequest(Constants.INDEX_NAME, hits[0].getId());

            try {
                client.delete(deleteRequest, RequestOptions.DEFAULT);
            } catch (IOException e) {
                LOGGER.error("ELK-SEARCH LOG -> " + "Veri silinemedi ", e);
                throw new RuntimeException(e);
            }
        }
        long deleteEndTime = new Date().getTime();
        LOGGER.info("ELK-SEARCH LOG -> Veri silindi. Süre:" + (deleteEndTime - deleteStartTime));
        return true;
    }

    /**
     * Elastic search üzerinde source alanında tutulan, yani aktarılmış olan ElasticSearchData verilerini döner. Elastic search üzerinde ilgili index içinde
     * arama işlemi yaparak tüm verileri alır ve SearchHit olarak döner.
     *
     * @return
     */

    @Override
    public List<ElasticSearchDataDto> getAllData() {
        long getDataStartTime = new Date().getTime();
        LOGGER.info("ELK-SEARCH LOG -> Veriler okunuyor");

        SearchRequest searchRequest = new SearchRequest(Constants.INDEX_NAME);
        SearchResponse response;
        try {
            response = client.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            LOGGER.error("ELK-SEARCH LOG -> Veriler okunamadı ", e.getLocalizedMessage());
            throw new RuntimeException(e);
        }

        SearchHit[] searchHits = response.getHits().getHits();
        List<ElasticSearchDataDto> elasticSearchDataList = new ArrayList<>();

        for (SearchHit searchHit : searchHits) {
            ElasticSearchDataDto obj = this.objectMapperHelper.convertValue(searchHit.getSourceAsMap(), ElasticSearchDataDto.class);
            elasticSearchDataList.add(obj);
        }

        long getDataEndTime = new Date().getTime();
        LOGGER.info("ELK-SEARCH LOG -> Veriler okundu:" + (getDataEndTime - getDataStartTime));

        return elasticSearchDataList;
    }

    /**
     * Index altındaki documentleri döner. documentCount kaç tane document bilgisinin istendiğini ifade eder. offset ise hangi indexten itibaren veri okunması
     * istendiğini belirtir. offset indexinden itibaren documentCount kadar olan documentleri döner.
     *
     * @param  documentCount
     * @param  offset
     * @return
     */
    @Override
    public SearchHit[] getAllDocuments(int documentCount, int offset) {
        long searchStartTime = new Date().getTime();
        LOGGER.info("ELK-SEARCH LOG -> Arama yapılıyor");

        SearchRequest searchRequest = new SearchRequest(Constants.INDEX_NAME);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.size(documentCount);
        searchSourceBuilder.from(offset);
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        searchRequest.source(searchSourceBuilder);
        SearchHit[] hits;
        try {
            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            hits = searchResponse.getHits().getHits();
            long searchEndTime = new Date().getTime();
            LOGGER.info("ELK-SEARCH LOG -> Arama tamamlandı. Süre:" + (searchEndTime - searchStartTime));

            return hits;
        } catch (IOException e) {
            LOGGER.info("ELK-SEARCH LOG -> Arama yapılırken hata oluştu ", e.getLocalizedMessage());
            throw new RuntimeException(e);
        }
    }

}
