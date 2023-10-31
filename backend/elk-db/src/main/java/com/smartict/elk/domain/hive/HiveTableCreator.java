/* SmartICT Bilisim A.S. (C) 2023 */
package com.smartict.elk.domain.hive;

import org.springframework.jdbc.core.JdbcTemplate;

/**
 * TODO: evrim.oguzhan elastiğe gönderilen verilerin tutulması için veri tabanı yapısı kurgulanacaktır. Örnek amacaıyla class oluşturuldu bu class üzerinden
 * gerekli veri tabanı işlemleri yapılacaktır.
 */
public class HiveTableCreator {
    private final JdbcTemplate jdbcTemplate;

    public HiveTableCreator(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void createTable() {
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS table_name (id INT, name STRING)");
    }

}
