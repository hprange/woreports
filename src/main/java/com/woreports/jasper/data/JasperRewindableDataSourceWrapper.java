package com.woreports.jasper.data;

import static java.util.Objects.requireNonNull;

import org.apache.commons.lang.UnhandledException;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRRewindableDataSource;

/**
 * This class acts as a data source wrapper and rewinds the provided data source upon creation. That feature is very
 * useful when a report contains a subreport inside the detail band. By passing the data source wrapped by this class to
 * the subreport will move the pointer to the first record for each iteration of the detail band.
 *
 * @author <a href="mailto:hprange@gmail.com.br">Henrique Prange</a>
 */
public class JasperRewindableDataSourceWrapper implements JRRewindableDataSource {
    private final JRRewindableDataSource dataSource;

    /**
     * Wraps the provided data source moving the pointer to the first position before initial processing.
     *
     * @param dataSource
     *            A rewindable data source.
     */
    public JasperRewindableDataSourceWrapper(JRRewindableDataSource dataSource) {
        requireNonNull(dataSource, "The data source parameter cannot be null");

        this.dataSource = dataSource;

        try {
            this.dataSource.moveFirst();
        } catch (JRException exception) {
            throw new UnhandledException("An error has occurred while trying to rewind the data source", exception);
        }
    }

    @Override
    public boolean next() throws JRException {
        return dataSource.next();
    }

    @Override
    public Object getFieldValue(JRField jrField) throws JRException {
        return dataSource.getFieldValue(jrField);
    }

    @Override
    public void moveFirst() throws JRException {
        dataSource.moveFirst();
    }
}
