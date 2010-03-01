package br.com.wobr.reports.jasper;

import br.com.wobr.reports.AbstractReportProcessor;
import br.com.wobr.reports.Format;
import br.com.wobr.reports.ReportModel;
import br.com.wobr.reports.ReportProcessingException;
import br.com.wobr.reports.ReportProcessor;

import com.google.inject.Inject;
import com.webobjects.eocontrol.EOQualifier;
import com.webobjects.eocontrol.EOSortOrdering;
import com.webobjects.foundation.NSArray;

public class JasperReportProcessorForIReport extends AbstractReportProcessor {

	@Inject
	public JasperReportProcessorForIReport(ReportProcessor nextProcessor) {
		super(nextProcessor);

	}

	@Override
	protected byte[] handleProcessing(Format format, ReportModel model,
			EOQualifier qualifier,
			NSArray<EOSortOrdering> additionalSortOrderings)
			throws ReportProcessingException {
		// TODO Auto-generated method stub



		return null;
	}


}
