package com.woreports.jasper.data;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import org.junit.Rule;
import org.junit.Test;

import com.woreports.model.Foo;
import com.wounit.rules.MockEditingContext;

/**
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 */
public class TestJasperEODataSource {
    @Rule
    public MockEditingContext editingContext = new MockEditingContext("Sample");

    @Test
    public void cannotCreateIfEditingContextIsNull() throws Exception {
        try {
            new JasperEODataSource(null, Foo.ENTITY_NAME);

            fail();
        } catch (IllegalArgumentException exception) {
            assertThat(exception.getMessage(), is("Cannot create the data source with null editing context"));
        }
    }

    @Test
    public void cannotCreateIfEntityNameIsNull() throws Exception {
        try {
            new JasperEODataSource(editingContext, null);

            fail();
        } catch (IllegalArgumentException exception) {
            assertThat(exception.getMessage(), is("Cannot create the data source with null or empty entity name"));
        }
    }

    @Test
    public void nextReturnsFalseIfHasNoObjects() throws Exception {
        JasperEODataSource dataSource = new JasperEODataSource(editingContext, Foo.ENTITY_NAME);

        assertThat(dataSource.next(), is(false));
    }

    @Test
    public void nextReturnsTrueIfHasObjects() throws Exception {
        editingContext.createSavedObject(Foo.class);

        JasperEODataSource dataSource = new JasperEODataSource(editingContext, Foo.ENTITY_NAME);

        assertThat(dataSource.next(), is(true));
    }

    @Test
    public void rewindToFirstElement() throws Exception {
        editingContext.createSavedObject(Foo.class);

        JasperEODataSource dataSource = new JasperEODataSource(editingContext, Foo.ENTITY_NAME);

        dataSource.next();
        dataSource.next();

        dataSource.moveFirst();

        assertThat(dataSource.next(), is(true));
    }
}
