package com.woreports.jasper.data;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSDictionary;
import com.woreports.model.Foo;
import com.wounit.rules.MockEditingContext;

/**
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 */
@RunWith(value = MockitoJUnitRunner.class)
public class TestJasperEODataSource {
    protected static final String ENTITY_NAME = "entity";

    @Rule
    public MockEditingContext mockEditingContext = new MockEditingContext("Sample");

    @Test
    public void cannotCreateIfEditingContextIsNull() throws Exception {
        try {
            new JasperEODataSource(null, ENTITY_NAME);

            fail();
        } catch (IllegalArgumentException exception) {
            assertThat(exception.getMessage(), is("Cannot create the data source with null editing context"));
        }
    }

    @Test
    public void cannotCreateIfEntityNameIsNull() throws Exception {
        try {
            new JasperEODataSource(mockEditingContext, null);

            fail();
        } catch (IllegalArgumentException exception) {
            assertThat(exception.getMessage(), is("Cannot create the data source with null or empty entity name"));
        }
    }

    @Test
    public void nextReturnsFalseIfHasNoObjects() throws Exception {
        JasperEODataSource dataSource = Mockito.spy(new JasperEODataSource(mockEditingContext, ENTITY_NAME));

        Mockito.doReturn(NSArray.emptyArray()).when(dataSource).resultSet();

        assertThat(dataSource.next(), is(false));
    }

    @Test
    public void nextReturnsTrueIfHasObjects() throws Exception {
        JasperEODataSource dataSource = Mockito.spy(new JasperEODataSource(mockEditingContext, ENTITY_NAME));

        Mockito.doReturn(new NSArray<NSDictionary<String, ? extends Object>>(new NSDictionary<String, Object>())).when(dataSource).resultSet();

        assertThat(dataSource.next(), is(true));
    }

    @Test
    public void rewindToFirstElement() throws Exception {
        mockEditingContext.createSavedObject(Foo.class);

        JasperEODataSource dataSource = new JasperEODataSource(mockEditingContext, Foo.ENTITY_NAME);

        dataSource.next();
        dataSource.next();

        dataSource.moveFirst();

        assertThat(dataSource.next(), is(true));
    }
}
