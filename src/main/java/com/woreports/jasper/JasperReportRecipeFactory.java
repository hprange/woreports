package com.woreports.jasper;

import com.google.inject.name.Named;
import com.woreports.api.ReportModel;

/**
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 */
public interface JasperReportRecipeFactory {
    @Named("template")
    JasperReportRecipe createTemplateRecipe(ReportModel model);

    @Named("model")
    JasperReportRecipe createModelRecipe(ReportModel model);
}
