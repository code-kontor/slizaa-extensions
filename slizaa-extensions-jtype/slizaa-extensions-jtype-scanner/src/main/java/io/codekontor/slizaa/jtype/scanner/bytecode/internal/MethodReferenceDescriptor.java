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

public class MethodReferenceDescriptor {

  /** - */
  private String  _ownerTypeName;

  /** - */
  private String  _methodName;

  /** - */
  private String  _methodSignature;

  /** - */
  private boolean _isInterface;

  /**
   * @param ownerTypeName
   * @param methodName
   * @param methodSignature
   * @param isStatic
   */
  public MethodReferenceDescriptor(String ownerTypeName, String methodName, String methodSignature, boolean isInterface) {
    checkNotNull(ownerTypeName);
    checkNotNull(methodName);
    checkNotNull(methodSignature);
    checkNotNull(isInterface);

    _ownerTypeName = ownerTypeName;
    _methodName = methodName;
    _methodSignature = methodSignature;
    _isInterface = isInterface;
  }

  public String getOwnerTypeName() {
    return _ownerTypeName;
  }

  public String getMethodName() {
    return _methodName;
  }

  public String getMethodSignature() {
    return _methodSignature;
  }

  public boolean isInterface() {
    return _isInterface;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (_isInterface ? 1231 : 1237);
    result = prime * result + ((_methodName == null) ? 0 : _methodName.hashCode());
    result = prime * result + ((_methodSignature == null) ? 0 : _methodSignature.hashCode());
    result = prime * result + ((_ownerTypeName == null) ? 0 : _ownerTypeName.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    MethodReferenceDescriptor other = (MethodReferenceDescriptor) obj;
    if (_isInterface != other._isInterface)
      return false;
    if (_methodName == null) {
      if (other._methodName != null)
        return false;
    } else if (!_methodName.equals(other._methodName))
      return false;
    if (_methodSignature == null) {
      if (other._methodSignature != null)
        return false;
    } else if (!_methodSignature.equals(other._methodSignature))
      return false;
    if (_ownerTypeName == null) {
      if (other._ownerTypeName != null)
        return false;
    } else if (!_ownerTypeName.equals(other._ownerTypeName))
      return false;
    return true;
  }
}
