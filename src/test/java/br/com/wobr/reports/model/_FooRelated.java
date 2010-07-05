// $LastChangedRevision: 5810 $ DO NOT EDIT.  Make changes to FooRelated.java instead.
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
public abstract class _FooRelated extends  EOGenericRecord {
	public static final String ENTITY_NAME = "FooRelated";

	// Attributes
	public static final String BAR_KEY = "bar";

	// Relationships

  private static Logger LOG = Logger.getLogger(_FooRelated.class);

  public FooRelated localInstanceIn(EOEditingContext editingContext) {
    FooRelated localInstance = (FooRelated)EOUtilities.localInstanceOfObject(editingContext, this);
    if (localInstance == null) {
      throw new IllegalStateException("You attempted to localInstance " + this + ", which has not yet committed.");
    }
    return localInstance;
  }

  public String bar() {
    return (String) storedValueForKey("bar");
  }

  public void setBar(String value) {
    if (_FooRelated.LOG.isDebugEnabled()) {
    	_FooRelated.LOG.debug( "updating bar from " + bar() + " to " + value);
    }
    takeStoredValueForKey(value, "bar");
  }


  public static FooRelated createFooRelated(EOEditingContext editingContext) {
    FooRelated eo = (FooRelated) EOUtilities.createAndInsertInstance(editingContext, _FooRelated.ENTITY_NAME);    
    return eo;
  }

  public static NSArray<FooRelated> fetchAllFooRelateds(EOEditingContext editingContext) {
    return _FooRelated.fetchAllFooRelateds(editingContext, null);
  }

  public static NSArray<FooRelated> fetchAllFooRelateds(EOEditingContext editingContext, NSArray<EOSortOrdering> sortOrderings) {
    return _FooRelated.fetchFooRelateds(editingContext, null, sortOrderings);
  }

  public static NSArray<FooRelated> fetchFooRelateds(EOEditingContext editingContext, EOQualifier qualifier, NSArray<EOSortOrdering> sortOrderings) {
    EOFetchSpecification fetchSpec = new EOFetchSpecification(_FooRelated.ENTITY_NAME, qualifier, sortOrderings);
    fetchSpec.setIsDeep(true);
    NSArray<FooRelated> eoObjects = (NSArray<FooRelated>)editingContext.objectsWithFetchSpecification(fetchSpec);
    return eoObjects;
  }

  public static FooRelated fetchFooRelated(EOEditingContext editingContext, String keyName, Object value) {
    return _FooRelated.fetchFooRelated(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static FooRelated fetchFooRelated(EOEditingContext editingContext, EOQualifier qualifier) {
    NSArray<FooRelated> eoObjects = _FooRelated.fetchFooRelateds(editingContext, qualifier, null);
    FooRelated eoObject;
    int count = eoObjects.count();
    if (count == 0) {
      eoObject = null;
    }
    else if (count == 1) {
      eoObject = (FooRelated)eoObjects.objectAtIndex(0);
    }
    else {
      throw new IllegalStateException("There was more than one FooRelated that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static FooRelated fetchRequiredFooRelated(EOEditingContext editingContext, String keyName, Object value) {
    return _FooRelated.fetchRequiredFooRelated(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static FooRelated fetchRequiredFooRelated(EOEditingContext editingContext, EOQualifier qualifier) {
    FooRelated eoObject = _FooRelated.fetchFooRelated(editingContext, qualifier);
    if (eoObject == null) {
      throw new NoSuchElementException("There was no FooRelated that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static FooRelated localInstanceIn(EOEditingContext editingContext, FooRelated eo) {
    FooRelated localInstance = (eo == null) ? null : (FooRelated)EOUtilities.localInstanceOfObject(editingContext, eo);
    if (localInstance == null && eo != null) {
      throw new IllegalStateException("You attempted to localInstance " + eo + ", which has not yet committed.");
    }
    return localInstance;
  }
}
