package br.com.wobr.reports.jasper;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
import ar.com.fdvs.dj.core.DynamicJasperHelper;
import ar.com.fdvs.dj.core.layout.ClassicLayoutManager;
import ar.com.fdvs.dj.domain.Style;
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

import er.extensions.localization.ERXLocalizer;

public class JasperReportProcessorForIReport extends AbstractReportProcessor {
	

	private final Provider<EOEditingContext> editingContextProvider;


	@Inject
	public JasperReportProcessorForIReport(final Provider<EOEditingContext> editingContextProvider,ReportProcessor nextProcessor) {
		super(nextProcessor);
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
			
			//JRDataSource dataSource = new EofDataSource(AccountingEntry.ENTITY_NAME, 
				//										new NSArray<String>(new String[] { AccountingEntry.CATEGORY_FIRST_LEVEL_KEY + "." + PermanentTag.VALUE_KEY, AccountingEntry.CATEGORY_SECOND_LEVEL_KEY + "." + PermanentTag.VALUE_KEY, AccountingEntry.CATEGORY_THIRD_LEVEL_KEY + "." + PermanentTag.VALUE_KEY, AccountingEntry.DATE_KEY, AccountingEntry.DATE_OF_ISSUE_KEY, AccountingEntry.DUE_DATE_KEY, AccountingEntry.DESCRIPTION_KEY, AccountingEntry.EXTERNAL_DOCUMENT_KEY, AccountingEntry.INTERNAL_DOCUMENT_KEY, AccountingEntry.BENEFICIARY_KEY + "." + Party.FULL_NAME_KEY, AccountingEntry.PROJECT_KEY + "." + Project.NAME_KEY, AccountingEntry.BALANCE_KEY }), qualifier, ERXS.ascInsensitives(AccountingEntry.CATEGORY_FIRST_LEVEL_KEY + "." + PermanentTag.TAG_KEY + "." + Tag.ORDER_KEY).then(ERXS.ascInsensitive(AccountingEntry.CATEGORY_SECOND_LEVEL_KEY + "." + PermanentTag.TAG_KEY + "." + Tag.ORDER_KEY)).then(ERXS.ascInsensitive(AccountingEntry.CATEGORY_THIRD_LEVEL_KEY + "." + PermanentTag.TAG_KEY + "." + Tag.ORDER_KEY)).then(ERXS.ascInsensitive(AccountingEntry.DATE_KEY)));
			JRDataSource dataSource = new JasperEofDataSource( editingContextProvider.get(), model.baseEntity().name(), model.keyPaths(), qualifier, model.sortOrderings().arrayByAddingObjectsFromArray( additionalSortOrderings ) );
		
		try {
			print = DynamicJasperHelper.generateJasperPrint(null, new ClassicLayoutManager(), dataSource);
			data = JasperExportManager.exportReportToPdf(null);
		} catch (JRException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return data;
	}


}
