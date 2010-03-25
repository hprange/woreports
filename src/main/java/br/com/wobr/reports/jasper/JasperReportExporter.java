package br.com.wobr.reports.jasper;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
import br.com.wobr.reports.ReportExporter;
import br.com.wobr.reports.ReportProcessingException;

/**
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 */
public class JasperReportExporter implements ReportExporter<JasperPrint>
{
	public byte[] export( final JasperPrint print ) throws ReportProcessingException
	{
		try
		{
			return JasperExportManager.exportReportToPdf( print );
		}
		catch( JRException exception )
		{
			throw new ReportProcessingException( exception );
		}
	}
}
