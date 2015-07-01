package com.woreports.config;

import net.sf.jasperreports.engine.JasperPrint;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.name.Names;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOObjectStoreCoordinator;
import com.woreports.api.ReportExporter;
import com.woreports.api.ReportProcessor;
import com.woreports.api.ReportProcessorFacade;
import com.woreports.api.WOReports;
import com.woreports.jasper.JasperReportExporter;
import com.woreports.jasper.JasperReportProcessorForJava;
import com.woreports.jasper.JasperReportProcessorForModel;
import com.woreports.jasper.JasperReportProcessorForTemplate;

import er.extensions.eof.ERXEC;
import er.extensions.eof.ERXObjectStoreCoordinator;
import er.extensions.eof.ERXObjectStoreCoordinatorSynchronizer;

public class JasperReportModule extends AbstractModule {
    @Override
    protected void configure() {
        Multibinder<ReportProcessor> uriBinder = Multibinder.newSetBinder(binder(), ReportProcessor.class, Names.named("ForFacade"));

        uriBinder.addBinding().to(JasperReportProcessorForTemplate.class);
        uriBinder.addBinding().to(JasperReportProcessorForJava.class);
        uriBinder.addBinding().to(JasperReportProcessorForModel.class);

        bind(ReportProcessor.class).to(ReportProcessorFacade.class);

        bind(new TypeLiteral<ReportExporter<JasperPrint>>() {}).to(JasperReportExporter.class);
    }

    @Provides
    @WOReports
    public EOEditingContext providesEditingContextForBackgroundTasks(@WOReports EOObjectStoreCoordinator coordinator) {
        return ERXEC.newEditingContext(coordinator);
    }

    @Provides
    @Singleton
    @WOReports
    public EOObjectStoreCoordinator providesObjectStoreCoordinatorForBackgroundTasks() {
        ERXObjectStoreCoordinator coordinator = new ERXObjectStoreCoordinator();

        ERXObjectStoreCoordinatorSynchronizer synchronizer = ERXObjectStoreCoordinatorSynchronizer.synchronizer();

        synchronizer.addObjectStore(ERXObjectStoreCoordinator.defaultCoordinator());
        synchronizer.addObjectStore(coordinator);

        return coordinator;
    }
}
