package br.com.wobr.reports;

import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;

/**
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 */
public abstract class AbstractReportProcessor
{
	private final ReportProcessor nextProcessor;

	public AbstractReportProcessor()
	{
		this.nextProcessor = null;
	}

	public AbstractReportProcessor( final ReportProcessor nextProcessor )
	{
		this.nextProcessor = nextProcessor;
	}

	protected abstract byte[] handleProcessing( Format format, ReportModel model, Map<String, Object> parameters, JRDataSource dataSource ) throws ReportProcessingException;

	public final byte[] process( final Format format, final ReportModel model, final Map<String, Object> parameters, final JRDataSource dataSource ) throws ReportProcessingException
	{
		byte[] result = handleProcessing( format, model, parameters, dataSource );

		if( result != null )
		{
			return result;
		}

		if( nextProcessor == null )
		{
			// throw new ReportProcessingException(
			// "The report model cannot be handled by any processor. Please, verify if the model is correctly filled."
			// );

			return null;
		}

		return nextProcessor.process( format, model, parameters, dataSource );
	}
}
