package com.woreports.jasper;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRVirtualizer;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.fill.JRSwapFileVirtualizer;
import net.sf.jasperreports.engine.util.JRSwapFile;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.UnhandledException;

import ar.com.fdvs.dj.core.DynamicJasperHelper;
import ar.com.fdvs.dj.core.layout.ClassicLayoutManager;
import ar.com.fdvs.dj.core.layout.ListLayoutManager;
import ar.com.fdvs.dj.domain.CustomExpression;
import ar.com.fdvs.dj.domain.DynamicReport;
import ar.com.fdvs.dj.domain.Style;
import ar.com.fdvs.dj.domain.builders.ColumnBuilder;
import ar.com.fdvs.dj.domain.builders.ColumnBuilderException;
import ar.com.fdvs.dj.domain.builders.DynamicReportBuilder;
import ar.com.fdvs.dj.domain.builders.GroupBuilder;
import ar.com.fdvs.dj.domain.constants.GroupLayout;
import ar.com.fdvs.dj.domain.constants.HorizontalAlign;
import ar.com.fdvs.dj.domain.entities.DJGroup;
import ar.com.fdvs.dj.domain.entities.columns.AbstractColumn;
import ar.com.fdvs.dj.domain.entities.columns.PropertyColumn;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.webobjects.eoaccess.EOAttribute;
import com.webobjects.eoaccess.EOEntity;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOQualifier;
import com.webobjects.eocontrol.EOSortOrdering;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSTimestamp;
import com.woreports.api.AbstractReportProcessor;
import com.woreports.api.Format;
import com.woreports.api.ReportColumn;
import com.woreports.api.ReportModel;
import com.woreports.api.ReportProcessingException;
import com.woreports.localization.LocalizerKeyGenerator;

import er.extensions.localization.ERXLocalizer;

/**
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 */
public class JasperReportProcessorForModel extends AbstractReportProcessor {
    private final Provider<DynamicReportBuilder> builderProvider;
    private final Provider<EOEditingContext> editingContextProvider;
    private final Provider<ERXLocalizer> localizerProvider;
    private final Provider<Style> styleProvider;
    private DynamicReportBuilder builder;
    private Format format;
    private Map<String, Object> parameters;
    private JRDataSource dataSource;
    private boolean isPrepared = false;

    @Inject
    public JasperReportProcessorForModel(Provider<EOEditingContext> editingContextProvider, Provider<ERXLocalizer> localizerProvider, Provider<DynamicReportBuilder> builderProvider, Provider<Style> styleProvider) {
        super();

        this.editingContextProvider = editingContextProvider;
        this.localizerProvider = localizerProvider;
        this.builderProvider = builderProvider;
        this.styleProvider = styleProvider;
    }

    @Override
    public void prepareReport(Format format, ReportModel model, Map<String, Object> parameters, EOQualifier qualifier, NSArray<EOSortOrdering> sortOrderings) throws ReportProcessingException {
        JRDataSource dataSource = new JasperEofBatchDataSource(editingContextProvider.get(), model.baseEntity().name(), model.keyPaths(), qualifier, model.sortOrderings().arrayByAddingObjectsFromArray(sortOrderings));

        prepareReport(format, model, parameters, dataSource);
    }

    @Override
    public byte[] generateReport() throws ReportProcessingException {
        if (!isPrepared) {
            return null;
        }

        JRSwapFile swapFile = new JRSwapFile("/tmp", 1024, 1024);

        JRVirtualizer virtualizer = new JRSwapFileVirtualizer(2, swapFile, true);

        parameters.put(JRParameter.REPORT_VIRTUALIZER, virtualizer);

        try {
            if (Format.XLS.equals(format)) {
                builder.setPrintColumnNames(true);
                builder.setIgnorePagination(true);

                DynamicReport dr = builder.build();

                dr.setReportLocale(new Locale("pt", "BR"));

                JasperPrint print = DynamicJasperHelper.generateJasperPrint(dr, new ListLayoutManager(), dataSource, parameters);

                JRExporter exporter = new JRXlsExporter();

                exporter.setParameter(JRXlsExporterParameter.IGNORE_PAGE_MARGINS, Boolean.TRUE);
                exporter.setParameter(JRXlsExporterParameter.IS_COLLAPSE_ROW_SPAN, Boolean.TRUE);
                exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS, Boolean.TRUE);
                exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
                exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
                exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
                exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
                exporter.setParameter(JRXlsExporterParameter.IS_IGNORE_GRAPHICS, Boolean.TRUE);
                exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
                exporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);

                try {
                    File file = File.createTempFile("report", ".xls");

                    exporter.setParameter(JRExporterParameter.OUTPUT_FILE, file);

                    exporter.exportReport();

                    return FileUtils.readFileToByteArray(file);
                } catch (IOException exception) {
                    throw new UnhandledException(exception);
                }
            }

            DynamicReport dr = builder.build();

            JasperPrint print = DynamicJasperHelper.generateJasperPrint(dr, new ClassicLayoutManager(), dataSource, parameters);

