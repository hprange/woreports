package br.com.wobr.reports.d2w;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.UnhandledException;

import br.com.wobr.reports.api.Format;
import br.com.wobr.reports.api.ReportProcessingException;
import br.com.wobr.reports.api.ReportProcessor;
import br.com.wobr.reports.impl.DefaultReportColumn;
import br.com.wobr.reports.impl.DefaultReportModel;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WOResponse;
import com.webobjects.directtoweb.D2WContext;
import com.webobjects.eocontrol.EOQualifier;
import com.webobjects.foundation.NSArray;

import er.directtoweb.pages.ERD2WPage;

/**
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 */
public class ReportPage extends ERD2WPage
{
	@Inject
	private ReportProcessor reportProcessor;

	private EOQualifier restrictingQualifier;

	public ReportPage(WOContext context)
	{
		super(context);

		// TODO: We must improve the integration with Guice
		Injector injector = (Injector) application().valueForKey("injector");

		injector.injectMembers(this);
	}

	@Override
	public void appendToResponse(WOResponse response, WOContext context)
	{
		super.appendToResponse(response, context);

		DefaultReportModel model = new DefaultReportModel();

		model.setBaseEntity(entity());

		D2WContext d2wContext = d2wContext();

		model.setTitle((String) d2wContext.valueForKey("title"));

		Format format = formatForString((String) d2wContext.valueForKey("format"));

		for(String key : displayPropertyKeys())
		{
			d2wContext.setPropertyKey(key);
			d2wContext.computeDerivedValues();

			DefaultReportColumn column = new DefaultReportColumn();

			column.setKeypath(key);
			column.setModel(model);
			column.setTitle(d2wContext.displayNameForProperty());
			column.setPattern((String) d2wContext.valueForKey("pattern"));

			String width = (String) d2wContext.valueForKey("width");

			if(!StringUtils.isBlank(width))
			{
				column.setWidth(Integer.valueOf(width));
			}

			model.addColumn(column);
		}

		// TODO: The qualifier should be provided by the query page that calls
		// this page.
		EOQualifier qualifier = restrictingQualifier();

		// TODO: Why not grab parameters based on a rule?
		Map<String, Object> parameters = new HashMap<String, Object>();

		String fileName = StringUtils.defaultIfEmpty((String) d2wContext.valueForKey("fileName"), "report");

		try
		{
			byte[] data = reportProcessor.process(format, model, parameters, qualifier);

			response.disableClientCaching();
			response.removeHeadersForKey("Cache-Control");
			response.removeHeadersForKey("pragma");
			response.setHeader(format.mimeType(), "content-type");
			response.setHeader("inline; attachment; filename=\"" + fileName + "." + format.toString().toLowerCase() + "\"", "content-disposition");
			response.setHeader(Integer.toString(data.length), "content-length");
			response.setContent(data);
		}
		catch(ReportProcessingException exception)
		{
			throw new UnhandledException(exception);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public NSArray<String> displayPropertyKeys()
	{
		return super.displayPropertyKeys();
	}

	private Format formatForString(String text)
	{
		if(text == null)
		{
			return Format.PDF;
		}

		return Format.valueOf(text);
	}

	public EOQualifier restrictingQualifier()
	{
		return restrictingQualifier;
	}

	public void setRestrictingQualifier(EOQualifier restrictingQualifier)
	{
		this.restrictingQualifier = restrictingQualifier;
	}
}
