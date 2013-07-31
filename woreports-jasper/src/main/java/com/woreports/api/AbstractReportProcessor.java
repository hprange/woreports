package com.woreports.api;

import java.util.Map;

import com.webobjects.eocontrol.EOQualifier;
import com.webobjects.eocontrol.EOSortOrdering;
import com.webobjects.foundation.NSArray;

/**
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 */
public abstract class AbstractReportProcessor implements ReportProcessor {
    public AbstractReportProcessor() {
    }

    @Override
    public final void prepareReport(Format format, ReportModel model, Map<String, Object> parameters) throws ReportProcessingException {
        prepareReport(format, model, parameters, (EOQualifier) null);
    }

    @Override
    public final void prepareReport(Format format, ReportModel model, Map<String, Object> parameters, EOQualifier qualifier) throws ReportProcessingException {
        prepareReport(format, model, parameters, qualifier, NSArray.<EOSortOrdering> emptyArray());
    }
}
