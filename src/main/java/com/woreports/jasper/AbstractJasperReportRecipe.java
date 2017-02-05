package com.woreports.jasper;

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOQualifier;
import com.webobjects.eocontrol.EOSortOrdering;
import com.webobjects.foundation.NSArray;
import com.woreports.api.Format;
import com.woreports.api.ReportModel;
import com.woreports.api.ReportProcessingException;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRVirtualizer;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.fill.JRSwapFileVirtualizer;
import net.sf.jasperreports.engine.util.JRSwapFile;

/**
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 */
public abstract class AbstractJasperReportRecipe implements JasperReportRecipe {
    protected final EOEditingContext editingContext;
    protected final ReportModel model;

    private final List<JasperReportInput> inputs = new ArrayList<>();

    public AbstractJasperReportRecipe(EOEditingContext editingContext, ReportModel model) {
        this.model = model;
        this.editingContext = editingContext;
    }

    @Override
    public abstract JasperReportRecipe filledBy(Map<String, Object> parameters, EOQualifier qualifier, NSArray<EOSortOrdering> sortOrderings);

    @Override
    public JasperReportRecipe filledBy(Map<String, Object> parameters, JRDataSource dataSource) {
        inputs.add(new JasperReportInput(parameters, dataSource));

        return this;
    }

    @Override
    public final byte[] generateReportIn(Format format) throws ReportProcessingException {
        if (inputs.isEmpty()) {
            JasperPrint print = filler(format).fillReport(new HashMap<String, Object>(), new JREmptyDataSource(0));

            return new JasperReportGenerator(asList(print)).generateReport(format);
        }

        List<JasperPrint> prints = new ArrayList<>(inputs.size());

        for (JasperReportInput input : inputs) {
            JRSwapFile swapFile = new JRSwapFile(FileUtils.getTempDirectoryPath(), 1024, 1024);

            JRVirtualizer virtualizer = new JRSwapFileVirtualizer(2, swapFile, true);

            Map<String, Object> parameters = input.parameters();

            parameters.put(JRParameter.REPORT_VIRTUALIZER, virtualizer);

            JasperPrint print;

            try {
                print = filler(format).fillReport(parameters, input.dataSource());
            } catch (Exception exception) {
                throw new ReportProcessingException(exception);
            }

            prints.add(print);
        }

        return new JasperReportGenerator(prints).generateReport(format);
    }

    protected abstract JasperFiller filler(Format format);
}
