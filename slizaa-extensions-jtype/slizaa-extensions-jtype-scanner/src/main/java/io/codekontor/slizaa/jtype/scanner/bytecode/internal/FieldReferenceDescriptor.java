/**
 * slizaa-extensions-jtype-scanner - Slizaa Static Software Analysis Tools
 * Copyright © 2019 Code-Kontor GmbH and others (slizaa@codekontor.io)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package io.codekontor.slizaa.jtype.scanner.bytecode.internal;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * <p>
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class FieldReferenceDescriptor {

  /** - */
  private String  _ownerTypeName;

  /** - */
  private String  _fieldName;

  /** - */
  private String  _fieldType;

  /** - */
  private boolean _isStatic;

  /**
   * <p>
   * Creates a new instance of type {@link FieldReferenceDescriptor}.
   * </p>
   * 
   * @param ownerTypeName
   * @param fieldName
   * @param fieldType
   * @param isStatic
   */
  public FieldReferenceDescriptor(String ownerTypeName, String fieldName, String fieldType, boolean isStatic) {

    checkNotNull(ownerTypeName);
    checkNotNull(fieldName);
    checkNotNull(fieldType);

    //
    _ownerTypeName = ownerTypeName;
    _fieldName = fieldName;
    _fieldType = fieldType;
    _isStatic = isStatic;
  }

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  public String getOwnerTypeName() {
    return _ownerTypeName;
  }

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  public String getFieldName() {
    return _fieldName;
  }

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  public String getFieldType() {
    return _fieldType;
  }

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  public boolean isStatic() {
    return _isStatic;
  }

  /**
   * <p>
   * </p>
   *
   * @return
   */
  public boolean isPrimitive() {
    return _fieldType.equals("boolean") | _fieldType.equals("byte") | _fieldType.equals("char")
        | _fieldType.equals("double") | _fieldType.equals("float") | _fieldType.equals("int")
        | _fieldType.equals("long") | _fieldType.equals("short");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((_fieldName == null) ? 0 : _fieldName.hashCode());
    result = prime * result + ((_fieldType == null) ? 0 : _fieldType.hashCode());
    result = prime * result + (_isStatic ? 1231 : 1237);
    result = prime * result + ((_ownerTypeName == null) ? 0 : _ownerTypeName.hashCode());
    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    FieldReferenceDescriptor other = (FieldReferenceDescriptor) obj;
    if (_fieldName == null) {
      if (other._fieldName != null)
        return false;
    } else if (!_fieldName.equals(other._fieldName))
      return false;
    if (_fieldType == null) {
      if (other._fieldType != null)
        return false;
    } else if (!_fieldType.equals(other._fieldType))
      return false;
    if (_isStatic != other._isStatic)
      return false;
    if (_ownerTypeName == null) {
      if (other._ownerTypeName != null)
        return false;
    } else if (!_ownerTypeName.equals(other._ownerTypeName))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "FieldDescriptor [_ownerTypeName=" + _ownerTypeName + ", _fieldName=" + _fieldName + ", _fieldType="
        + _fieldType + ", _isStatic=" + _isStatic + "]";
  }
}
