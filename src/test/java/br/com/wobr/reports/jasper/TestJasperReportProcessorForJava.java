package br.com.wobr.reports.jasper;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import net.sf.jasperreports.engine.JasperPrint;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import br.com.wobr.reports.Format;
import br.com.wobr.reports.ReportExporter;
import br.com.wobr.reports.ReportModel;
import br.com.wobr.reports.ReportTemplate;

/**
 * TODO: Generate empty PDF
 * 
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 */
@RunWith( MockitoJUnitRunner.class )
public class TestJasperReportProcessorForJava
{
	public static byte[] inputStreamToBytes( final InputStream in ) throws IOException
	{
		ByteArrayOutputStream out = new ByteArrayOutputStream( 1024 );
		byte[] buffer = new byte[1024];
		int len;

		while( ( len = in.read( buffer ) ) >= 0 )
		{
			out.write( buffer, 0, len );
		}

		in.close();
		out.close();

		return out.toByteArray();
	}

	@Mock
	protected ReportExporter<JasperPrint> mockExporter;

	@Mock
	protected ReportModel mockModel;

	@Mock
	protected ReportTemplate<JasperPrint> mockTemplate;

	private JasperReportProcessorForJava processor;

	@Test
	public void generateEmptyPDF() throws Exception
	{
		Mockito.doReturn( mockTemplate.getClass() ).when( mockModel ).javaClassTemplate();
		Mockito.doReturn( mockTemplate ).when( processor ).objectForClass( (Class<? extends ReportTemplate<?>>) mockTemplate.getClass() );

		processor.handleProcessing( Format.PDF, mockModel, null, null );

		Mockito.verify( mockTemplate ).build( mockModel );
		Mockito.verify( mockExporter ).export( Mockito.any( JasperPrint.class ) );
	}

	@Test
	public void returnNullIfNoJavaTemplateDefined() throws Exception
	{
		Mockito.doReturn( null ).when( mockModel ).javaClassTemplate();

		byte[] result = processor.handleProcessing( Format.PDF, mockModel, null, null );

		assertThat( result, nullValue() );
	}

	@Before
	public void setup()
	{
		processor = Mockito.spy( new JasperReportProcessorForJava( mockExporter ) );
	}
}
