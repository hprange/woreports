package com.woreports.jasper;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.woreports.api.ReportModel;
import com.woreports.jasper.JasperReportProcessor;
import com.woreports.jasper.JasperReportRecipeFactory;

/**
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 */
@RunWith(MockitoJUnitRunner.class)
public class TestJasperReportProcessor {
    @Mock
    private JasperReportRecipeFactory factory;

    @Mock
    private ReportModel model;

    @Test
    public void createTemplateRecipeWhenProcessingReportWithTemplate() throws Exception {
        when(model.templateLocation()).thenReturn(getClass().getResource("/sample.jrxml"));

        JasperReportProcessor processor = new JasperReportProcessor(factory);

        processor.recipeFor(model);

        verify(factory).createTemplateRecipe(model);
    }

    @Test
    public void createModelRecipeWhenProcessingReportWithoutTemplate() throws Exception {
        JasperReportProcessor processor = new JasperReportProcessor(factory);

        processor.recipeFor(model);

        verify(factory).createModelRecipe(model);
    }
}
