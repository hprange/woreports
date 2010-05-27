package br.com.wobr.reports.jasper;

import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSKeyValueCodingAdditions;

/**
 * Implementation of <code>JRDataSource</code> to support
 * <code>EOEnterpriseObject</code>s and <code>NSKeyValueCoding</code> interface.
 * This class provides a way to iterate over <code>EOEnterpriseObject</code>s
 * and collect the required data through keyValue coding pattern.
 * <p>
 * You can map Jasper fields to properties and relationships from the
 * <code>EOEnterpriseObject<code>s provided. You must follow the
 * same pattern used for keyPath navigation.
 * <p>
 * If you have two objects like these:
 * 
 * <pre>
 * Foo            ->       FooRelated
 *  |-bar                    `-relatedBar
 *  `-relatedRelationship
 * </pre>
 * 
 * You can map the following fields:
 * <ul>
 * <li>bar</li>
 * <li>relatedRelationship.relatedBar</li>
 * </ul>
 * <p>
 * This class also supports objects that do not implement the NSKeyValueCoding interface.
 * 
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 */
public class JasperKeyValueDataSource implements JRDataSource
{
	private int index = -1;

	private final List<? extends Object> objects;

	public JasperKeyValueDataSource( final List<? extends Object> objects )
	{
		if( objects == null )
		{
			throw new IllegalArgumentException( "The array of objects cannot be null" );
		}

		this.objects = objects;
	}

	public JasperKeyValueDataSource( final Object... objects )
	{
		this( new NSArray<Object>( objects ) );
	}

	public Object getFieldValue( final JRField field ) throws JRException
	{
		Object object = objects.get( index );

		return NSKeyValueCodingAdditions.DefaultImplementation.valueForKeyPath( object, field.getName() );
	}

	public boolean next() throws JRException
	{
		index++;

		return index < objects.size();
	}
}
