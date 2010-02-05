package br.com.wobr.reports;

import com.webobjects.eocontrol.EOQualifier;
import com.webobjects.eocontrol.EOSortOrdering;
import com.webobjects.foundation.NSArray;

/**
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 */
public abstract class AbstractReportProcessor implements ReportProcessor
{
	private final ReportProcessor nextProcessor;

	public AbstractReportProcessor(ReportProcessor nextProcessor)
	{
		this.nextProcessor = nextProcessor;
	}

	protected abstract byte[] handleProcessing(Format format, ReportModel model, EOQualifier qualifier, NSArray<EOSortOrdering> additionalSortOrderings) throws ReportProcessingException;

	public final byte[] process(Format format, ReportModel model) throws ReportProcessingException
	{
		return process(format, model, null, null);
	}

	public final byte[] process(Format format, ReportModel model, EOQualifier qualifier) throws ReportProcessingException
	{
		return process(format, model, qualifier, null);
	}

	public final byte[] process(Format format, ReportModel model, EOQualifier qualifier, NSArray<EOSortOrdering> additionalSortOrderings) throws ReportProcessingException
	{
		byte[] result = handleProcessing(format, model, qualifier, additionalSortOrderings);

		if(result != null)
		{
			return result;
		}

		if(nextProcessor == null)
		{
			throw new ReportProcessingException("The report model cannot be handled by any processor. Please, verify if the model is correctly filled.");
		}

		return nextProcessor.process(format, model, qualifier, additionalSortOrderings);
	}
}
