/* SmartICT Bilisim A.S. (C) 2021 */
package com.smartict.elk.service.impl.mapper.config;

import java.time.Instant;

/**
 * Instant tarih verilerini çevirme işlemi yapan sınıftır.
 */
public class InstantMapper {

    /**
     * Instant tipinde verilen tarih bilgisini String tipine çevirme işlemini yapan metotdur.
     *
     * @param  instant
     * @return         String
     */
    public String asString(Instant instant) {
        return instant != null ? instant.toString() : null;
    }

    /**
     * String tipinde verilen tarih bilgisini Instant tipine çevirme işlemini yapan metotdur.
     *
     * @param  instant
     * @return         Timestamp
     */
    public Instant asInstant(String instant) {
        return instant != null ? Instant.parse(instant) : null;
    }
}