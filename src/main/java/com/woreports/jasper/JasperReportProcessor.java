package com.woreports.jasper;

import com.google.inject.Inject;
import com.woreports.api.ReportModel;
import com.woreports.api.ReportProcessor;

import net.sf.jasperreports.engine.JRDataSource;

/**
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 */
public class JasperReportProcessor implements ReportProcessor<JRDataSource> {
    private final JasperReportRecipeFactory recipeFactory;

    @Inject
    public JasperReportProcessor(JasperReportRecipeFactory recipeFactory) {
        this.recipeFactory = recipeFactory;
    }

    @Override
    public JasperReportRecipe recipeFor(ReportModel model) {
        if (model.templateLocation() != null) {
            return recipeFactory.createTemplateRecipe(model);
        }

        return recipeFactory.createModelRecipe(model);
    }
}
