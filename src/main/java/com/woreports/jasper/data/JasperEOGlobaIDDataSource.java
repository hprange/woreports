package com.woreports.jasper.data;

import static er.extensions.eof.ERXEOControlUtilities.objectsForGlobalIDs;

import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOGlobalID;
import com.webobjects.foundation.NSArray;

import er.extensions.eof.ERXEC;
import er.extensions.eof.ERXEC.Factory;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRRewindableDataSource;

/**
 * This class implements the {@code JRRewindableDataSource} interface, allowing the iteration to go back to the first
 * element.
 *
 * @author <a href="mailto:hprange@gmail.com.br">Henrique Prange</a>
 */
public class JasperEOGlobaIDDataSource implements JRDataSource, JRRewindableDataSource {
    private JasperKeyValueDataSource dataSource;
    private final Factory editingContextFactory;
    private final NSArray<EOGlobalID> globalIds;

    public JasperEOGlobaIDDataSource(EOGlobalID... globalIds) {
        this(new NSArray<EOGlobalID>(globalIds));
    }

    public JasperEOGlobaIDDataSource(NSArray<EOGlobalID> globalIds) {
        this(ERXEC._factory(), globalIds);
    }

    public JasperEOGlobaIDDataSource(ERXEC.Factory editingContextFactory, EOGlobalID... globalIds) {
        this(editingContextFactory, new NSArray<EOGlobalID>(globalIds));
    }

    public JasperEOGlobaIDDataSource(ERXEC.Factory editingContextFactory, NSArray<EOGlobalID> globalIds) {
        this.editingContextFactory = editingContextFactory;
        this.globalIds = globalIds;
    }

    @SuppressWarnings("unchecked")
    private JasperKeyValueDataSource dataSource() {
        if (dataSource == null) {
            EOEditingContext editingContext = editingContextFactory._newEditingContext();

            dataSource = new JasperKeyValueDataSource(objectsForGlobalIDs(editingContext, globalIds));
        }

        return dataSource;
    }

    @Override
    public Object getFieldValue(JRField field) throws JRException {
        return dataSource().getFieldValue(field);
    }

    @Override
    public void moveFirst() throws JRException {
        dataSource().moveFirst();
    }

    @Override
    public boolean next() throws JRException {
        return dataSource().next();
    }
}
