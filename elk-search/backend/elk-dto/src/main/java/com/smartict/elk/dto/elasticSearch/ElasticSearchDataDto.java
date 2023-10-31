/* SmartICT Bilisim A.S. (C) 2023 */
package com.smartict.elk.dto.elasticSearch;

import java.sql.Timestamp;
import java.util.UUID;

import com.smartict.elk.constant.enums.EnumElasticSearchDataType;

import lombok.*;

/**
 * Elastic elasticSearch üzerinde tutulacak veri modelidir.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class ElasticSearchDataDto {
    UUID id;
    EnumElasticSearchDataType type;
    String title;
    Timestamp registrationDate;
    UUID userId;
    UUID customerId;
}
