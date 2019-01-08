package com.woreports.custom;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.inject.Provider;
import com.woreports.api.Alignment;
import com.woreports.api.ReportColumn;
import com.woreports.api.ReportModel;

import ar.com.fdvs.dj.domain.CustomExpression;

@RunWith(MockitoJUnitRunner.class)
public class TestJasperCustomPatternAppllier {
    private JasperReportColumnCustomizer columnCustomizer;

    @Mock
    private Provider<String> patternProvider;

    @Before
    public void setup() {
        columnCustomizer = new JasperReportColumnCustomizer(null);
    }

    @Test
    public void returnNullWhenColumnIsNull() throws Exception {
        assertThat(columnCustomizer.patternFor(null), nullValue());
    }

    @Test
    public void doNothingWhenColumnPatternIsNull() throws Exception {
        assertThat(columnCustomizer.patternFor(null), nullValue());
    }

    @Test
    public void doNothingWhenPatternDoesNotMatchSpecialKey() throws Exception {
        ReportColumn column = new CustomReportColumn("dd/mm/yyyy");

        assertThat(columnCustomizer.patternFor(column), is("dd/mm/yyyy"));
    }

    @Test
    public void doNothingWhenPatternProviderIsNull() throws Exception {
        columnCustomizer = new JasperReportColumnCustomizer(null);

        ReportColumn column = new CustomReportColumn("${dateFormat}");

        assertThat(columnCustomizer.patternFor(column), is("${dateFormat}"));
    }

    @Test
    public void returnCustomPatternWhenSpecialKeyIsInformed() throws Exception {
        when(patternProvider.get()).thenReturn("dd-MMM-yyyy");

        columnCustomizer = new JasperReportColumnCustomizer(patternProvider);

        ReportColumn column = new CustomReportColumn("${dateFormat}");

        assertThat(columnCustomizer.patternFor(column), is("dd-MMM-yyyy"));
    }

    class CustomReportColumn implements ReportColumn {
        private final String pattern;

        public CustomReportColumn(String pattern) {
            this.pattern = pattern;
        }

        @Override
        public Alignment alignment() {
            return null;
        }

        @Override
        public Class<? extends CustomExpression> customExpressionClass() {
            return null;
        }

        @Override
        public String fontColor() {
            return null;
        }

        @Override
        public String fontSize() {
            return null;
        }

        @Override
        public Boolean groupedBy() {
            return null;
        }

        @Override
        public Boolean hidden() {
            return null;
        }

        @Override
        public String keypath() {
            return null;
        }

        @Override
        public ReportModel model() {
            return null;
        }

        @Override
        public String pattern() {
            return pattern;
        }

        @Override
        public String title() {
            return null;
        }

        @Override
        public Integer width() {
            return null;
        }
    }
}
