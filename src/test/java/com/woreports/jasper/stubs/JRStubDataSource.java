package com.woreports.jasper.stubs;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

/**
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 */
public class JRStubDataSource implements JRDataSource {
    @Override
    public boolean next() throws JRException {
        return false;
    }

    @Override
    public Object getFieldValue(JRField field) throws JRException {
        return null;
    }
}
