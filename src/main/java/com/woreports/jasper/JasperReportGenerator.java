package com.woreports.jasper;

import java.io.ByteArrayOutputStream;
import java.util.List;

import com.woreports.api.Format;
import com.woreports.api.ReportProcessingException;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.export.Exporter;
import net.sf.jasperreports.export.ExporterInput;
import net.sf.jasperreports.export.OutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;

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

        Exporter<ExporterInput, ?, ?, OutputStreamExporterOutput> exporter = JRExporters.of(format);

        exporter.setExporterInput(SimpleExporterInput.getInstance(prints));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(baos));

        try {
            exporter.exportReport();
        } catch (JRException exception) {
            throw new ReportProcessingException(exception);
        }

        return baos.toByteArray();
    }
}
