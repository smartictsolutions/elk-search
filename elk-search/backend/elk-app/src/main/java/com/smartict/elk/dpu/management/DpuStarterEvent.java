/* SmartICT Bilisim A.S. (C) 2020 */
package com.smartict.elk.dpu.management;

import org.springframework.context.ApplicationEvent;

public class DpuStarterEvent extends ApplicationEvent {
    public DpuStarterEvent(Object source) {
        super(source);
    }
}
