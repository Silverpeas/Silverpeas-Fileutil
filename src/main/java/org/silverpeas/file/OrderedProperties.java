/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.silverpeas.file;

import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Properties;
import java.util.Set;

/**
 * @author ehugonnet
 */
public class OrderedProperties extends Properties {

  private static final long serialVersionUID = 76138647684130582L;
  protected Set<Object> orderedKeys = new LinkedHashSet<Object>();

  /**
   * Creates an empty property list with no default values.
   */
  public OrderedProperties() {
    super();
  }

  /**
   * Creates an empty property list with the specified defaults.
   * @param defaults the defaults.
   */
  public OrderedProperties(Properties defaults) {
    super(defaults);
  }

  @Override
  public synchronized Object put(Object key, Object value) {
    orderedKeys.add(key);
    return super.put(key, value);
  }

  @Override
  public synchronized Object remove(Object key) {
    orderedKeys.remove(key);
    return super.remove(key);
  }

  @Override
  public synchronized Enumeration<Object> keys() {
    return Collections.enumeration(orderedKeys);
  }

}
