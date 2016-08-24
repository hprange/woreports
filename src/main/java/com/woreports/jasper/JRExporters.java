package com.woreports.jasper;

import com.woreports.api.Format;

import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;

/**
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 */
class JRExporters {
    static JRExporter of(Format format) {
        switch (format) {
        case PDF:
            return new JRPdfExporter();

        case XLS:
            JRExporter exporter = new JRXlsxExporter();

            exporter.setParameter(JRXlsExporterParameter.IGNORE_PAGE_MARGINS, true);
            exporter.setParameter(JRXlsExporterParameter.IS_COLLAPSE_ROW_SPAN, true);
            exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS, true);
            exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, true);
            exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, false);
            exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, true);
            exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, false);
            exporter.setParameter(JRXlsExporterParameter.IS_IGNORE_GRAPHICS, true);
            exporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, true);

            return exporter;
        default:
            throw new UnsupportedOperationException("Unable to create a exporter for format " + format);
        }
    }

    private JRExporters() {
        // Shouldn't be instantiated
    }
}
