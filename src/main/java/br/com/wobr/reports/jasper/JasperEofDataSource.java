package br.com.wobr.reports.jasper;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

import org.apache.commons.lang.Validate;

import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOFetchSpecification;
import com.webobjects.eocontrol.EOQualifier;
import com.webobjects.eocontrol.EOSortOrdering;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSKeyValueCoding;

/**
 * EOF implementation of Jasper data source. This class provides a way to
 * iterate over objects fetched through an <code>EOEditingContext</code>.
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
public class JasperEofDataSource implements JRDataSource
{
	private final EOEditingContext editingContext;

	private final String entityName;

	private int index = -1;

	private final NSArray<String> keyPaths;

	private final EOQualifier qualifier;

	private NSArray<NSDictionary<String, ? extends Object>> resultSet;

	private final NSArray<EOSortOrdering> sortOrderings;

	public JasperEofDataSource(EOEditingContext editingContext, String entityName)
	{
		this(editingContext, entityName, null, null, null);
	}

	public JasperEofDataSource(EOEditingContext editingContext, String entityName, EOQualifier qualifier)
	{
		this(editingContext, entityName, null, qualifier, null);
	}

	public JasperEofDataSource(EOEditingContext editingContext, String entityName, NSArray<String> keyPaths)
	{
		this(editingContext, entityName, keyPaths, null, null);
	}

	public JasperEofDataSource(EOEditingContext editingContext, String entityName, NSArray<String> keyPaths, EOQualifier qualifier)
	{
		this(editingContext, entityName, keyPaths, qualifier, null);
	}

	public JasperEofDataSource(EOEditingContext editingContext, String entityName, NSArray<String> keyPaths, EOQualifier qualifier, NSArray<EOSortOrdering> sortOrderings)
	{
		Validate.notNull(editingContext, "Cannot create the data source with null editing context");
		Validate.notEmpty(entityName, "Cannot create the data source with null or empty entity name");

		this.entityName = entityName;
		this.keyPaths = keyPaths;
		this.qualifier = qualifier;
		this.sortOrderings = sortOrderings;
		this.editingContext = editingContext;
	}

	public Object getFieldValue(JRField field) throws JRException
	{
		NSDictionary<String, ? extends Object> row = resultSet().objectAtIndex(index);

		System.out.println("Field: " + field.getName());

		Object value = row.valueForKeyPath(field.getName());

		System.out.println("Value: " + value);

		if(value == NSKeyValueCoding.NullValue)
		{
			return null;
		}

		return value;
	}

	public boolean next() throws JRException
	{
		index++;

		return index < resultSet().size();
	}

	/**
	 * Fetch records according to the entityName, qualifier and sortOrderings
	 * specified.
	 *
	 * @return The set of objects found
	 */
	@SuppressWarnings("unchecked")
	protected NSArray<NSDictionary<String, ? extends Object>> resultSet()
	{
		if(resultSet == null)
		{
			EOFetchSpecification fetchSpecification = new EOFetchSpecification(entityName, qualifier, sortOrderings);

			fetchSpecification.setFetchesRawRows(true);

			if(keyPaths != null && keyPaths.size() > 0)
			{
				fetchSpecification.setRawRowKeyPaths(keyPaths);
			}

			resultSet = editingContext.objectsWithFetchSpecification(fetchSpecification);

			System.out.println("result set: " + resultSet);
		}

		return resultSet;
	}
}
