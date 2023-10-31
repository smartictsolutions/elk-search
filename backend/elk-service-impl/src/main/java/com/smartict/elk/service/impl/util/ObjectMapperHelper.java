/* SmartICT Bilisim A.S. (C) 2023 */
package com.smartict.elk.service.impl.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ObjectMapperHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(ObjectMapperHelper.class);

    final ObjectMapper mapper = new ObjectMapper();

    public <T> T convertValue(Object obj, Class<T> clss) {
        return mapper.convertValue(obj, clss);
    }

    public String fromJsonObjectToString(Object obj) {
        String stringObj = "";

        try {
            stringObj = mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            LOGGER.error("ELK-SEARCH LOG -> String'e dönüştürme işlemi başarısız! ", e);

            throw new RuntimeException(e);
        }
        return stringObj;
    }
}
