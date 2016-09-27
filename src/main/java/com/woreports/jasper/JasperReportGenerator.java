package com.woreports.jasper;

import java.io.ByteArrayOutputStream;
import java.util.List;

import com.woreports.api.Format;
import com.woreports.api.ReportProcessingException;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporterParameter;

/**
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 */
class JasperReportGenerator {
    final List<JasperPrint> prints;

    JasperReportGenerator(List<JasperPrint> prints) {
        this.prints = prints;
    }

    byte[] generateReport(Format format) throws ReportProcessingException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        JRExporter exporter = JRExporters.of(format);

        exporter.setParameter(JRPdfExporterParameter.JASPER_PRINT_LIST, prints);
        exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baos);

        try {
            exporter.exportReport();
        } catch (JRException exception) {
            throw new ReportProcessingException(exception);
        }

        return baos.toByteArray();
    }
}
