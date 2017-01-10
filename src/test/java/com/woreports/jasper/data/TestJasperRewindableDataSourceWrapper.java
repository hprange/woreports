package com.woreports.jasper.data;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;

import org.apache.commons.lang.UnhandledException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRRewindableDataSource;

/**
 * @author <a href="mailto:hprange@gmail.com.br">Henrique Prange</a>
 */
@RunWith(MockitoJUnitRunner.class)
public class TestJasperRewindableDataSourceWrapper {
    @Mock
    private JRRewindableDataSource dataSource;

    private JasperRewindableDataSourceWrapper wrapper;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setup() {
        wrapper = new JasperRewindableDataSourceWrapper(dataSource);
    }

    @Test
    public void rewindDataSourceWhenCreatingDataSourceWrapper() throws Exception {
        verify(dataSource).moveFirst();
    }

    @Test
    public void delegateMethodInvocationsToDataSourceWhenCallingWrapperMethods() throws Exception {
        reset(dataSource);

        wrapper.moveFirst();

        verify(dataSource).moveFirst();

        wrapper.next();

        verify(dataSource).next();

        wrapper.getFieldValue(null);

        verify(dataSource).getFieldValue(null);
    }

    @Test
    public void throwExceptionWhenWrappingNullDataSource() throws Exception {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage(is("The data source parameter cannot be null"));

        new JasperRewindableDataSourceWrapper(null);
    }

    @Test
    public void throwChainedRuntimeExceptionWhenMovingToFirstDuringCreation() throws Exception {
        JRException exception = new JRException("error");

        doThrow(exception).when(dataSource).moveFirst();

        thrown.expect(UnhandledException.class);
        thrown.expectMessage(is("An error has occurred while trying to rewind the data source"));
        thrown.expectCause(is(exception));

        new JasperRewindableDataSourceWrapper(dataSource);
    }
}
