package com.woreports.impl;

import java.net.URL;

import com.webobjects.eoaccess.EOEntity;
import com.webobjects.eocontrol.EOSortOrdering;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSMutableArray;
import com.woreports.api.ReportColumn;
import com.woreports.api.ReportModel;

/**
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 */
public class DefaultReportModel implements ReportModel {
    private EOEntity baseEntity;

    private final NSMutableArray<ReportColumn> columns = new NSMutableArray<ReportColumn>();

    private final NSMutableArray<EOSortOrdering> sortOrderings = new NSMutableArray<EOSortOrdering>();

    private String subtitle;

    private URL templateLocation;

    private String title;

    public boolean addColumn(ReportColumn column) {
        return columns.add(column);
    }

    public boolean addSortOrdering(EOSortOrdering sortOrdering) {
        return sortOrderings.add(sortOrdering);
    }

    @Override
    public EOEntity baseEntity() {
        return baseEntity;
    }

    @Override
    public NSArray<? extends ReportColumn> columns() {
        return columns;
    }

    @Override
    @SuppressWarnings("unchecked")
    public NSArray<String> keyPaths() {
        return (NSArray<String>) columns.valueForKeyPath("keypath");
    }

    public void setBaseEntity(EOEntity baseEntity) {
        this.baseEntity = baseEntity;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public void setTemplateLocation(URL templateLocation) {
        this.templateLocation = templateLocation;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public NSArray<EOSortOrdering> sortOrderings() {
        return sortOrderings;
    }

    @Override
    public String subtitle() {
        return subtitle;
    }

    @Override
    public URL templateLocation() {
        return templateLocation;
    }

    @Override
    public String title() {
        return title;
    }
}
