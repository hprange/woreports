package com.woreports.api;

import java.util.Map;

import com.webobjects.eocontrol.EOQualifier;
import com.webobjects.eocontrol.EOSortOrdering;
import com.webobjects.foundation.NSArray;

public interface ReportRecipe<T> {
    ReportRecipe<T> filledBy(Map<String, Object> parameters, T dataSource);

    ReportRecipe<T> filledBy(Map<String, Object> parameters, EOQualifier qualifier, NSArray<EOSortOrdering> sortOrderings);

    byte[] generateReportIn(Format format) throws ReportProcessingException;
}
