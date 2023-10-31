/* SmartICT Bilisim A.S. (C) 2023 */
package com.smartict.elk.dto.elasticSearch;

import lombok.Getter;
import lombok.Setter;

/**
 * Elastic Search index içerinde tutulan documentte yer alan keyler özelinde arama işlemi için kullanılmaktadır.
 */
@Getter
@Setter
public class DynamicSearchRequest {
    private String key;
    private String value;
}
