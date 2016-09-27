package com.woreports.jasper;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOQualifier;
import com.webobjects.eocontrol.EOSortOrdering;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSMutableArray;
import com.woreports.api.Format;
import com.woreports.api.ReportModel;
import com.woreports.api.WOReports;
import com.woreports.jasper.data.JasperEOBatchDataSource;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;

/**
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 */
public class JasperTemplateReportRecipe extends AbstractJasperReportRecipe {
    private static JasperReport loadJasperReport(URL url) {
        try (InputStream in = url.openStream()) {
            if (url.toExternalForm().endsWith(".jrxml")) {
                return JasperCompileManager.compileReport(in);
            }

            return (JasperReport) JRLoader.loadObject(in);
        } catch (JRException | IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    private final JasperReport report;
    private final JasperFillerFactory fillerFactory;

    @Inject
    public JasperTemplateReportRecipe(@WOReports EOEditingContext editingContext, JasperFillerFactory fillerFactory, @Assisted ReportModel model) {
        super(editingContext, model);

        this.fillerFactory = fillerFactory;

        report = loadJasperReport(model.templateLocation());
    }

    @Override
    public JasperReportRecipe filledBy(Map<String, Object> parameters, EOQualifier qualifier, NSArray<EOSortOrdering> sortOrderings) {
        JRField[] fields = report.getFields();

        NSArray<String> keypaths = new NSMutableArray<>(fields.length);

        for (JRField field : fields) {
            keypaths.add(field.getName());
        }

        JRDataSource dataSource = new JasperEOBatchDataSource(editingContext, model.baseEntity().name(), keypaths, qualifier, model.sortOrderings().arrayByAddingObjectsFromArray(sortOrderings));

        return filledBy(parameters, dataSource);
    }

    @Override
    protected JasperFiller filler(Format format) {
        return fillerFactory.createTemplateFiller(report);
    }
}
