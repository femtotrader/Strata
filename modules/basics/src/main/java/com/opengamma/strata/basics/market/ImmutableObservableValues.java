/**
 * Copyright (C) 2015 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.strata.basics.market;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

import org.joda.beans.BeanDefinition;
import org.joda.beans.ImmutableBean;
import org.joda.beans.Property;
import org.joda.beans.PropertyDefinition;

import java.util.NoSuchElementException;

import org.joda.beans.Bean;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaProperty;
import org.joda.beans.impl.direct.DirectFieldsBeanBuilder;
import org.joda.beans.impl.direct.DirectMetaBean;
import org.joda.beans.impl.direct.DirectMetaProperty;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;

import com.google.common.collect.ImmutableMap;

/**
 * Implementation of {@link ObservableValues} using a map between {@link ObservableKey} and the value.
 */
@BeanDefinition
public final class ImmutableObservableValues 
    implements ObservableValues, ImmutableBean, Serializable{

  /** Serialization version. */
  private static final long serialVersionUID = 1L;
  
  /**
   * The map containing the keys and values.
   */
  @PropertyDefinition(validate = "notNull")
  private final Map<ObservableKey, Double> values;
  
  /**
   * Obtain an instance.
   * 
   * @param values  the map of key to values
   * @return the instance
   */
  public static ImmutableObservableValues of(Map<ObservableKey, Double> values) {
    return new ImmutableObservableValues(values);
  }

  @Override
  public boolean containsValue(ObservableKey marketDataKey) {
    return values.containsKey(marketDataKey);
  }

  @Override
  public double getValue(ObservableKey marketDataKey) {
    Double v = values.get(marketDataKey);
    if (v == null) {
      throw new RuntimeException("No market value for key " + marketDataKey);
    }
    return v;
  }

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code ImmutableObservableValues}.
   * @return the meta-bean, not null
   */
  public static ImmutableObservableValues.Meta meta() {
    return ImmutableObservableValues.Meta.INSTANCE;
  }

  static {
    JodaBeanUtils.registerMetaBean(ImmutableObservableValues.Meta.INSTANCE);
  }

  /**
   * Returns a builder used to create an instance of the bean.
   * @return the builder, not null
   */
  public static ImmutableObservableValues.Builder builder() {
    return new ImmutableObservableValues.Builder();
  }

  private ImmutableObservableValues(
      Map<ObservableKey, Double> values) {
    JodaBeanUtils.notNull(values, "values");
    this.values = ImmutableMap.copyOf(values);
  }

  @Override
  public ImmutableObservableValues.Meta metaBean() {
    return ImmutableObservableValues.Meta.INSTANCE;
  }

  @Override
  public <R> Property<R> property(String propertyName) {
    return metaBean().<R>metaProperty(propertyName).createProperty(this);
  }

  @Override
  public Set<String> propertyNames() {
    return metaBean().metaPropertyMap().keySet();
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the map containing the keys and values.
   * @return the value of the property, not null
   */
  public Map<ObservableKey, Double> getValues() {
    return values;
  }

  //-----------------------------------------------------------------------
  /**
   * Returns a builder that allows this bean to be mutated.
   * @return the mutable builder, not null
   */
  public Builder toBuilder() {
    return new Builder(this);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      ImmutableObservableValues other = (ImmutableObservableValues) obj;
      return JodaBeanUtils.equal(getValues(), other.getValues());
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = getClass().hashCode();
    hash = hash * 31 + JodaBeanUtils.hashCode(getValues());
    return hash;
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(64);
    buf.append("ImmutableObservableValues{");
    buf.append("values").append('=').append(JodaBeanUtils.toString(getValues()));
    buf.append('}');
    return buf.toString();
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code ImmutableObservableValues}.
   */
  public static final class Meta extends DirectMetaBean {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code values} property.
     */
    @SuppressWarnings({"unchecked", "rawtypes" })
    private final MetaProperty<Map<ObservableKey, Double>> values = DirectMetaProperty.ofImmutable(
        this, "values", ImmutableObservableValues.class, (Class) Map.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> metaPropertyMap$ = new DirectMetaPropertyMap(
        this, null,
        "values");

    /**
     * Restricted constructor.
     */
    private Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case -823812830:  // values
          return values;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public ImmutableObservableValues.Builder builder() {
      return new ImmutableObservableValues.Builder();
    }

    @Override
    public Class<? extends ImmutableObservableValues> beanType() {
      return ImmutableObservableValues.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code values} property.
     * @return the meta-property, not null
     */
    public MetaProperty<Map<ObservableKey, Double>> values() {
      return values;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case -823812830:  // values
          return ((ImmutableObservableValues) bean).getValues();
      }
      return super.propertyGet(bean, propertyName, quiet);
    }

    @Override
    protected void propertySet(Bean bean, String propertyName, Object newValue, boolean quiet) {
      metaProperty(propertyName);
      if (quiet) {
        return;
      }
      throw new UnsupportedOperationException("Property cannot be written: " + propertyName);
    }

  }

  //-----------------------------------------------------------------------
  /**
   * The bean-builder for {@code ImmutableObservableValues}.
   */
  public static final class Builder extends DirectFieldsBeanBuilder<ImmutableObservableValues> {

    private Map<ObservableKey, Double> values = ImmutableMap.of();

    /**
     * Restricted constructor.
     */
    private Builder() {
    }

    /**
     * Restricted copy constructor.
     * @param beanToCopy  the bean to copy from, not null
     */
    private Builder(ImmutableObservableValues beanToCopy) {
      this.values = ImmutableMap.copyOf(beanToCopy.getValues());
    }

    //-----------------------------------------------------------------------
    @Override
    public Object get(String propertyName) {
      switch (propertyName.hashCode()) {
        case -823812830:  // values
          return values;
        default:
          throw new NoSuchElementException("Unknown property: " + propertyName);
      }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Builder set(String propertyName, Object newValue) {
      switch (propertyName.hashCode()) {
        case -823812830:  // values
          this.values = (Map<ObservableKey, Double>) newValue;
          break;
        default:
          throw new NoSuchElementException("Unknown property: " + propertyName);
      }
      return this;
    }

    @Override
    public Builder set(MetaProperty<?> property, Object value) {
      super.set(property, value);
      return this;
    }

    @Override
    public Builder setString(String propertyName, String value) {
      setString(meta().metaProperty(propertyName), value);
      return this;
    }

    @Override
    public Builder setString(MetaProperty<?> property, String value) {
      super.setString(property, value);
      return this;
    }

    @Override
    public Builder setAll(Map<String, ? extends Object> propertyValueMap) {
      super.setAll(propertyValueMap);
      return this;
    }

    @Override
    public ImmutableObservableValues build() {
      return new ImmutableObservableValues(
          values);
    }

    //-----------------------------------------------------------------------
    /**
     * Sets the map containing the keys and values.
     * @param values  the new value, not null
     * @return this, for chaining, not null
     */
    public Builder values(Map<ObservableKey, Double> values) {
      JodaBeanUtils.notNull(values, "values");
      this.values = values;
      return this;
    }

    //-----------------------------------------------------------------------
    @Override
    public String toString() {
      StringBuilder buf = new StringBuilder(64);
      buf.append("ImmutableObservableValues.Builder{");
      buf.append("values").append('=').append(JodaBeanUtils.toString(values));
      buf.append('}');
      return buf.toString();
    }

  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}
