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

	public AbstractReportProcessor(ReportProcessor nextProcessor)
	{
		this.nextProcessor = nextProcessor;
	}

	protected abstract byte[] handleProcessing(Format format, ReportModel model, EOQualifier qualifier, NSArray<EOSortOrdering> additionalSortOrderings,Map<String,Object> params) throws ReportProcessingException;

	public final byte[] process(Format format, ReportModel model,Map<String,Object> params) throws ReportProcessingException
	{
		return process(format, model, null, NSArray.<EOSortOrdering>emptyArray(), params);
	}

	public final byte[] process(Format format, ReportModel model, EOQualifier qualifier,Map<String,Object> params) throws ReportProcessingException
	{
		return process(format, model, qualifier, NSArray.<EOSortOrdering>emptyArray(), params);
	}

	public final byte[] process(Format format, ReportModel model, EOQualifier qualifier, NSArray<EOSortOrdering> additionalSortOrderings,Map<String,Object> params) throws ReportProcessingException
	{
		byte[] result = handleProcessing(format, model, qualifier, additionalSortOrderings, params);

		if(result != null)
		{
			return result;
		}

		if(nextProcessor == null)
		{
			throw new ReportProcessingException("The report model cannot be handled by any processor. Please, verify if the model is correctly filled.");
		}

		return nextProcessor.process(format, model, qualifier, additionalSortOrderings,params);
	}
}
