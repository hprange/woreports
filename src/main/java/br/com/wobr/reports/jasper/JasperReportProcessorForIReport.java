package br.com.wobr.reports.jasper;

import java.net.URL;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import br.com.wobr.reports.AbstractReportProcessor;
import br.com.wobr.reports.Format;
import br.com.wobr.reports.ReportModel;
import br.com.wobr.reports.ReportProcessingException;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOQualifier;
import com.webobjects.eocontrol.EOSortOrdering;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSMutableArray;

/**
 * @author <a href="mailto:alexandre.parreira@doit.com.br">Alexandre
 *         Parreira</a>
 */
public class JasperReportProcessorForIReport extends AbstractReportProcessor
{
	private final Provider<EOEditingContext> editingContextProvider;

	@Inject
	public JasperReportProcessorForIReport( final Provider<EOEditingContext> editingContextProvider )
	{
		super( null );

		this.editingContextProvider = editingContextProvider;
	}

	@Override
	protected byte[] handleProcessing( final Format format, final ReportModel model, final EOQualifier qualifier, final NSArray<EOSortOrdering> additionalSortOrderings ) throws ReportProcessingException
	{
		byte[] data = null;

		JasperPrint print = null;

		try
		{
			// recupera a URL
			URL url = model.iReportTemplate();

			// Le a url e transforma em JasperReport
			JasperReport jasperReport = (JasperReport) JRLoader.loadObject( url );

			JRField[] fields = jasperReport.getFields();

			NSMutableArray<String> keypaths = new NSMutableArray<String>();

			for( JRField field : fields )
			{
				keypaths.add( field.getName() );
			}

			JRDataSource dataSource = new JasperEofDataSource( editingContextProvider.get(), model.baseEntity().name(), keypaths, qualifier, model.sortOrderings().arrayByAddingObjectsFromArray( additionalSortOrderings ) );

			// Cria o JasperPrint
			print = JasperFillManager.fillReport( jasperReport, null, dataSource );

			data = JasperExportManager.exportReportToPdf( print );

		}
		catch( JRException exception )
		{
			new ReportProcessingException( exception );
		}

		return data;
	}
}
