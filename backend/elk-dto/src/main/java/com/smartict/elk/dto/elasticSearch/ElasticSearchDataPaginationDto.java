/* SmartICT Bilisim A.S. (C) 2023 */
package com.smartict.elk.dto.elasticSearch;

import java.util.List;

import lombok.*;

/**
 * Elastic search üzerinden yapılan aramalarda default 10 sonuç yerine aramaya ait tüm sonuçları pagination yapısıyla parça parça tutup dönecek veri yapısıdır.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class ElasticSearchDataPaginationDto {
    private String scrollId;
    private List<ElasticSearchDataDto> elasticSearchDataDtoList;
}
