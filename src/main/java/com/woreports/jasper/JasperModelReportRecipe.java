package com.woreports.jasper;

import java.util.Map;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOQualifier;
import com.webobjects.eocontrol.EOSortOrdering;
import com.webobjects.foundation.NSArray;
import com.woreports.api.Format;
import com.woreports.api.ReportModel;
import com.woreports.api.WOReports;
import com.woreports.jasper.data.JasperEOBatchDataSource;

import net.sf.jasperreports.engine.JRDataSource;

/**
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 */
public class JasperModelReportRecipe extends AbstractJasperReportRecipe {
    private final JasperFillerFactory fillerFactory;

    @Inject
    public JasperModelReportRecipe(@WOReports EOEditingContext editingContext, JasperFillerFactory fillerFactory, @Assisted ReportModel model) {
        super(editingContext, model);

        this.fillerFactory = fillerFactory;
    }

    @Override
    public JasperReportRecipe filledBy(Map<String, Object> parameters, EOQualifier qualifier, NSArray<EOSortOrdering> sortOrderings) {
        JRDataSource dataSource = new JasperEOBatchDataSource(editingContext, model.baseEntity().name(), model.keyPaths(), qualifier, model.sortOrderings().arrayByAddingObjectsFromArray(sortOrderings));

        return filledBy(parameters, dataSource);
    }

    @Override
    protected JasperFiller filler(Format format) {
        return fillerFactory.createModelFiller(model, format);
    }
}
