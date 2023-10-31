/* SmartICT Bilisim A.S. (C) 2020 */
package com.smartict.elk.dpu.system;

import com.smartict.elk.dpu.management.DpuStarterEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
@Order(2)
public class SystemDefaultDataDpu implements ApplicationListener<DpuStarterEvent> {
    private static final Logger LOGGER = LoggerFactory.getLogger(SystemDefaultDataDpu.class);

    private final ApplicationContext appContext;

    public SystemDefaultDataDpu(ApplicationContext appContext) {
        this.appContext = appContext;
    }

    @Override
    @SuppressWarnings("squid:S4507")
    public void onApplicationEvent(@NonNull DpuStarterEvent event) {
        try {
            // TODO: evrim.oguzhan hazırlanacak verilere göre ekleme yapılacaktır.
        } catch (Exception e) {
            e.printStackTrace();
            SpringApplication.exit(this.appContext, () -> 1);
            System.exit(1);
        }
    }

}
