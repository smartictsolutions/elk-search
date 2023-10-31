/* SmartICT Bilisim A.S. (C) 2021 */
package com.smartict.elk.domain;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Test {
    @Column(name = "test")
    private String test;
}
