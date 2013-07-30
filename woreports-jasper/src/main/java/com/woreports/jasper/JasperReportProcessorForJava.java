package com.woreports.jasper;

import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperPrint;

import com.google.inject.Inject;
import com.webobjects.eocontrol.EOQualifier;
import com.webobjects.eocontrol.EOSortOrdering;
import com.webobjects.foundation.NSArray;
import com.woreports.api.AbstractReportProcessor;
import com.woreports.api.Format;
import com.woreports.api.ReportExporter;
import com.woreports.api.ReportModel;
import com.woreports.api.ReportProcessingException;
import com.woreports.api.ReportTemplate;

/**
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 */
public class JasperReportProcessorForJava extends AbstractReportProcessor {
    private final ReportExporter<JasperPrint> exporter;

    @Inject
    public JasperReportProcessorForJava(ReportExporter<JasperPrint> exporter) {
        super();

        this.exporter = exporter;
    }

    @Override
    protected byte[] handleProcessing(Format format, ReportModel model, Map<String, Object> parameters, EOQualifier qualifier, NSArray<EOSortOrdering> sortOrderings) throws ReportProcessingException {
        return handleProcessing(format, model, parameters, null);
    }

    @Override
    protected byte[] handleProcessing(Format format, ReportModel model, Map<String, Object> parameters, JRDataSource dataSource) throws ReportProcessingException {
        Class<? extends ReportTemplate<?>> templateClass = model.javaClassTemplate();

        if (templateClass == null) {
            return null;
        }

        ReportTemplate<JasperPrint> template = objectForClass(templateClass);

        JasperPrint print = template.build(model);

        return exporter.export(print);
    }

    @SuppressWarnings("unchecked")
    ReportTemplate<JasperPrint> objectForClass(Class<? extends ReportTemplate<?>> clazz) throws ReportProcessingException {
        try {
            return (ReportTemplate<JasperPrint>) clazz.newInstance();
        } catch (Exception exception) {
            throw new ReportProcessingException(exception);
        }
    }
}
