package com.woreports.jasper;

import static java.util.Objects.requireNonNull;

import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;

/**
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 */
class JasperReportInput {
    private final Map<String, Object> parameters;
    private final JRDataSource dataSource;

    JasperReportInput(Map<String, Object> parameters, JRDataSource dataSource) {
        this.parameters = requireNonNull(parameters, "The parameters param cannot be null.");
        this.dataSource = requireNonNull(dataSource, "The dataSource param cannot be null.");
    }

    Map<String, Object> parameters() {
        return parameters;
    }

    JRDataSource dataSource() {
        return dataSource;
    }
}