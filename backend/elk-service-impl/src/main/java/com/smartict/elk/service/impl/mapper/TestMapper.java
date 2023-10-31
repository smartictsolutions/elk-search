/* SmartICT Bilisim A.S. (C) 2021 */
package com.smartict.elk.service.impl.mapper;

import com.smartict.elk.service.impl.mapper.config.GeneralMapStructConfig;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(config = GeneralMapStructConfig.class)
public interface TestMapper {
    TestMapper MAPPER = Mappers.getMapper(TestMapper.class);
}
