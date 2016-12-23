package com.woreports.jasper.data;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.webobjects.eocontrol.EOEnterpriseObject;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSKeyValueCoding;
import com.webobjects.foundation.NSMutableDictionary;
import com.woreports.model.Foo;
import com.woreports.model.FooRelated;
import com.woreports.model.StubObject;
import com.wounit.rules.MockEditingContext;

import net.sf.jasperreports.engine.JRField;

/**
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 */
@RunWith(MockitoJUnitRunner.class)
public class TestJasperKeyValueDataSource {
    protected JasperKeyValueDataSource dataSource;

    @Rule
    public final MockEditingContext editingContext = new MockEditingContext("Sample");

    protected Foo mockEO;

    @Mock
    protected JRField mockField;

    @Mock
    private StubObject mockObject;

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    @Test
    public void exceptionIfCannotFindTheKey() throws Exception {
        Mockito.when(mockField.getName()).thenReturn("invalidKey");

        dataSource.next();

        thrown.expect(NSKeyValueCoding.UnknownKeyException.class);

        dataSource.getFieldValue(mockField);
    }

    @Test
    public void exceptionIfNullNSArrayProvided() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(is("The array of objects cannot be null"));

        new JasperKeyValueDataSource((NSArray<EOEnterpriseObject>) null);
    }

    @Test
    public void getFieldValueForKey() throws Exception {
        mockEO.setBar("result");

        Mockito.when(mockField.getName()).thenReturn("bar");

        dataSource.next();

        Object result = dataSource.getFieldValue(mockField);

        assertThat((String) result, is("result"));
    }

    @Test
    public void getFieldValueForKeyOnSimpleObject() throws Exception {
        when(mockObject.foo()).thenReturn("the result");

        dataSource = new JasperKeyValueDataSource(mockObject);

        Mockito.when(mockField.getName()).thenReturn("foo");

        dataSource.next();

        Object result = dataSource.getFieldValue(mockField);

        assertThat((String) result, is("the result"));
    }

    @Test
    public void getFieldValueForKeypathInDictionary() throws Exception {
        NSDictionary<String, Object> dictionary = new NSMutableDictionary<String, Object>();

        dictionary.put("key.with.path", "result");

        dataSource = new JasperKeyValueDataSource(dictionary);

        dataSource.next();

        Mockito.when(mockField.getName()).thenReturn("key.with.path");

        Object result = dataSource.getFieldValue(mockField);

        assertThat((String) result, is("result"));
    }

    @Test
    public void getFieldValueForRelationship() throws Exception {
        FooRelated mockRelatedObject = FooRelated.createFooRelated(editingContext);

        mockRelatedObject.setBar("expected result");

        mockEO.setRelatedRelationship(mockRelatedObject);

        Mockito.when(mockField.getName()).thenReturn("related.bar");

        dataSource.next();

        String result = (String) dataSource.getFieldValue(mockField);

        assertThat(result, is("expected result"));
    }

    @Test
    public void getFieldValueForRelationshipForSimpleObject() throws Exception {
        StubObject mockRelatedObject = mock(StubObject.class);

        when(mockRelatedObject.foo()).thenReturn("test");

        when(mockObject.related()).thenReturn(mockRelatedObject);

        dataSource = new JasperKeyValueDataSource(mockObject);

        Mockito.when(mockField.getName()).thenReturn("related.foo");

        dataSource.next();

        String result = (String) dataSource.getFieldValue(mockField);

        assertThat(result, is("test"));
    }

    @Test
    public void getNullFieldValueForKeypathInDictionary() throws Exception {
        NSDictionary<String, Object> dictionary = new NSMutableDictionary<String, Object>();

        dictionary.put("key", NSKeyValueCoding.NullValue);

        dataSource = new JasperKeyValueDataSource(dictionary);

        dataSource.next();

        Mockito.when(mockField.getName()).thenReturn("key");

        Object result = dataSource.getFieldValue(mockField);

        assertThat(result, nullValue());
    }

    @Test
    public void iterateOverMultipleRecords() throws Exception {
        dataSource = new JasperKeyValueDataSource(mockEO, mockEO, mockEO);

        assertThat(dataSource.next(), is(true));
        assertThat(dataSource.next(), is(true));
        assertThat(dataSource.next(), is(true));
        assertThat(dataSource.next(), is(false));
    }

    @Test
    public void iterateOverMultipleRecordsFromNSArray() throws Exception {
        dataSource = new JasperKeyValueDataSource(new NSArray<EOEnterpriseObject>(new EOEnterpriseObject[] { mockEO, mockEO }));

        assertThat(dataSource.next(), is(true));
        assertThat(dataSource.next(), is(true));
        assertThat(dataSource.next(), is(false));
    }

    @Test
    public void iterateOverOneRecord() throws Exception {
        assertThat(dataSource.next(), is(true));
        assertThat(dataSource.next(), is(false));
    }

    @Test
    public void rewindToFirstElement() throws Exception {
        dataSource.next();
        dataSource.next();

        dataSource.moveFirst();

        assertThat(dataSource.next(), is(true));
    }

    @Before
    public void setup() {
        mockEO = Foo.createFoo(editingContext);

        dataSource = new JasperKeyValueDataSource(mockEO);
    }
}
