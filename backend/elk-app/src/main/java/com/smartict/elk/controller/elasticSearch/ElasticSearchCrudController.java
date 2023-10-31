/* SmartICT Bilisim A.S. (C) 2023 */
package com.smartict.elk.controller.elasticSearch;

import java.util.List;

import com.smartict.elk.dto.elasticSearch.ElasticSearchDataDto;
import com.smartict.elk.service.elasticSearch.ElasticSearchCrudService;

import org.elasticsearch.search.SearchHit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("elkData")
public class ElasticSearchCrudController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ElasticSearchCrudController.class);

    private final ElasticSearchCrudService elasticSearchCrudService;

    public ElasticSearchCrudController(ElasticSearchCrudService elasticSearchCrudService) {
        this.elasticSearchCrudService = elasticSearchCrudService;
    }

    @PostMapping("bulkInsert")
    public ResponseEntity<Boolean> bulkInsert(@RequestBody List<ElasticSearchDataDto> dataList) {
        return ResponseEntity.ok(elasticSearchCrudService.insertBulkData(dataList));
    }

    @PostMapping("insertData")
    public ResponseEntity<Boolean> insertData(@RequestBody ElasticSearchDataDto data) {
        return ResponseEntity.ok(elasticSearchCrudService.insertData(data));
    }

    @PostMapping("bulkUpdate")
    public ResponseEntity<Boolean> bulkUpdate(@RequestBody List<ElasticSearchDataDto> dataList) {
        return ResponseEntity.ok(elasticSearchCrudService.updateBulkData(dataList));
    }

    @PostMapping("updateData")
    public ResponseEntity<Boolean> updateData(@RequestBody ElasticSearchDataDto data) {
        return ResponseEntity.ok(elasticSearchCrudService.updateData(data));
    }

    @DeleteMapping("bulkDelete")
    public ResponseEntity<Boolean> bulkDelete(@RequestBody List<ElasticSearchDataDto> dataList) {
        return ResponseEntity.ok(elasticSearchCrudService.deleteBulkData(dataList));
    }

    @DeleteMapping("deleteData")
    public ResponseEntity<Boolean> deleteData(@RequestBody ElasticSearchDataDto data) {
        return ResponseEntity.ok(elasticSearchCrudService.deleteData(data));
    }

    @GetMapping("getAllData")
    public ResponseEntity<List<ElasticSearchDataDto>> getAllData() {
        return ResponseEntity.ok(elasticSearchCrudService.getAllData());
    }

    @GetMapping("getAllDocuments/{documentCount}/{offset}")
    public ResponseEntity<SearchHit[]> getAllDocuments(@PathVariable int documentCount, @PathVariable int offset) {
        return ResponseEntity.ok(elasticSearchCrudService.getAllDocuments(documentCount, offset));
    }

}
