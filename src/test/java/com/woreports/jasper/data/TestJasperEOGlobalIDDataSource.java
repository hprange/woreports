package com.woreports.jasper.data;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.woreports.model.Foo;
import com.wounit.rules.MockEditingContext;
import com.wounit.utils.WOUnitEditingContextFactory;

/**
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 */
public class TestJasperEOGlobalIDDataSource {
    private JasperEOGlobaIDDataSource dataSource;

    @Rule
    public MockEditingContext editingContext = new MockEditingContext("Sample");

    @Before
    public void setup() {
        Foo eo = editingContext.createSavedObject(Foo.class);

        dataSource = new JasperEOGlobaIDDataSource(new WOUnitEditingContextFactory(editingContext), editingContext.globalIDForObject(eo));
    }

    @Test
    public void rewindToFirstElement() throws Exception {
        dataSource.next();
        dataSource.next();

        dataSource.moveFirst();

        assertThat(dataSource.next(), is(true));
    }
}
