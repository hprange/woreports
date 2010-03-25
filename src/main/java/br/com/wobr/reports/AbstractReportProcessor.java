package br.com.wobr.reports;

import java.util.Map;

import com.webobjects.eocontrol.EOQualifier;
import com.webobjects.eocontrol.EOSortOrdering;
import com.webobjects.foundation.NSArray;

/**
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 */
public abstract class AbstractReportProcessor implements ReportProcessor
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

	protected abstract byte[] handleProcessing( Format format, ReportModel model, Map<String, Object> parameters, EOQualifier qualifier, NSArray<EOSortOrdering> additionalSortOrderings ) throws ReportProcessingException;

	public final byte[] process( final Format format, final ReportModel model, final Map<String, Object> parameters ) throws ReportProcessingException
	{
		return process( format, model, parameters, null, NSArray.<EOSortOrdering> emptyArray() );
	}

	public final byte[] process( final Format format, final ReportModel model, final Map<String, Object> parameters, final EOQualifier qualifier ) throws ReportProcessingException
	{
		return process( format, model, parameters, qualifier, NSArray.<EOSortOrdering> emptyArray() );
	}

	public final byte[] process( final Format format, final ReportModel model, final Map<String, Object> parameters, final EOQualifier qualifier, final NSArray<EOSortOrdering> additionalSortOrderings ) throws ReportProcessingException
	{
		byte[] result = handleProcessing( format, model, parameters, qualifier, additionalSortOrderings );

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

		return nextProcessor.process( format, model, parameters, qualifier, additionalSortOrderings );
	}
}
