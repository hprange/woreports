package com.woreports.jasper;

import java.util.Map;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.woreports.api.ReportProcessingException;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

/**
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 */
public class JasperTemplateFiller implements JasperFiller {
    private final JasperReport report;

    @Inject
    public JasperTemplateFiller(@Assisted JasperReport report) {
        this.report = report;
    }

    @Override
    public JasperPrint fillReport(Map<String, Object> parameters, JRDataSource dataSource) throws ReportProcessingException {
        try {
            return JasperFillManager.fillReport(report, parameters, dataSource);
        } catch (JRException exception) {
            throw new ReportProcessingException(exception);
        }
    }
}
