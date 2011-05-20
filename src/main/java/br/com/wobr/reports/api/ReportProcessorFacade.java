package br.com.wobr.reports.api;

import java.util.Map;
import java.util.Set;

import net.sf.jasperreports.engine.JRDataSource;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.webobjects.eocontrol.EOQualifier;
import com.webobjects.eocontrol.EOSortOrdering;
import com.webobjects.foundation.NSArray;

/**
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 */
public class ReportProcessorFacade implements ReportProcessor
{
	private final Set<ReportProcessor> processors;

	@Inject
	public ReportProcessorFacade( @Named( "ForFacade" ) final Set<ReportProcessor> processors )
	{
		this.processors = processors;
	}

	public byte[] process( final Format format, final ReportModel model, final Map<String, Object> parameters ) throws ReportProcessingException
	{
		return process( format, model, parameters, (EOQualifier) null );
	}

	public byte[] process( final Format format, final ReportModel model, final Map<String, Object> parameters, final EOQualifier qualifier ) throws ReportProcessingException
	{
		return process( format, model, parameters, qualifier, NSArray.<EOSortOrdering> emptyArray() );
	}

	public byte[] process( final Format format, final ReportModel model, final Map<String, Object> parameters, final EOQualifier qualifier, final NSArray<EOSortOrdering> sortOrderings ) throws ReportProcessingException
	{
		byte[] result = null;

		for( ReportProcessor processor : processors )
		{
			result = processor.process( format, model, parameters, qualifier, sortOrderings );

			if( result != null )
			{
				break;
			}
		}

		return result;
	}

	public byte[] process( final Format format, final ReportModel model, final Map<String, Object> parameters, final JRDataSource dataSource ) throws ReportProcessingException
	{
		byte[] result = null;

		for( ReportProcessor processor : processors )
		{
			result = processor.process( format, model, parameters, dataSource );

			if( result != null )
			{
				break;
			}
		}

		return result;
	}
}
