package br.com.wobr.reports.jasper;

import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperPrint;
import br.com.wobr.reports.api.AbstractReportProcessor;
import br.com.wobr.reports.api.Format;
import br.com.wobr.reports.api.ReportExporter;
import br.com.wobr.reports.api.ReportModel;
import br.com.wobr.reports.api.ReportProcessingException;
import br.com.wobr.reports.api.ReportTemplate;

import com.google.inject.Inject;
import com.webobjects.eocontrol.EOQualifier;
import com.webobjects.eocontrol.EOSortOrdering;
import com.webobjects.foundation.NSArray;

/**
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 */
public class JasperReportProcessorForJava extends AbstractReportProcessor
{
	private final ReportExporter<JasperPrint> exporter;

	@Inject
	public JasperReportProcessorForJava( final ReportExporter<JasperPrint> exporter )
	{
		super();

		this.exporter = exporter;
	}

	@Override
	protected byte[] handleProcessing( final Format format, final ReportModel model, final Map<String, Object> parameters, final EOQualifier qualifier, final NSArray<EOSortOrdering> sortOrderings ) throws ReportProcessingException
	{
		return handleProcessing( format, model, parameters, null );
	}

	@Override
	protected byte[] handleProcessing( final Format format, final ReportModel model, final Map<String, Object> parameters, final JRDataSource dataSource ) throws ReportProcessingException
	{
		Class<? extends ReportTemplate<?>> templateClass = model.javaClassTemplate();

		if( templateClass == null )
		{
			return null;
		}

		ReportTemplate<JasperPrint> template = objectForClass( templateClass );

		JasperPrint print = template.build( model );

		return exporter.export( print );
	}

	@SuppressWarnings( "unchecked" )
	ReportTemplate<JasperPrint> objectForClass( final Class<? extends ReportTemplate<?>> clazz ) throws ReportProcessingException
	{
		try
		{
			return (ReportTemplate<JasperPrint>) clazz.newInstance();
		}
		catch( Exception exception )
		{
			throw new ReportProcessingException( exception );
		}
	}
}
