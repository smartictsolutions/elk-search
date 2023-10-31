/* SmartICT Bilisim A.S. (C) 2022 */
package com.smartict.elk.controller.util.version;

import com.smartict.elk.controller.util.yaml.YamlPropertySourceFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value = "classpath:version.yml", factory = YamlPropertySourceFactory.class)
public class VersionProperties {

    @Value("${version}")
    private String version;

    public String getVersion() {
        return version;
    }

}
