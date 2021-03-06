package com.woreports.api;

import java.net.URL;

import com.webobjects.eoaccess.EOEntity;
import com.webobjects.eocontrol.EOSortOrdering;
import com.webobjects.foundation.NSArray;

/**
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 */
public interface ReportModel {
    public EOEntity baseEntity();

    public NSArray<? extends ReportColumn> columns();

    public NSArray<String> keyPaths();

    public NSArray<EOSortOrdering> sortOrderings();

    public String subtitle();

    public URL templateLocation();

    public String title();
}
