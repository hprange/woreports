package com.woreports.jasper;

import static org.hamcrest.CoreMatchers.is;

import java.util.Collections;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.woreports.jasper.JasperReportInput;
import com.woreports.jasper.stubs.JRStubDataSource;

/**
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 */
public class TestJasperReportInput {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void throwExceptionWhenCreatingInputWithNullParameters() throws Exception {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage(is("The parameters param cannot be null."));

        new JasperReportInput(null, new JRStubDataSource());
    }

    @Test
    public void throwExceptionWhenCreatingInputWithNullDataSource() throws Exception {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage(is("The dataSource param cannot be null."));

        new JasperReportInput(Collections.<String, Object> emptyMap(), null);
    }
}