            return JasperExportManager.exportReportToPdf(print);
        } catch (JRException exception) {
            throw new ReportProcessingException("An unexpected error occurred while trying to build the report.", exception);
        } finally {
            if (virtualizer != null) {
                virtualizer.cleanup();
            }
        }
    }

    @Override
    public void prepareReport(Format format, ReportModel model, Map<String, Object> parameters, JRDataSource dataSource) throws ReportProcessingException {
        if (model.baseEntity() == null) {
            return;
        }

        this.format = format;
        this.parameters = parameters;
        this.dataSource = dataSource;

        builder = builderProvider.get();

        builder.setTitle(model.title()).setSubtitle(model.subtitle());

        EOEntity entity = model.baseEntity();

        for (ReportColumn column : model.columns()) {
            EOAttribute attribute = entity._attributeForPath(column.keypath());

            if (attribute == null) {
                throw new ReportProcessingException("Cannot find an EOAttribute for the keypath '" + column.keypath() + "' in " + entity.name() + " entity. Are you sure it is an attribute and not a relationship? Also check the spelling.");
            }

            String classname = attribute.adaptorValueClass().getName();

            if (NSTimestamp.class.getName().equals(classname)) {
                classname = Date.class.getName();
            }

            String columnTitle = titleForColumn(entity, column);

            ColumnBuilder columnBuilder = ColumnBuilder.getNew().setColumnProperty(column.keypath(), classname).setTitle(columnTitle).setPattern(column.pattern());

            if (column.width() != null) {
                columnBuilder.setWidth(column.width());
                columnBuilder.setFixedWidth(false);
            }

            processColumnStyle(column, columnBuilder, classname);
            processCustomExpression(column, columnBuilder, builder);

            AbstractColumn djColumn = null;

            try {
                djColumn = columnBuilder.build();
            } catch (ColumnBuilderException exception) {
                throw new ReportProcessingException("An unexpected error occurred while trying to build the report.", exception);
            }

            if (column.hidden()) {
                builder.addField(column.title(), classname);
            } else {
                builder.addColumn(djColumn);
            }

            if (column.groupedBy()) {
                GroupBuilder groupBuilder = new GroupBuilder();

                DJGroup group = groupBuilder.setCriteriaColumn((PropertyColumn) djColumn).setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS).build();

                builder.addGroup(group);

                builder.setPrintColumnNames(false);
            }
        }
    }

    private void processColumnStyle(ReportColumn column, ColumnBuilder columnBuilder, String classname) throws ReportProcessingException {
        Style style = styleProvider.get();

        style.getFont().setFontSize(8);

        if (column.fontSize() != null && column.fontSize().length() > 0) {
            try {
                style.getFont().setFontSize(Integer.parseInt(column.fontSize().trim().replace("pt", "").replace("px", "")));
                style.getFont().setPdfFontEmbedded(true);
            } catch (Exception e) {
                throw new ReportProcessingException("Ocorreu um erro ao gerar o relat\u00f3rio: Tamanho da fonte deve ser num\u00e9rico", e);
            }
        }

        if (column.fontColor() != null && column.fontColor().length() > 0) {
            try {
                style.setTextColor(new Color(Integer.parseInt(column.fontColor().replace("#", ""), 16)));
            } catch (Exception e) {
                throw new ReportProcessingException("Ocorreu um erro ao gerar o relat\u00f3rio: A cor da fonte deve ser informada em HexaDecimal v\u00e1lido Ex: #FF0000", e);
            }
        }

        switch (column.alignment()) {
        case CENTER:
            style.setHorizontalAlign(HorizontalAlign.CENTER);
            break;

        case RIGHT:
            style.setHorizontalAlign(HorizontalAlign.RIGHT);
            break;

        case LEFT:
        default:
            style.setHorizontalAlign(HorizontalAlign.LEFT);
            break;
        }

        if (BigDecimal.class.getName().equals(classname)) {
            Style red;

            try {
                red = (Style) BeanUtils.cloneBean(style);
                // TODO: Check why the CustomExpression is executed twice if the
                // ConditionalStyle is added.
                // columnBuilder.addConditionalStyle(new ConditionalStyle(new
                // AmmountCondition(), red));
            } catch (Exception e) {
                throw new ReportProcessingException("Erro ao gerar relat\u00f3rio", e);
            }

            red.setTextColor(Color.RED);

        }

        columnBuilder.setStyle(style);

        isPrepared = true;
    }

    private void processCustomExpression(ReportColumn column, ColumnBuilder columnBuilder, DynamicReportBuilder reportBuilder) throws ReportProcessingException {
        Class<? extends CustomExpression> customExpressionClass = column.customExpressionClass();

        if (customExpressionClass == null) {
            return;
        }

        CustomExpression customExpression = null;

        try {
            customExpression = customExpressionClass.newInstance();
        } catch (Exception exception) {
            throw new ReportProcessingException(exception);
        }

        columnBuilder.setColumnProperty(null);
        columnBuilder.setCustomExpression(customExpression);

        EOEntity entity = column.model().baseEntity();

        String classname = entity._attributeForPath(column.keypath()).className();

        reportBuilder.addField(column.keypath(), classname);
    }

    private String titleForColumn(EOEntity entity, ReportColumn column) {
        String key = column.title();

        if (key == null) {
            LocalizerKeyGenerator generator = new LocalizerKeyGenerator();

            key = generator.generateKey(entity, column.keypath());
        }

        ERXLocalizer localizer = localizerProvider.get();

        String title = (String) localizer.valueForKey(key);

        return title == null ? key : title;
    }
}