/* SmartICT Bilisim A.S. (C) 2023 */
package com.smartict.elk.service.impl.elasticSearch;

import java.io.IOException;
import java.util.*;

import com.smartict.elk.constant.Constants;
import com.smartict.elk.constant.exception.EnumExceptionMessages;
import com.smartict.elk.constant.exception.ServiceException;
import com.smartict.elk.dto.elasticSearch.DynamicSearchRequest;
import com.smartict.elk.dto.elasticSearch.ElasticSearchDataDto;
import com.smartict.elk.dto.elasticSearch.ElasticSearchDataPaginationDto;
import com.smartict.elk.service.elasticSearch.ElasticSearchService;
import com.smartict.elk.service.impl.util.ObjectMapperHelper;

import org.elasticsearch.ElasticsearchStatusException;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchScrollRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.core.TimeValue;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.xcontent.XContentBuilder;
import org.elasticsearch.xcontent.XContentFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ElasticSearchServiceImpl implements ElasticSearchService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ElasticSearchServiceImpl.class);

    private final RestHighLevelClient client;
    private final ObjectMapperHelper objectMapperHelper;

    public ElasticSearchServiceImpl(RestHighLevelClient client, ObjectMapperHelper objectMapperHelper) {
        this.client = client;
        this.objectMapperHelper = objectMapperHelper;
    }

    /**
     * Indexleme işlemini yapan metottur. Arama işleminde kulanılacak olan filtre kriterleri eklenip, index için bir analyzer denilen yapı oluşturuluyor.
     * Indexlenecek veri yapısı oluşturulup, mappings adı verilen yapı oluşturuluyor. Indexlenen verilerin hangi filtrelerle işleneceğini analysis_filters
     * listesine eklenen filtreler belirliyor. asciifolding : metinde yer alan farklı karakterlerin ascii karşılığını bulup dönüştürme işlemini yapar. lowercase
     * : metindeki tüm karakterleri küçük harfe dönüştürür. arama sırasında büyük/kjüçük harf duyarlılığını gidermek için kullanılır. ngram : metni parçalara
     * böler ve bu bölünen her parça ayrı ayrı indexlenir. Arama sırasında bu parçalara ayrılmış tokenlerle eşleşme sağlamaya çalışır. edge ngram : metindeki
     * kelimeleri başlangıç harflerini üzerinden parçalara böler. Otomatik tamamlama ve hızlı arama işlemleri için kullanılır. char filter : metinde belirli
     * karakterlerde değişiklik yapmak için kullanılır. Özel sembolleri değiştirmek, html taglarını kaldırmak gibi.
     *
     * @return
     */
    @Override
    public Boolean createIndex() {
        long createIndexStartTime = new Date().getTime();
        LOGGER.info("ELK-SEARCH LOG -> Index oluşturuluyor");

        CreateIndexRequest request = new CreateIndexRequest();
        request.index(Constants.INDEX_NAME);
        List<String> analysis_filters = new ArrayList<>();
        analysis_filters.add("asciifolding");
        analysis_filters.add("lowercase");
        analysis_filters.add("ngram");
        analysis_filters.add("customized_edge_ngram");

        List<String> char_filters = new ArrayList<>();
        char_filters.add("customized_char_filter");

        request.settings(
            Settings.builder()
                .put("index.number_of_shards", 1)
                .put("index.number_of_replicas", 1)
                // .put("max_ngram_diff", 8)
                .put("index.analysis.char_filter.customized_char_filter.type", "pattern_replace")
                .put("index.analysis.char_filter.customized_char_filter.pattern", "[^\\s\\p{L}\\p{N}]")
                .put("index.analysis.char_filter.customized_char_filter.replacement", "")
                .put("index.analysis.filter.customized_edge_ngram.type", "edge_ngram")
                .put("index.analysis.filter.customized_edge_ngram.min_gram", "2")
                .put("index.analysis.filter.customized_edge_ngram.max_gram", "15")
                .putList("index.analysis.analyzer.customized_analyzer.filter", analysis_filters)
                .putList("index.analysis.analyzer.customized_analyzer.char_filter", char_filters)
                .put("index.analysis.analyzer.customized_analyzer.tokenizer", "standard")
                .put("index.analysis.analyzer.customized_analyzer.type", "custom")
        );

        try {
            XContentBuilder builder = XContentFactory.jsonBuilder()
                .startObject()
                .startObject("properties")
                .startObject("id")
                .field("type", "keyword")
                .endObject()
                .startObject("type")
                .field("type", "text")
                .endObject()
                .startObject("title")
                .field("type", "text")
                .field("analyzer", "customized_analyzer")
                .field("search_analyzer", "customized_analyzer")
                .endObject()
                .startObject("registrationDate")
                .field("type", "date")
                .field("format", "strict_date_optional_time||epoch_millis")
                .endObject()
                .startObject("userId")
                .field("type", "keyword")
                .endObject()
                .startObject("customerId")
                .field("type", "keyword")
                .endObject()
                .endObject()
                .endObject();
            request.mapping("properties", builder);

            client.indices().create(request, RequestOptions.DEFAULT);

        } catch (IOException e) {
            LOGGER.error("ELK-SEARCH LOG -> Index oluşturulamadı ", e);

            throw new RuntimeException(e);
        }
        long createIndexEndTime = new Date().getTime();
        LOGGER.info("ELK-SEARCH LOG -> Index oluşturuldu Süre: " + (createIndexEndTime - createIndexStartTime));

        return true;
    }

    /**
     * Indexlenmiş veriler içerisinde, parametre olarak gönderilen aranan metin title alanında aranıyor. Arama işlemi için; arama yapılacak alan, arama metni ve
     * analiz için kullanılacak analyzer belirtiliyor. Elastic search üzerinden yapılan aramalarda default 10 sonuç dönmektedir. Daha fazla sonuç dönmesi için
     * (requestAllResults değişkeni true), dönmesi istenen sonuç sayısı(size) belirtilir. Belirtilen size değerinden daha fazla sonuç olması durumu için
     * pagination yapısı eklenmiştir. Arama querysine eklenen scroll() parametresi ile yapılan arama işlemine unique bir scroll_id oluşturulur.Kalan sonuç
     * verilerini almak için bu scroll id ile istek yapılmalıdır.
     *
     * @param  searchText
     * @param  requestAllResults
     * @return
     */
    @Override
    public ElasticSearchDataPaginationDto search(String searchText, Boolean requestAllResults) {
        long searchStartTime = new Date().getTime();
        LOGGER.info("ELK-SEARCH LOG -> Arama yapılıyor");
        try {
            SearchRequest searchRequest = new SearchRequest(Constants.INDEX_NAME);
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

            if (requestAllResults)
                searchSourceBuilder.size(Constants.PAGINATION_RESULT_COUNT); // her arama işleminde dönücelecek kayıt sayısıdır.

            searchSourceBuilder.query(QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("title", searchText).analyzer("customized_analyzer")));
            searchRequest.source(searchSourceBuilder);

            if (requestAllResults)
                searchRequest.scroll(TimeValue.timeValueHours(Constants.SCROLL_ID_EXPIRE_LONG)); // aramadan dönecek olan scroll idnin kullanım süresi 1 saat
                                                                                                 // olarak set eder.

            List<ElasticSearchDataDto> elasticSearchDataList = new ArrayList<>();
            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            SearchHit[] hits = searchResponse.getHits().getHits();
            for (SearchHit hit : hits) {
                ElasticSearchDataDto obj = this.objectMapperHelper.convertValue(hit.getSourceAsMap(), ElasticSearchDataDto.class);
                elasticSearchDataList.add(obj);
            }

            ElasticSearchDataPaginationDto elasticSearchDataPaginationDto = new ElasticSearchDataPaginationDto();
            elasticSearchDataPaginationDto.setScrollId(requestAllResults ? searchResponse.getScrollId() : null);
            elasticSearchDataPaginationDto.setElasticSearchDataDtoList(elasticSearchDataList);

            long searchEndTime = new Date().getTime();
            LOGGER.info("ELK-SEARCH LOG -> Arama tamamlandı. Süre:" + (searchEndTime - searchStartTime));

            return elasticSearchDataPaginationDto;
        } catch (IOException e) {
            LOGGER.info("ELK-SEARCH LOG -> Arama yapılırken hata oluştu ", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Yapılan aramada pagination ile sıradaki sonuçların alınmasını sağlar.
     *
     * @param  scrollId
     * @return
     */
    @Override
    public ElasticSearchDataPaginationDto searchNextPage(String scrollId) {
        long searchStartTime = new Date().getTime();
        LOGGER.info("ELK-SEARCH LOG -> Arama yapılıyor");
        try {
            SearchScrollRequest scrollRequest = new SearchScrollRequest(scrollId);
            scrollRequest.scroll(TimeValue.timeValueHours(Constants.SCROLL_ID_EXPIRE_LONG));
            SearchResponse searchScrollResponse = client.scroll(scrollRequest, RequestOptions.DEFAULT);
            SearchHit[] hits = searchScrollResponse.getHits().getHits();

            List<ElasticSearchDataDto> elasticSearchDataList = new ArrayList<>();

            for (SearchHit hit : hits) {
                ElasticSearchDataDto obj = this.objectMapperHelper.convertValue(hit.getSourceAsMap(), ElasticSearchDataDto.class);
                elasticSearchDataList.add(obj);
            }

            ElasticSearchDataPaginationDto elasticSearchDataPaginationDto = new ElasticSearchDataPaginationDto();
            elasticSearchDataPaginationDto.setScrollId(searchScrollResponse.getScrollId());
            elasticSearchDataPaginationDto.setElasticSearchDataDtoList(elasticSearchDataList);

            long searchEndTime = new Date().getTime();
            LOGGER.info("ELK-SEARCH LOG -> Arama tamamlandı. Süre:" + (searchEndTime - searchStartTime));

            return elasticSearchDataPaginationDto;
        } catch (ElasticsearchStatusException e) {
            LOGGER.info("ELK-SEARCH LOG -> Arama yapılırken hata oluştu ", e);

            if (e.getMessage().contains("search_context_missing_exception")) {
                throw new ServiceException(
                    EnumExceptionMessages.SEARCH_CONTEXT_MISSING_EXCEPTION.getLanguageKey(),
                    EnumExceptionMessages.SEARCH_CONTEXT_MISSING_EXCEPTION.getLanguageValue()
                );
            } else {
                throw e;
            }
        } catch (IOException e) {
            LOGGER.info("ELK-SEARCH LOG -> Arama yapılırken hata oluştu ", e);
            throw new RuntimeException(e);
        }

    }

    /**
     * Elastic search üzerinde String title alanı dışında sourceta tutulan diğer keyler üzerinden arama işlemini yapar. Elastic search objesini döner. Eşleşme
     * bulunan documente ait id bilgileri de dönmektedir.
     *
     * @param dynamicSearchRequestList@return
     */
    @Override
    public SearchHit[] searchDynamic(List<DynamicSearchRequest> dynamicSearchRequestList) {
        long searchStartTime = new Date().getTime();
        LOGGER.info("ELK-SEARCH LOG -> Arama yapılıyor");

        try {
            SearchRequest searchRequest = new SearchRequest(Constants.INDEX_NAME);
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

            Collection<String> values = new ArrayList<>();

            for (DynamicSearchRequest dynamicSearchRequest : dynamicSearchRequestList) {
                values.add(dynamicSearchRequest.getValue());
            }
            searchSourceBuilder.query(QueryBuilders.termsQuery(dynamicSearchRequestList.get(0).getKey(), values));
            searchRequest.source(searchSourceBuilder);

            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

            long searchEndTime = new Date().getTime();
            LOGGER.info("ELK-SEARCH LOG -> Arama tamamlandı. Süre:" + (searchEndTime - searchStartTime));

            return searchResponse.getHits().getHits();
        } catch (IOException e) {
            LOGGER.info("ELK-SEARCH LOG -> Arama yapılırken hata oluştu");
            throw new RuntimeException(e);
        }
    }

    /**
     * Indexlenmiş veriye ait id bilgisi ile arama işlemini yapar.
     *
     * @param  dataIdList
     * @return
     */

    @Override
    public SearchHit[] searchBySourceDataId(List<String> dataIdList) {
        long searchStartTime = new Date().getTime();
        LOGGER.info("ELK-SEARCH LOG -> Arama yapılıyor");
        try {
            SearchRequest searchRequest = new SearchRequest(Constants.INDEX_NAME);
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

            searchSourceBuilder.query(QueryBuilders.termsQuery("id", dataIdList));
            searchRequest.source(searchSourceBuilder);

            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            long searchEndTime = new Date().getTime();
            LOGGER.info("ELK-SEARCH LOG -> Arama tamamlandı. Süre:" + (searchEndTime - searchStartTime));

            return searchResponse.getHits().getHits();

        } catch (IOException e) {
            LOGGER.info("ELK-SEARCH LOG -> Arama yapılırken hata oluştu");
            throw new RuntimeException(e);
        }
    }

    /**
     * Oluşturulan indexi elastic search nesnesi olarak döner.
     *
     * @param  indexId
     * @param  indexName
     * @return
     */
    @Override
    public GetResponse getIndex(String indexName, String indexId) {
        long getIndexStartTime = new Date().getTime();
        LOGGER.info("ELK-SEARCH LOG -> Index okunuyor");

        GetRequest getRequest = new GetRequest(indexName, indexId);
        GetResponse getResponse;

        try {
            getResponse = client.get(getRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            LOGGER.error("ELK-SEARCH LOG -> Index okunamadı ", e);
            throw new RuntimeException(e);
        }
        long getIndexEndTime = new Date().getTime();
        LOGGER.info("ELK-SEARCH LOG -> Index okundu Süre: " + (getIndexEndTime - getIndexStartTime));

        return getResponse;
    }

    /**
     * Oluşturulmuş indexi siler.
     * 
     * @param  indexName
     * @return
     */
    @Override
    public Boolean deleteIndex(String indexName) {
        long deleteStartTime = new Date().getTime();
        LOGGER.info("ELK-SEARCH LOG -> Index siliniyor");

        DeleteIndexRequest request = new DeleteIndexRequest(indexName);
        try {
            client.indices().delete(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            LOGGER.error("ELK-SEARCH LOG -> Index silinemedi ", e);
            throw new RuntimeException(e);
        }

        long deleteEndTime = new Date().getTime();
        LOGGER.info("ELK-SEARCH LOG -> Index silindi Süre:" + (deleteEndTime - deleteStartTime));

        return true;
    }
}
