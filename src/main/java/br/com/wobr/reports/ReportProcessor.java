package br.com.wobr.reports;

import java.util.Map;

import com.webobjects.eocontrol.EOQualifier;
import com.webobjects.eocontrol.EOSortOrdering;
import com.webobjects.foundation.NSArray;

/**
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 *
 */
public interface ReportProcessor
{
	public byte[] process(Format format, ReportModel model,Map<String,Object> params) throws ReportProcessingException;

	public byte[] process(Format format, ReportModel model, EOQualifier qualifier,Map<String,Object> params) throws ReportProcessingException;

	public byte[] process(Format format, ReportModel model, EOQualifier qualifier, NSArray<EOSortOrdering> sortOrderings,Map<String,Object> params) throws ReportProcessingException;
}
