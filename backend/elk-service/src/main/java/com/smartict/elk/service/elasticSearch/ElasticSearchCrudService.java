/* SmartICT Bilisim A.S. (C) 2023 */
package com.smartict.elk.service.elasticSearch;

import java.util.List;

import com.smartict.elk.dto.elasticSearch.ElasticSearchDataDto;

import org.elasticsearch.search.SearchHit;

public interface ElasticSearchCrudService {

    Boolean insertBulkData(List<ElasticSearchDataDto> dataList);

    Boolean insertData(ElasticSearchDataDto data);

    Boolean updateBulkData(List<ElasticSearchDataDto> dataList);

    Boolean updateData(ElasticSearchDataDto data);

    Boolean deleteBulkData(List<ElasticSearchDataDto> dataList);

    Boolean deleteData(ElasticSearchDataDto data);

    List<ElasticSearchDataDto> getAllData();

    SearchHit[] getAllDocuments(int documentCount, int offset);

}
