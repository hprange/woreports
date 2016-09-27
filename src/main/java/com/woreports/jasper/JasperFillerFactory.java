package com.woreports.jasper;

import com.google.inject.name.Named;
import com.woreports.api.Format;
import com.woreports.api.ReportModel;

import net.sf.jasperreports.engine.JasperReport;

/**
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 */
public interface JasperFillerFactory {
    @Named("model")
    JasperFiller createModelFiller(ReportModel model, Format format);

    @Named("template")
    JasperFiller createTemplateFiller(JasperReport report);
}
