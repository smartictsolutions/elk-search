/* SmartICT Bilisim A.S. (C) 2023 */
package com.smartict.elk.service.elasticSearch;

import java.util.List;

import com.smartict.elk.dto.elasticSearch.DynamicSearchRequest;
import com.smartict.elk.dto.elasticSearch.ElasticSearchDataPaginationDto;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.search.SearchHit;

public interface ElasticSearchService {
    GetResponse getIndex(String indexName, String indexId);

    Boolean deleteIndex(String indexName);

    Boolean createIndex();

    SearchHit[] searchDynamic(List<DynamicSearchRequest> dynamicSearchRequestList);

    SearchHit[] searchBySourceDataId(List<String> dataIdList);

    ElasticSearchDataPaginationDto search(String searchText, Boolean requestAllResults);

    ElasticSearchDataPaginationDto searchNextPage(String scrollId);

}
