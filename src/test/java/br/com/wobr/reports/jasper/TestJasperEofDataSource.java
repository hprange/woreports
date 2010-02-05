package br.com.wobr.reports.jasper;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSDictionary;

/**
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 */
@RunWith(value = MockitoJUnitRunner.class)
public class TestJasperEofDataSource
{
	protected static final String ENTITY_NAME = "entity";

	@Mock
	protected EOEditingContext mockEditingContext;

	@Test
	public void cannotCreateIfEditingContextIsNull() throws Exception
	{
		try
		{
			new JasperEofDataSource(null, ENTITY_NAME);

			fail();
		}
		catch(IllegalArgumentException exception)
		{
			assertThat(exception.getMessage(), is("Cannot create the data source with null editing context"));
		}
	}

	@Test
	public void cannotCreateIfEntityNameIsNull() throws Exception
	{
		try
		{
			new JasperEofDataSource(mockEditingContext, null);

			fail();
		}
		catch(IllegalArgumentException exception)
		{
			assertThat(exception.getMessage(), is("Cannot create the data source with null or empty entity name"));
		}
	}

	@Test
	public void nextReturnsFalseIfHasNoObjects() throws Exception
	{
		JasperEofDataSource dataSource = Mockito.spy(new JasperEofDataSource(mockEditingContext, ENTITY_NAME));

		Mockito.doReturn(NSArray.emptyArray()).when(dataSource).resultSet();

		assertThat(dataSource.next(), is(false));
	}

	@Test
	public void nextReturnsTrueIfHasObjects() throws Exception
	{
		JasperEofDataSource dataSource = Mockito.spy(new JasperEofDataSource(mockEditingContext, ENTITY_NAME));

		Mockito.doReturn(new NSArray<NSDictionary<String, ? extends Object>>(new NSDictionary<String, Object>())).when(dataSource).resultSet();

		assertThat(dataSource.next(), is(true));
	}
}
