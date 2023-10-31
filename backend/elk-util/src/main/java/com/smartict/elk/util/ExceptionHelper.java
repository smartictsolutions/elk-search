/* SmartICT Bilisim A.S. (C) 2023 */
package com.smartict.elk.util;

import com.smartict.elk.constant.exception.ServiceException;
import com.smartict.elk.constant.exception.ValidationException;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class ExceptionHelper {

    private static ObjectMapper objectMapper = setObjectMapper();

    private static ObjectMapper setObjectMapper() {
        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }

    public static void throwDetailedServiceException(Object restResponse) {
        String stringifiedMessage = null;
        try {
            stringifiedMessage = objectMapper.writeValueAsString(restResponse);
        } catch (Exception e) {
            throw new ServiceException("throwDetailedException exception", e.getLocalizedMessage());
        }
        throw new ServiceException(stringifiedMessage, stringifiedMessage);
    }

    public static void throwDetailedValidationException(Object restResponse) {
        String stringifiedMessage = null;
        try {
            stringifiedMessage = objectMapper.writeValueAsString(restResponse);
        } catch (Exception e) {
            throw new ValidationException("throwDetailedException exception", e.getLocalizedMessage());
        }
        throw new ValidationException(stringifiedMessage, stringifiedMessage);
    }
}
