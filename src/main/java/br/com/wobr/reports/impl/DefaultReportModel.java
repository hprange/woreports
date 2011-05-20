package br.com.wobr.reports.impl;

import java.net.URL;

import br.com.wobr.reports.api.ReportColumn;
import br.com.wobr.reports.api.ReportModel;
import br.com.wobr.reports.api.ReportTemplate;

import com.webobjects.eoaccess.EOEntity;
import com.webobjects.eocontrol.EOSortOrdering;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSMutableArray;

/**
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 */
public class DefaultReportModel implements ReportModel
{
	private EOEntity baseEntity;

	private final NSMutableArray<ReportColumn> columns = new NSMutableArray<ReportColumn>();

	private Class<? extends ReportTemplate<?>> javaClassTemplate;

	private final NSMutableArray<EOSortOrdering> sortOrderings = new NSMutableArray<EOSortOrdering>();

	private String subtitle;

	private URL templateLocation;

	private String title;

	public boolean addColumn(ReportColumn column)
	{
		return columns.add(column);
	}

	public boolean addSortOrdering(EOSortOrdering sortOrdering)
	{
		return sortOrderings.add(sortOrdering);
	}

	public EOEntity baseEntity()
	{
		return baseEntity;
	}

	public NSArray<? extends ReportColumn> columns()
	{
		return columns;
	}

	public Class<? extends ReportTemplate<?>> javaClassTemplate()
	{
		return javaClassTemplate;
	}

	public NSArray<String> keyPaths()
	{
		return (NSArray<String>) columns.valueForKeyPath("keypath");
	}

	public void setBaseEntity(EOEntity baseEntity)
	{
		this.baseEntity = baseEntity;
	}

	public void setJavaClassTemplate(Class<? extends ReportTemplate<?>> javaClassTemplate)
	{
		this.javaClassTemplate = javaClassTemplate;
	}

	public void setSubtitle(String subtitle)
	{
		this.subtitle = subtitle;
	}

	public void setTemplateLocation(URL templateLocation)
	{
		this.templateLocation = templateLocation;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public NSArray<EOSortOrdering> sortOrderings()
	{
		return sortOrderings;
	}

	public String subtitle()
	{
		return subtitle;
	}

	public URL templateLocation()
	{
		return templateLocation;
	}

	public String title()
	{
		return title;
	}
}
