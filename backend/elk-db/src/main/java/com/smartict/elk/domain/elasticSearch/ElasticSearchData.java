/* SmartICT Bilisim A.S. (C) 2023 */
package com.smartict.elk.domain.elasticSearch;

import java.sql.Timestamp;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Document(indexName = "elastic_search_data")
public class ElasticSearchData {

    @Id
    @JsonProperty("id")
    @Field(type = FieldType.Keyword)
    UUID id;
    @JsonProperty("type")
    @Field(type = FieldType.Text)
    String type;

    @JsonProperty("title")
    @Field(type = FieldType.Text)
    String title;

    @JsonProperty("createdOn")
    @Field(type = FieldType.Date)
    Timestamp createdOn;

    @JsonProperty("userId")
    @Field(type = FieldType.Keyword)
    UUID userId;

    @JsonProperty("customerId")
    @Field(type = FieldType.Keyword)
    UUID customerId;
}
