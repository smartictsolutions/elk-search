/* SmartICT Bilisim A.S. (C) 2021 */
package com.smartict.elk.service.impl.mapper.config;

import java.sql.Timestamp;
import java.time.Instant;

/**
 * Timestamp tarih verilerini çevirme işlemi yapoan sınıftır.
 */
public class TimestampMapper {

    /**
     * Timestamp tipinde verilen tarih bilgisini String tipine çevirme işlemini yapan metotdur.
     *
     * @param  timestamp
     * @return           String
     */
    public String asString(Timestamp timestamp) {
        if (timestamp == null) {
            return null;
        }
        Instant instant = timestamp.toInstant();
        return instant.toString();
    }

    /**
     * String tipinde verilen tarih bilgisini Timestamp tipine çevirme işlemini yapan metotdur.
     *
     * @param  timestamp
     * @return           Timestamp
     */
    public Timestamp asTimestamp(String timestamp) {
        if (timestamp == null) {
            return null;
        }
        Instant instant = Instant.parse(timestamp);
        return Timestamp.from(instant);
    }
}