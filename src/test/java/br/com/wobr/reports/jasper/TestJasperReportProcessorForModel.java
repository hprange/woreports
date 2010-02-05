package br.com.wobr.reports.jasper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import ar.com.fdvs.dj.domain.builders.DynamicReportBuilder;
import br.com.wobr.reports.ReportModel;

import com.google.inject.Provider;
import com.webobjects.eoaccess.EOEntity;

/**
 * TODO: Test PDF generation
 * <p>
 * TODO: Test Excel generation
 * 
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 */
@RunWith(value = MockitoJUnitRunner.class)
public class TestJasperReportProcessorForModel
{
	@Mock
	protected Provider<DynamicReportBuilder> mockBuilderProvider;

	@Mock
	protected EOEntity mockEntity;

	@Mock
	protected ReportModel mockModel;

	@Test
	public void generatePdfForModel() throws Exception
	{
		// Mockito.when(mockModel.baseEntity()).thenReturn(mockEntity);
		//
		// JasperReportProcessorForModel processor = new
		// JasperReportProcessorForModel(null, null, mockBuilderProvider, null,
		// null);
		//
		// byte[] result = processor.handleProcessing(mockModel, null, null);
		//
		// InputStream inputStream =
		// getClass().getResourceAsStream("/sample-model-report.pdf");
		//
		// byte[] expectedBytes = inputStream.toString().getBytes();

		// assertArrayEquals(expectedBytes, result);
	}
}
