package com.woreports.jasper;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;

import com.woreports.api.ReportExporter;
import com.woreports.api.ReportProcessingException;

/**
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 */
public class JasperReportExporter implements ReportExporter<JasperPrint> {
    @Override
    public byte[] export(final JasperPrint print) throws ReportProcessingException {
	try {
	    return JasperExportManager.exportReportToPdf(print);
	} catch (JRException exception) {
	    throw new ReportProcessingException(exception);
	}
    }
}
