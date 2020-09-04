package com.woreports.jasper;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.woreports.api.Format;

import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.Exporter;
import net.sf.jasperreports.export.ExporterInput;
import net.sf.jasperreports.export.OutputStreamExporterOutput;

/**
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 */
public class TestJRExporters {
    @Test
    public void createPdfExporterWhenCreatingExporterForPdfFormat() throws Exception {
        Exporter<ExporterInput, ?, ?, OutputStreamExporterOutput> result = JRExporters.of(Format.PDF);

        assertThat(result, instanceOf(JRPdfExporter.class));
    }

    @Test
    public void createExcelExporterWhenCreatingExporterForXlsFormat() throws Exception {
        Exporter<ExporterInput, ?, ?, OutputStreamExporterOutput> result = JRExporters.of(Format.XLSX);

        assertThat(result, instanceOf(JRXlsxExporter.class));
    }
}
