package com.woreports.jasper;

import java.util.Map;

import com.webobjects.eocontrol.EOQualifier;
import com.webobjects.eocontrol.EOSortOrdering;
import com.webobjects.foundation.NSArray;
import com.woreports.api.ReportRecipe;

import net.sf.jasperreports.engine.JRDataSource;

/**
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 */
public interface JasperReportRecipe extends ReportRecipe<JRDataSource> {
    @Override
    JasperReportRecipe filledBy(Map<String, Object> parameters, JRDataSource dataSource);

    @Override
    JasperReportRecipe filledBy(Map<String, Object> parameters, EOQualifier qualifier, NSArray<EOSortOrdering> sortOrderings);
}
