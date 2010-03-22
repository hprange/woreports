package br.com.wobr.reports.jasper;

import java.util.Map;

import net.sf.jasperreports.engine.JasperPrint;
import br.com.wobr.reports.AbstractReportProcessor;
import br.com.wobr.reports.Format;
import br.com.wobr.reports.ReportExporter;
import br.com.wobr.reports.ReportModel;
import br.com.wobr.reports.ReportProcessingException;
import br.com.wobr.reports.ReportProcessor;
import br.com.wobr.reports.ReportTemplate;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provider;
import com.google.inject.internal.Nullable;
import com.webobjects.eocontrol.EOQualifier;
import com.webobjects.eocontrol.EOSortOrdering;
import com.webobjects.foundation.NSArray;

/**
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 */
public class JasperReportProcessorForJava extends AbstractReportProcessor
{
	private final Provider<ReportExporter<JasperPrint>> exporter;

	private final Injector injector;

	public JasperReportProcessorForJava(Provider<ReportExporter<JasperPrint>> exporter)
	{
		this(exporter, null, null);
	}

	@Inject
	public JasperReportProcessorForJava(Provider<ReportExporter<JasperPrint>> exporter, Injector injector, @Nullable ReportProcessor nextProcessor)
	{
		super(nextProcessor);

		this.exporter = exporter;
		this.injector = injector;
	}

	@Override
	protected byte[] handleProcessing(Format format, ReportModel model, EOQualifier qualifier, NSArray<EOSortOrdering> additionalSortOrderings,Map<String,Object> params) throws ReportProcessingException
	{
		Class<? extends ReportTemplate<?>> templateClass = model.javaClassTemplate();

		if(templateClass == null)
		{
			return null;
		}

		ReportTemplate<JasperPrint> template = objectForClass(templateClass);

		JasperPrint print = template.build(model);

		return exporter.get().export(print);
	}

	@SuppressWarnings("unchecked")
	ReportTemplate<JasperPrint> objectForClass(Class<? extends ReportTemplate<?>> clazz) throws ReportProcessingException
	{
		return (ReportTemplate<JasperPrint>) injector.getInstance(clazz);
	}
}
