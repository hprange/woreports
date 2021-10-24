package com.woreports.jasper;

import java.awt.Color;
import java.lang.reflect.Constructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.assistedinject.Assisted;
import com.webobjects.eoaccess.EOAttribute;
import com.webobjects.eoaccess.EOEntity;
import com.webobjects.foundation.NSTimestamp;
import com.woreports.api.Format;
import com.woreports.api.ReportColumn;
import com.woreports.api.ReportModel;
import com.woreports.api.ReportProcessingException;
import com.woreports.custom.JasperReportColumnCustomizer;
import com.woreports.localization.LocalizerKeyGenerator;

import ar.com.fdvs.dj.core.DynamicJasperHelper;
import ar.com.fdvs.dj.core.layout.ClassicLayoutManager;
import ar.com.fdvs.dj.core.layout.LayoutManager;
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
import er.extensions.crypting.ERXCryptoString;
import er.extensions.localization.ERXLocalizer;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;

/**
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 */
public class JasperModelFiller implements JasperFiller {
    private final DynamicReportBuilder builder;
    private final Format format;
    private final ERXLocalizer localizer;
    private final Provider<Style> styleProvider;

    @Inject
    public JasperModelFiller(Provider<Style> styleProvider, ERXLocalizer localizer, @Assisted ReportModel model, @Assisted Format format, JasperReportColumnCustomizer columnCustomizer) throws ReportProcessingException {
        this.styleProvider = styleProvider;
        this.localizer = localizer;
        this.format = format;
        DynamicReportBuilder builder = new DynamicReportBuilder();

        builder.setTitle(model.title()).setSubtitle(model.subtitle());

        EOEntity entity = model.baseEntity();

        for (ReportColumn column : model.columns()) {
            EOAttribute attribute = entity._attributeForPath(column.keypath());

            if (attribute == null) {
                throw new ReportProcessingException("Cannot find an EOAttribute for the keypath '" + column.keypath() + "' in " + entity.name() + " entity. Are you sure it is an attribute and not a relationship? Also check the spelling.");
            }

            String classname = attribute.valueTypeClassName();

            if (NSTimestamp.class.getName().equals(classname)) {
                classname = Date.class.getName();
            }

            if (ERXCryptoString.class.getSimpleName().equals(classname)) {
                classname = String.class.getName();
            }

            if (LocalDate.class.getSimpleName().equals(classname)) {
                classname = LocalDate.class.getName();
            }

            String columnTitle = titleForColumn(entity, column);
            String pattern = columnCustomizer.patternFor(column);

            ColumnBuilder columnBuilder = ColumnBuilder.getNew()
                                                       .setColumnProperty(column.keypath(), classname)
                                                       .setTitle(columnTitle)
                                                       .setPattern(pattern);

            if (column.width() != null) {
                columnBuilder.setWidth(column.width());
                columnBuilder.setFixedWidth(false);
            }

            processColumnStyle(column, columnBuilder, classname);
            processCustomExpression(model, column, columnBuilder, builder);

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

        if (format == Format.XLSX) {
            builder.setPrintColumnNames(true);
            builder.setIgnorePagination(true);
        }

        this.builder = builder;
    }

    @Override
    public JasperPrint fillReport(Map<String, Object> parameters, JRDataSource dataSource) throws ReportProcessingException {
        DynamicReport dr = builder.build();
        dr.setReportLocale(localizer.locale());

        LayoutManager layoutManager = format == Format.XLSX ? new ListLayoutManager() : new ClassicLayoutManager();

        try {
            return DynamicJasperHelper.generateJasperPrint(dr, layoutManager, dataSource, parameters);
        } catch (JRException exception) {
            throw new ReportProcessingException(exception);
        }
    }

    // No replacement for the HorizontalAlign class was provided in the last version of Dynamic Jasper.
    @SuppressWarnings("deprecation")
    private void processColumnStyle(ReportColumn column, ColumnBuilder columnBuilder, String classname) throws ReportProcessingException {
        Style style = styleProvider.get();

        style.getFont().setFontSize(8);

        if (column.fontSize() != null && column.fontSize().length() > 0) {
            try {
                style.getFont().setFontSize(Integer.parseInt(column.fontSize().trim().replace("pt", "").replace("px", "")));
                style.getFont().setPdfFontEmbedded(true);
            } catch (Exception e) {
                throw new ReportProcessingException("Ocorreu um erro ao gerar o relatório: Tamanho da fonte deve ser numérico", e);
            }
        }

        if (column.fontColor() != null && column.fontColor().length() > 0) {
            try {
                style.setTextColor(new Color(Integer.parseInt(column.fontColor().replace("#", ""), 16)));
            } catch (Exception e) {
                throw new ReportProcessingException("Ocorreu um erro ao gerar o relatório: A cor da fonte deve ser informada em HexaDecimal válido Ex: #FF0000", e);
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
                throw new ReportProcessingException("Erro ao gerar relatório", e);
            }

            red.setTextColor(Color.RED);

        }

        columnBuilder.setStyle(style);
    }

    private void processCustomExpression(ReportModel model, ReportColumn column, ColumnBuilder columnBuilder, DynamicReportBuilder reportBuilder) throws ReportProcessingException {
        Class<? extends CustomExpression> customExpressionClass = column.customExpressionClass();

        if (customExpressionClass == null) {
            return;
        }

        CustomExpression customExpression = null;

        try {
            Constructor<? extends CustomExpression> constructor = customExpressionClass.getConstructor(String.class);
            customExpression = constructor.newInstance(column.keypath());
        } catch (Exception exception) {
            throw new ReportProcessingException(exception);
        }

        columnBuilder.setColumnProperty(null);
        columnBuilder.setCustomExpression(customExpression);

        EOEntity entity = model.baseEntity();

        String classname = entity._attributeForPath(column.keypath()).className();

        reportBuilder.addField(column.keypath(), classname);
    }

    private String titleForColumn(EOEntity entity, ReportColumn column) {
        String key = column.title();

        if (key == null) {
            LocalizerKeyGenerator generator = new LocalizerKeyGenerator();

            key = generator.generateKey(entity, column.keypath());
        }

        String title = (String) localizer.valueForKey(key);

        return title == null ? key : title;
    }
}
