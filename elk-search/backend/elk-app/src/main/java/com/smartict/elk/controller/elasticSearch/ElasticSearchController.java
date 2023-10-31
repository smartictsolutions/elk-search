/* SmartICT Bilisim A.S. (C) 2023 */
package com.smartict.elk.controller.elasticSearch;

import java.util.List;

import com.smartict.elk.constant.exception.ServiceException;
import com.smartict.elk.constant.message.EnumCrudMessages;
import com.smartict.elk.dto.elasticSearch.DynamicSearchRequest;
import com.smartict.elk.dto.elasticSearch.ElasticSearchDataPaginationDto;
import com.smartict.elk.dto.response.ResponseTypeEnum;
import com.smartict.elk.dto.response.RestResponse;
import com.smartict.elk.service.elasticSearch.ElasticSearchService;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.search.SearchHit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ElasticSearchController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ElasticSearchController.class);

    private final ElasticSearchService elasticSearchService;

    public ElasticSearchController(ElasticSearchService elasticSearchService) {
        this.elasticSearchService = elasticSearchService;
    }

    @PostMapping("createIndex")
    public ResponseEntity<Boolean> createIndex() {
        return ResponseEntity.ok(elasticSearchService.createIndex());
    }

    @GetMapping("getIndex/{indexName}/{indexId}")
    public ResponseEntity<GetResponse> getIndex(@PathVariable String indexName, @PathVariable String indexId) {
        return ResponseEntity.ok(elasticSearchService.getIndex(indexName, indexId));
    }

    @DeleteMapping("deleteIndex/{indexName}")
    public ResponseEntity<Boolean> deleteIndex(@PathVariable String indexName) {
        return ResponseEntity.ok(elasticSearchService.deleteIndex(indexName));
    }

    @GetMapping("search/{searchText}/{requestAllResults}")
    public ResponseEntity<RestResponse<ElasticSearchDataPaginationDto>> search(@PathVariable String searchText, @PathVariable Boolean requestAllResults) {
        try {
            return new ResponseEntity<>(
                new RestResponse<>(
                    elasticSearchService.search(searchText, requestAllResults),
                    EnumCrudMessages.READ_TITLE.getLanguageKey(),
                    EnumCrudMessages.READ_TITLE.getLanguageValue(),
                    EnumCrudMessages.READ_SUCCESS_MESSAGE.getLanguageKey(),
                    EnumCrudMessages.READ_SUCCESS_MESSAGE.getLanguageValue(),
                    ResponseTypeEnum.Success
                ),
                HttpStatus.OK
            );
        } catch (ServiceException e) {
            return new ResponseEntity<>(
                new RestResponse<>(
                    EnumCrudMessages.READ_TITLE.getLanguageKey(),
                    EnumCrudMessages.READ_TITLE.getLanguageValue(),
                    e.getMessageLanguageKey(),
                    e.getMessage(),
                    ResponseTypeEnum.Error
                ),
                HttpStatus.NOT_ACCEPTABLE
            );
        }
    }

    @GetMapping("searchNextPage/{scrollId}")
    public ResponseEntity<RestResponse<ElasticSearchDataPaginationDto>> searchNextPage(@PathVariable String scrollId) {
        try {
            return new ResponseEntity<>(
                new RestResponse<>(
                    elasticSearchService.searchNextPage(scrollId),
                    EnumCrudMessages.READ_TITLE.getLanguageKey(),
                    EnumCrudMessages.READ_TITLE.getLanguageValue(),
                    EnumCrudMessages.READ_SUCCESS_MESSAGE.getLanguageKey(),
                    EnumCrudMessages.READ_SUCCESS_MESSAGE.getLanguageValue(),
                    ResponseTypeEnum.Success
                ),
                HttpStatus.OK
            );
        } catch (ServiceException e) {
            return new ResponseEntity<>(
                new RestResponse<>(
                    EnumCrudMessages.READ_TITLE.getLanguageKey(),
                    EnumCrudMessages.READ_TITLE.getLanguageValue(),
                    e.getMessageLanguageKey(),
                    e.getMessage(),
                    ResponseTypeEnum.Error
                ),
                HttpStatus.NOT_ACCEPTABLE
            );
        }
    }

    @GetMapping("searchDynamic")
    public ResponseEntity<SearchHit[]> searchDynamic(@RequestBody List<DynamicSearchRequest> dynamicSearchRequestList) {
        return ResponseEntity.ok(elasticSearchService.searchDynamic(dynamicSearchRequestList));
    }
}
