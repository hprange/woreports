package br.com.wobr.reports.jasper;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import net.sf.jasperreports.engine.JRField;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import br.com.wobr.reports.model.Foo;
import br.com.wobr.reports.model.FooRelated;
import br.com.wobr.reports.model.StubObject;
import br.com.wobr.unittest.rules.TemporaryEditingContextProvider;

import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOEnterpriseObject;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSKeyValueCoding;

/**
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 */
@RunWith( MockitoJUnitRunner.class )
public class TestJasperValueForKeyDataSource
{
	@Rule
	public final TemporaryEditingContextProvider editingContextProvider = new TemporaryEditingContextProvider( "Sample" );

	@Rule
	public final ExpectedException thrown = ExpectedException.none();

	protected Foo mockEO;

	@Mock
	protected JRField mockField;

	protected JasperValueForKeyDataSource dataSource;

	private EOEditingContext editingContext;

	@Mock
	private StubObject mockObject;

	@Test
	public void exceptionIfCannotFindTheKey() throws Exception
	{
		Mockito.when( mockField.getName() ).thenReturn( "invalidKey" );

		dataSource.next();

		thrown.expect( NSKeyValueCoding.UnknownKeyException.class );

		dataSource.getFieldValue( mockField );
	}

	@Test
	public void exceptionIfNullNSArrayProvided()
	{
		thrown.expect( IllegalArgumentException.class );
		thrown.expectMessage( is( "The array of objects cannot be null" ) );

		new JasperValueForKeyDataSource( (NSArray<EOEnterpriseObject>) null );
	}

	@Test
	public void getFieldValueForKey() throws Exception
	{
		mockEO.setBar( "result" );

		Mockito.when( mockField.getName() ).thenReturn( "bar" );

		dataSource.next();

		Object result = dataSource.getFieldValue( mockField );

		assertThat( (String) result, is( "result" ) );
	}

	@Test
	public void getFieldValueForKeyOnSimpleObject() throws Exception
	{
		when( mockObject.foo() ).thenReturn( "the result" );

		dataSource = new JasperValueForKeyDataSource( mockObject );

		Mockito.when( mockField.getName() ).thenReturn( "foo" );

		dataSource.next();

		Object result = dataSource.getFieldValue( mockField );

		assertThat( (String) result, is( "the result" ) );
	}

	@Test
	public void getFieldValueForRelationship() throws Exception
	{
		FooRelated mockRelatedObject = FooRelated.createFooRelated( editingContext );

		mockRelatedObject.setBar( "expected result" );

		mockEO.setRelatedRelationship( mockRelatedObject );

		Mockito.when( mockField.getName() ).thenReturn( "related.bar" );

		dataSource.next();

		String result = (String) dataSource.getFieldValue( mockField );

		assertThat( result, is( "expected result" ) );
	}

	@Test
	public void getFieldValueForRelationshipForSimpleObject() throws Exception
	{
		StubObject mockRelatedObject = mock( StubObject.class );

		when( mockRelatedObject.foo() ).thenReturn( "test" );

		when( mockObject.related() ).thenReturn( mockRelatedObject );

		dataSource = new JasperValueForKeyDataSource( mockObject );

		Mockito.when( mockField.getName() ).thenReturn( "related.foo" );

		dataSource.next();

		String result = (String) dataSource.getFieldValue( mockField );

		assertThat( result, is( "test" ) );
	}

	@Test
	public void iterateOverMultipleRecords() throws Exception
	{
		dataSource = new JasperValueForKeyDataSource( mockEO, mockEO, mockEO );

		assertThat( dataSource.next(), is( true ) );
		assertThat( dataSource.next(), is( true ) );
		assertThat( dataSource.next(), is( true ) );
		assertThat( dataSource.next(), is( false ) );
	}

	@Test
	public void iterateOverMultipleRecordsFromNSArray() throws Exception
	{
		dataSource = new JasperValueForKeyDataSource( new NSArray<EOEnterpriseObject>( new EOEnterpriseObject[] { mockEO, mockEO } ) );

		assertThat( dataSource.next(), is( true ) );
		assertThat( dataSource.next(), is( true ) );
		assertThat( dataSource.next(), is( false ) );
	}

	@Test
	public void iterateOverOneRecord() throws Exception
	{
		assertThat( dataSource.next(), is( true ) );
		assertThat( dataSource.next(), is( false ) );
	}

	@Before
	public void setup()
	{
		editingContext = editingContextProvider.editingContext();

		mockEO = Foo.createFoo( editingContext );

		dataSource = new JasperValueForKeyDataSource( mockEO );
	}
}
