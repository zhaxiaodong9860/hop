package org.apache.hop.metadata.serializer.json.person.interest;

import org.apache.hop.metadata.api.HopMetadataProperty;

import java.util.Objects;

public class Cooking implements IInterest {

  @HopMetadataProperty
  private InterestType type;

  @HopMetadataProperty
  private String name;

  @HopMetadataProperty
  private String description;

  public Cooking() {
  }

  public Cooking( String name, String description ) {
    this.type = InterestType.Food;
    this.name = name;
    this.description = description;
  }

  @Override public boolean equals( Object o ) {
    if ( this == o ) {
      return true;
    }
    if ( o == null || getClass() != o.getClass() ) {
      return false;
    }
    Cooking cooking = (Cooking) o;
    return type == cooking.type &&
      Objects.equals( name, cooking.name ) &&
      Objects.equals( description, cooking.description );
  }

  @Override public int hashCode() {
    return Objects.hash( type, name, description );
  }

  /**
   * Gets name
   *
   * @return value of name
   */
  @Override public String getName() {
    return name;
  }

  /**
   * @param name The name to set
   */
  public void setName( String name ) {
    this.name = name;
  }

  /**
   * Gets description
   *
   * @return value of description
   */
  @Override public String getDescription() {
    return description;
  }

  /**
   * @param description The description to set
   */
  public void setDescription( String description ) {
    this.description = description;
  }

  /**
   * Gets type
   *
   * @return value of type
   */
  public InterestType getType() {
    return type;
  }

  /**
   * @param type The type to set
   */
  public void setType( InterestType type ) {
    this.type = type;
  }
}
