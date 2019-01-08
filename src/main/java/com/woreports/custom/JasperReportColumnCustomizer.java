package com.woreports.custom;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;
import com.woreports.api.ReportColumn;

/**
 * This class can be used to personalize the pattern field of a column.
 * A String provider should be injected, which will provide the custom pattern if present.
 *
 *
 * @author <a href="mailto:luizalfredocb@gmail.com">Luiz Alfredo</a>
 *
 */
public class JasperReportColumnCustomizer {

    private static final String DATE_FORMAT = "${dateFormat}";
    private final Provider<String> datePatternProvider;

    @Inject
    public JasperReportColumnCustomizer(@Named("datePattern") Provider<String> datePatternProvider) {
        this.datePatternProvider = datePatternProvider;
    }

    /**
     *
     * @param column
     * @return This method returns a pattern to be applied, if a <code>Provider<String></code> named
     *         <code>@Named("datePattern")</code> is informed, then its value will be returned. In other cases, the
     *         informed column pattern will be returned.
     */
    public String patternFor(ReportColumn column) {
        if (column == null) {
            return null;
        }

        if (column.pattern() != null && datePatternProvider != null && column.pattern().equals(DATE_FORMAT)) {
            return datePatternProvider.get();
        }

        return column.pattern();

    }

}
