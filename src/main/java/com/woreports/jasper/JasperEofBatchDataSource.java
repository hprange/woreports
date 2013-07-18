package com.woreports.jasper;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

import org.apache.commons.lang.Validate;

import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOEnterpriseObject;
import com.webobjects.eocontrol.EOQualifier;
import com.webobjects.eocontrol.EOSortOrdering;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSKeyValueCoding;

import er.extensions.eof.ERXFetchSpecification;
import er.extensions.eof.ERXFetchSpecificationBatchIterator;

/**
 * EOF implementation of Jasper data source. This class provides a way to
 * iterate over objects fetched through an <code>EOEditingContext</code>. The
 * objects are fetched in batches of 1000 rows.
 * <p>
 * You can map Jasper fields to attributes in your model. You must follow the
 * same pattern used for keypath navigation.
 * <p>
 * If you have a model like this:
 * 
 * <pre>
 * EntityA        ->      EntityB
 *   |-attributeA           |-attributeB
 *   `-relationshipToB
 * </pre>
 * 
 * You can map the following fields:
 * <ul>
 * <li>attributeA</li>
 * <li>relationshipToB.attributeB</li>
 * </ul>
 * <p>
 * The raw row option is activated while fetching data to reduce memory
 * consumption.
 * 
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 */
public class JasperEofBatchDataSource implements JRDataSource {
    private final ERXFetchSpecificationBatchIterator iterator;

    private NSDictionary<String, Object> row;

    public JasperEofBatchDataSource(EOEditingContext editingContext, String entityName) {
        this(editingContext, entityName, null, null, null);
    }

    public JasperEofBatchDataSource(EOEditingContext editingContext, String entityName, EOQualifier qualifier) {
        this(editingContext, entityName, null, qualifier, null);
    }

    public JasperEofBatchDataSource(EOEditingContext editingContext, String entityName, NSArray<String> keyPaths) {
        this(editingContext, entityName, keyPaths, null, null);
    }

    public JasperEofBatchDataSource(EOEditingContext editingContext, String entityName, NSArray<String> keyPaths, EOQualifier qualifier) {
        this(editingContext, entityName, keyPaths, qualifier, null);
    }

    public JasperEofBatchDataSource(EOEditingContext editingContext, String entityName, NSArray<String> keyPaths, EOQualifier qualifier, NSArray<EOSortOrdering> sortOrderings) {
        Validate.notNull(editingContext, "Cannot create the data source with null editing context");
        Validate.notEmpty(entityName, "Cannot create the data source with null or empty entity name");

        ERXFetchSpecification<EOEnterpriseObject> fetchSpecification = new ERXFetchSpecification<EOEnterpriseObject>(entityName, qualifier, sortOrderings);

        fetchSpecification.setFetchesRawRows(true);

        if (keyPaths != null && keyPaths.size() > 0) {
            fetchSpecification.setRawRowKeyPaths(keyPaths);
        }

        iterator = new ERXFetchSpecificationBatchIterator(fetchSpecification, editingContext, 1000);

    }

    @Override
    public Object getFieldValue(JRField field) throws JRException {
        Object value = row.valueForKeyPath(field.getName());

        return value == NSKeyValueCoding.NullValue ? null : value;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean next() throws JRException {
        boolean hasNext = iterator.hasNext();

        if (hasNext) {
            row = (NSDictionary<String, Object>) iterator.next();
        }

        return hasNext;
    }
}
