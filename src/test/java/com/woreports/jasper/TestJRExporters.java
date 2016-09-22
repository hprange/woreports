package com.woreports.jasper;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.woreports.api.Format;
import com.woreports.jasper.JRExporters;

import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;

/**
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 */
public class TestJRExporters {
    @Test
    public void createPdfExporterWhenCreatingExporterForPdfFormat() throws Exception {
        JRExporter result = JRExporters.of(Format.PDF);

        assertThat(result, instanceOf(JRPdfExporter.class));
    }

    @Test
    public void createExcelExporterWhenCreatingExporterForXlsFormat() throws Exception {
        JRExporter result = JRExporters.of(Format.XLSX);

        assertThat(result, instanceOf(JRXlsxExporter.class));
    }

    @Test
    public void initializeExcelExporterWhenCreatingExporterForXlsFormat() throws Exception {
        JRExporter result = JRExporters.of(Format.XLSX);

        assertThat(result.getParameter(JRXlsExporterParameter.IGNORE_PAGE_MARGINS), is((Object) true));
        assertThat(result.getParameter(JRXlsExporterParameter.IS_COLLAPSE_ROW_SPAN), is((Object) true));
        assertThat(result.getParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS), is((Object) true));
        assertThat(result.getParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS), is((Object) true));
        assertThat(result.getParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET), is((Object) false));
        assertThat(result.getParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS), is((Object) true));
        assertThat(result.getParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND), is((Object) false));
        assertThat(result.getParameter(JRXlsExporterParameter.IS_IGNORE_GRAPHICS), is((Object) true));
        assertThat(result.getParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE), is((Object) true));
    }
}
