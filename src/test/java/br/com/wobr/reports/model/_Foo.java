// $LastChangedRevision: 5810 $ DO NOT EDIT.  Make changes to Foo.java instead.
package br.com.wobr.reports.model;

import java.util.NoSuchElementException;

import org.apache.log4j.Logger;

import com.webobjects.eoaccess.EOUtilities;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOFetchSpecification;
import com.webobjects.eocontrol.EOGenericRecord;
import com.webobjects.eocontrol.EOKeyValueQualifier;
import com.webobjects.eocontrol.EOQualifier;
import com.webobjects.eocontrol.EOSortOrdering;
import com.webobjects.foundation.NSArray;

@SuppressWarnings("all")
public abstract class _Foo extends  EOGenericRecord {
	public static final String ENTITY_NAME = "Foo";

	// Attributes
	public static final String BAR_KEY = "bar";

	// Relationships
	public static final String RELATED_KEY = "related";

  private static Logger LOG = Logger.getLogger(_Foo.class);

  public Foo localInstanceIn(EOEditingContext editingContext) {
    Foo localInstance = (Foo)EOUtilities.localInstanceOfObject(editingContext, this);
    if (localInstance == null) {
      throw new IllegalStateException("You attempted to localInstance " + this + ", which has not yet committed.");
    }
    return localInstance;
  }

  public String bar() {
    return (String) storedValueForKey("bar");
  }

  public void setBar(String value) {
    if (_Foo.LOG.isDebugEnabled()) {
    	_Foo.LOG.debug( "updating bar from " + bar() + " to " + value);
    }
    takeStoredValueForKey(value, "bar");
  }

  public br.com.wobr.reports.model.FooRelated related() {
    return (br.com.wobr.reports.model.FooRelated)storedValueForKey("related");
  }

  public void setRelatedRelationship(br.com.wobr.reports.model.FooRelated value) {
    if (_Foo.LOG.isDebugEnabled()) {
      _Foo.LOG.debug("updating related from " + related() + " to " + value);
    }
    if (value == null) {
    	br.com.wobr.reports.model.FooRelated oldValue = related();
    	if (oldValue != null) {
    		removeObjectFromBothSidesOfRelationshipWithKey(oldValue, "related");
      }
    } else {
    	addObjectToBothSidesOfRelationshipWithKey(value, "related");
    }
  }
  

  public static Foo createFoo(EOEditingContext editingContext) {
    Foo eo = (Foo) EOUtilities.createAndInsertInstance(editingContext, _Foo.ENTITY_NAME);    
    return eo;
  }

  public static NSArray<Foo> fetchAllFoos(EOEditingContext editingContext) {
    return _Foo.fetchAllFoos(editingContext, null);
  }

  public static NSArray<Foo> fetchAllFoos(EOEditingContext editingContext, NSArray<EOSortOrdering> sortOrderings) {
    return _Foo.fetchFoos(editingContext, null, sortOrderings);
  }

  public static NSArray<Foo> fetchFoos(EOEditingContext editingContext, EOQualifier qualifier, NSArray<EOSortOrdering> sortOrderings) {
    EOFetchSpecification fetchSpec = new EOFetchSpecification(_Foo.ENTITY_NAME, qualifier, sortOrderings);
    fetchSpec.setIsDeep(true);
    NSArray<Foo> eoObjects = (NSArray<Foo>)editingContext.objectsWithFetchSpecification(fetchSpec);
    return eoObjects;
  }

  public static Foo fetchFoo(EOEditingContext editingContext, String keyName, Object value) {
    return _Foo.fetchFoo(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static Foo fetchFoo(EOEditingContext editingContext, EOQualifier qualifier) {
    NSArray<Foo> eoObjects = _Foo.fetchFoos(editingContext, qualifier, null);
    Foo eoObject;
    int count = eoObjects.count();
    if (count == 0) {
      eoObject = null;
    }
    else if (count == 1) {
      eoObject = (Foo)eoObjects.objectAtIndex(0);
    }
    else {
      throw new IllegalStateException("There was more than one Foo that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static Foo fetchRequiredFoo(EOEditingContext editingContext, String keyName, Object value) {
    return _Foo.fetchRequiredFoo(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static Foo fetchRequiredFoo(EOEditingContext editingContext, EOQualifier qualifier) {
    Foo eoObject = _Foo.fetchFoo(editingContext, qualifier);
    if (eoObject == null) {
      throw new NoSuchElementException("There was no Foo that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static Foo localInstanceIn(EOEditingContext editingContext, Foo eo) {
    Foo localInstance = (eo == null) ? null : (Foo)EOUtilities.localInstanceOfObject(editingContext, eo);
    if (localInstance == null && eo != null) {
      throw new IllegalStateException("You attempted to localInstance " + eo + ", which has not yet committed.");
    }
    return localInstance;
  }
}
