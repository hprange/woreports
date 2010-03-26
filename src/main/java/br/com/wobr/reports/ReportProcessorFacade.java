package br.com.wobr.reports;

import java.util.Map;
import java.util.Set;

import net.sf.jasperreports.engine.JRDataSource;
import br.com.wobr.reports.jasper.JasperEofDataSource;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOQualifier;
import com.webobjects.eocontrol.EOSortOrdering;
import com.webobjects.foundation.NSArray;

/**
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 */
public class ReportProcessorFacade implements ReportProcessor
{
	private final Set<AbstractReportProcessor> processors;

	private final Provider<EOEditingContext> editingContextProvider;

	@Inject
	public ReportProcessorFacade( @Named( "ForFacade" ) final Set<AbstractReportProcessor> processors, final Provider<EOEditingContext> editingContextProvider )
	{
		this.processors = processors;
		this.editingContextProvider = editingContextProvider;
	}

	public byte[] process( final Format format, final ReportModel model, final Map<String, Object> parameters ) throws ReportProcessingException
	{
		return process( format, model, parameters, (EOQualifier) null );
	}

	public byte[] process( final Format format, final ReportModel model, final Map<String, Object> parameters, final EOQualifier qualifier ) throws ReportProcessingException
	{
		return process( format, model, parameters, qualifier, NSArray.<EOSortOrdering> emptyArray() );
	}

	public byte[] process( final Format format, final ReportModel model, final Map<String, Object> parameters, final EOQualifier qualifier, final NSArray<EOSortOrdering> sortOrderings ) throws ReportProcessingException
	{
		JRDataSource dataSource = new JasperEofDataSource( editingContextProvider.get(), model.baseEntity().name(), model.keyPaths(), qualifier, model.sortOrderings().arrayByAddingObjectsFromArray( sortOrderings ) );

		return process( format, model, parameters, dataSource );
	}

	public byte[] process( final Format format, final ReportModel model, final Map<String, Object> parameters, final JRDataSource dataSource ) throws ReportProcessingException
	{
		byte[] result = null;

		for( AbstractReportProcessor processor : processors )
		{
			result = processor.process( format, model, parameters, dataSource );

			if( result != null )
			{
				break;
			}
		}

		return result;
	}
}
