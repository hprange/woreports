package com.woreports.jasper;

import com.woreports.api.Format;

import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.Exporter;
import net.sf.jasperreports.export.ExporterInput;
import net.sf.jasperreports.export.OutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;

/**
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 */
class JRExporters {
    static Exporter<ExporterInput, ?, ?, OutputStreamExporterOutput> of(Format format) {
        switch (format) {
            case PDF:
                return new JRPdfExporter();

            case XLSX:
                JRXlsxExporter exporter = new JRXlsxExporter();

                SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();

                configuration.setIgnorePageMargins(true);
                configuration.setCollapseRowSpan(true);
                configuration.setRemoveEmptySpaceBetweenColumns(true);
                configuration.setRemoveEmptySpaceBetweenRows(true);
                configuration.setOnePagePerSheet(false);
                configuration.setRemoveEmptySpaceBetweenRows(true);
                configuration.setWhitePageBackground(false);
                configuration.setIgnoreGraphics(true);
                configuration.setDetectCellType(true);

                exporter.setConfiguration(configuration);

                return exporter;
            default:
                throw new UnsupportedOperationException("Unable to create a exporter for format " + format);
        }
    }

    private JRExporters() {
        // Shouldn't be instantiated
    }
}
