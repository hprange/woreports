package com.woreports.api;

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
public class ReportProcessorFacade implements ReportProcessor {
    private final Set<ReportProcessor> processors;

    @Inject
    public ReportProcessorFacade(@Named("ForFacade") Set<ReportProcessor> processors) {
        this.processors = processors;
    }

    @Override
    public void prepareReport(Format format, ReportModel model, Map<String, Object> parameters) throws ReportProcessingException {
        prepareReport(format, model, parameters, (EOQualifier) null);
    }

    @Override
    public void prepareReport(Format format, ReportModel model, Map<String, Object> parameters, EOQualifier qualifier) throws ReportProcessingException {
        prepareReport(format, model, parameters, qualifier, NSArray.<EOSortOrdering> emptyArray());
    }

    @Override
    public void prepareReport(Format format, ReportModel model, Map<String, Object> parameters, EOQualifier qualifier, NSArray<EOSortOrdering> sortOrderings) throws ReportProcessingException {
        for (ReportProcessor processor : processors) {
            processor.prepareReport(format, model, parameters, qualifier, sortOrderings);
        }
    }

    @Override
    public void prepareReport(Format format, ReportModel model, Map<String, Object> parameters, JRDataSource dataSource) throws ReportProcessingException {
        for (ReportProcessor processor : processors) {
            processor.prepareReport(format, model, parameters, dataSource);
        }
    }

    @Override
    public byte[] generateReport() throws ReportProcessingException {
        byte[] data = null;

        for (ReportProcessor processor : processors) {
            data = processor.generateReport();

            if (data != null) {

                return data;
            }
        }

        return data;
    }
}
