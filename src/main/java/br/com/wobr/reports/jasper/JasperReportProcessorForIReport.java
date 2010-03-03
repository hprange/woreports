package br.com.wobr.reports.jasper;

import java.net.MalformedURLException;
import java.net.URL;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import ar.com.fdvs.dj.core.DynamicJasperHelper;
import ar.com.fdvs.dj.core.layout.ClassicLayoutManager;
import ar.com.fdvs.dj.domain.DynamicReport;
import ar.com.fdvs.dj.domain.builders.DynamicReportBuilder;
import br.com.wobr.reports.AbstractReportProcessor;
import br.com.wobr.reports.Format;
import br.com.wobr.reports.ReportModel;
import br.com.wobr.reports.ReportProcessingException;
import br.com.wobr.reports.ReportProcessor;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOQualifier;
import com.webobjects.eocontrol.EOSortOrdering;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSMutableArray;

public class JasperReportProcessorForIReport extends AbstractReportProcessor {

	private final Provider<EOEditingContext> editingContextProvider;

	@Inject
	public JasperReportProcessorForIReport(
			final Provider<EOEditingContext> editingContextProvider) {
		super(null);
		this.editingContextProvider = editingContextProvider;

	}

	@Override
	protected byte[] handleProcessing(Format format, ReportModel model,
			EOQualifier qualifier,
			NSArray<EOSortOrdering> additionalSortOrderings)
			throws ReportProcessingException {
		// TODO Auto-generated method stub

		byte[] data = null;
		JasperPrint print = null;

		try {

			// recupera a URL
			URL url = model.iReportTemplate();

			// Le a url e transforma em JasperReport
			JasperReport jasperReport = (JasperReport) JRLoader.loadObject(url);

			System.out.println("URL: " + url);

			JRField[] fields = jasperReport.getFields();

			NSMutableArray<String> keypaths = new NSMutableArray<String>();

			for (JRField field : fields) {
				keypaths.add(field.getName());
			}

			JRDataSource dataSource = new JasperEofDataSource(
					editingContextProvider.get(), model.baseEntity().name(),
					keypaths, qualifier, model.sortOrderings()
							.arrayByAddingObjectsFromArray(
									additionalSortOrderings));

			// Cria o JasperPrint
			print = JasperFillManager
					.fillReport(jasperReport, null, dataSource);

			data = JasperExportManager.exportReportToPdf(print);

		} catch (JRException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return data;
	}

}
