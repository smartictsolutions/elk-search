/* SmartICT Bilisim A.S. (C) 2020 */
package com.smartict.elk.config;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Merkez birimin isteklerinin ayarlandığı kısımdır.Birimin hangi link ya da ip adreslerinden istekleri kabul edeceğini ayarlanması gereken yerdir. Backend
 * projesine gelecek isteklerin kuralları da yine bu class ile ayarlanır.
 */
@Configuration
public class AppConfig implements WebMvcConfigurer {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppConfig.class);
    @Autowired
    private AllowedHosts allowedHosts;

    @Bean
    public CorsFilter corsFilter() {
        this.allowedHosts.getAllowedHosts().forEach(h -> LOGGER.info("elk::security::cors::allowed-host: {}", h));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(this.allowedHosts.getAllowedHosts());
        config.setAllowedHeaders(Collections.singletonList("*"));
        config.setAllowedMethods(Collections.singletonList("*"));
        config.setExposedHeaders(Arrays.asList("Authorization", "Link", "X-Total-Count"));
        config.setAllowCredentials(true);
        config.setMaxAge(1800L);
        List<String> allowedOrigins = config.getAllowedOrigins();
        if (Objects.nonNull(allowedOrigins) && !allowedOrigins.isEmpty()) {
            source.registerCorsConfiguration("/**", config);
        }
        return new CorsFilter(source);
    }

    @Bean
    public TaskScheduler taskScheduler() {
        return new ConcurrentTaskScheduler();
    }
}
