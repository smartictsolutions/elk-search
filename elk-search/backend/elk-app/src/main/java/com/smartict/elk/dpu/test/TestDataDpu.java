/* SmartICT Bilisim A.S. (C) 2021 */
package com.smartict.elk.dpu.test;

import com.smartict.elk.dpu.management.DpuStarterEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
@Order(4)
public class TestDataDpu implements ApplicationListener<DpuStarterEvent> {
    private static final Logger LOGGER = LoggerFactory.getLogger(TestDataDpu.class);

    private final ApplicationContext appContext;

    @Value("${elk.is-test-env}")
    private Boolean isTestEnv;

    public TestDataDpu(ApplicationContext appContext) {
        this.appContext = appContext;
    }

    @Override
    @SuppressWarnings("squid:S4507")
    public void onApplicationEvent(@NonNull DpuStarterEvent event) {
        try {
            if (this.isTestEnv) {}
        } catch (Exception e) {
            e.printStackTrace();
            SpringApplication.exit(this.appContext, () -> 1);
            System.exit(1);
        }
    }

}
