package br.com.wobr.reports;

import java.net.URL;

import com.webobjects.eoaccess.EOEntity;
import com.webobjects.eocontrol.EOSortOrdering;
import com.webobjects.foundation.NSArray;

/**
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 */
public interface ReportModel
{
	public EOEntity baseEntity();

	public NSArray<? extends ReportColumn> columns();

	public Class<? extends ReportTemplate<?>> javaClassTemplate();

	public NSArray<String> keyPaths();

	public NSArray<EOSortOrdering> sortOrderings();

	public String subtitle();

	public String title();

	public URL templateLocation();
}
