package com.woreports.jasper;

import java.util.Map;

import com.woreports.api.ReportProcessingException;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperPrint;

/**
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 */
public interface JasperFiller {
    JasperPrint fillReport(Map<String, Object> parameters, JRDataSource dataSource) throws ReportProcessingException;
}
