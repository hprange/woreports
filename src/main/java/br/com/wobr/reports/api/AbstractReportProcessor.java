package br.com.wobr.reports.api;

import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;

import com.webobjects.eocontrol.EOQualifier;
import com.webobjects.eocontrol.EOSortOrdering;
import com.webobjects.foundation.NSArray;

/**
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 */
public abstract class AbstractReportProcessor implements ReportProcessor
{
	public AbstractReportProcessor()
	{
	}

	protected abstract byte[] handleProcessing( Format format, ReportModel model, Map<String, Object> parameters, EOQualifier qualifier, final NSArray<EOSortOrdering> sortOrderings ) throws ReportProcessingException;

	protected abstract byte[] handleProcessing( Format format, ReportModel model, Map<String, Object> parameters, JRDataSource dataSource ) throws ReportProcessingException;

	public final byte[] process( final Format format, final ReportModel model, final Map<String, Object> parameters ) throws ReportProcessingException
	{
		return process( format, model, parameters, (EOQualifier) null );
	}

	public final byte[] process( final Format format, final ReportModel model, final Map<String, Object> parameters, final EOQualifier qualifier ) throws ReportProcessingException
	{
		return process( format, model, parameters, qualifier, NSArray.<EOSortOrdering> emptyArray() );
	}

	public final byte[] process( final Format format, final ReportModel model, final Map<String, Object> parameters, final EOQualifier qualifier, final NSArray<EOSortOrdering> sortOrderings ) throws ReportProcessingException
	{
		return handleProcessing( format, model, parameters, qualifier, sortOrderings );
	}

	public final byte[] process( final Format format, final ReportModel model, final Map<String, Object> parameters, final JRDataSource dataSource ) throws ReportProcessingException
	{
		return handleProcessing( format, model, parameters, dataSource );
	}
}
