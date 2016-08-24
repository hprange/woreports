package com.woreports.jasper;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.name.Names;
import com.woreports.api.ReportProcessor;

import net.sf.jasperreports.engine.JRDataSource;

/**
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 */
public class JasperReportModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(new TypeLiteral<ReportProcessor<JRDataSource>>() {}).to(JasperReportProcessor.class);

        install(new FactoryModuleBuilder()
                .implement(JasperReportRecipe.class, Names.named("model"), JasperModelReportRecipe.class)
                .implement(JasperReportRecipe.class, Names.named("template"), JasperTemplateReportRecipe.class)
                .build(JasperReportRecipeFactory.class));

        install(new FactoryModuleBuilder()
                .implement(JasperFiller.class, Names.named("model"), JasperModelFiller.class)
                .implement(JasperFiller.class, Names.named("template"), JasperTemplateFiller.class)
                .build(JasperFillerFactory.class));
    }
}
