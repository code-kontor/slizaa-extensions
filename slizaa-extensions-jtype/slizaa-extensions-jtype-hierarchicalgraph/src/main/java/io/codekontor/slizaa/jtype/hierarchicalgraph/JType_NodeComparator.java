/**
 * slizaa-extensions-jtype-hierarchicalgraph - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.jtype.hierarchicalgraph;

import io.codekontor.slizaa.hierarchicalgraph.core.model.HGNode;
import io.codekontor.slizaa.hierarchicalgraph.core.model.spi.INodeComparator;
import io.codekontor.slizaa.hierarchicalgraph.graphdb.model.GraphDbNodeSource;

public class JType_NodeComparator implements INodeComparator {

  /**
   * {@inheritDoc}
   */
  @Override
  public int category(Object element) {

    //
    if (!(hasGraphDbNodeSource(element))) {
      return 0;
    }

    //
    if (hasLabel(element, "Directory")) {
      if (hasLabel(element, "Package")) {
        return 20;
      } else {
        return 50;
      }
    }

    //
    if (hasLabel(element, "Resource")) {
      return 30;
    }

    //
    if (hasLabel(element, "Type")) {
      return 10;
    } else if (hasLabel(element, "Field")) {
      return "true".equalsIgnoreCase(getProperty(element, "static")) ? 10 :30;
    } else if (hasLabel(element, "Method")) {
      return "true".equalsIgnoreCase(getProperty(element, "static")) ? 20 :40;
    }

    //
    return 1;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int compare(Object node1, Object node2) {

    //
    if (!(hasGraphDbNodeSource(node1) || hasGraphDbNodeSource(node2))) {
      return 0;
    }

    //
    if (hasLabel(node1, node2, "Field") || hasLabel(node1, node2, "Method") || hasLabel(node1, node2, "Type")) {
      return compareProperties(node1, node2, "name");
    }

    //
    if (hasLabel(node1, node2, "Directory")) {
      return compareProperties(node1, node2, "fqn");
    }

    
    //
    if (hasLabel(node1, node2, "Resource")) {
      return compareProperties(node1, node2, "name");
    }

    //
    if (hasLabel(node1, node2, "Module")) {

      // handle <<Missing Types>>
      if ("<<Missing Types>>".equalsIgnoreCase(getProperty(node1, "name"))) {
        return 1000;
      }
      if ("<<Missing Types>>".equalsIgnoreCase(getProperty(node1, "name"))) {
        return -1000;
      }

      return compareProperties(node1, node2, "name");
    }

    //
    return -1;
  }

  /**
   * <p>
   * </p>
   *
   * @param node
   * @param label
   * @return
   */
  private boolean hasLabel(Object node, String label) {
    return ((GraphDbNodeSource) ((HGNode) node).getNodeSource()).getLabels().contains(label);
  }

  /**
   * <p>
   * </p>
   *
   * @param node1
   * @param node2
   * @param label
   * @return
   */
  private boolean hasLabel(Object node1, Object node2, String label) {
    return hasLabel(node1, label) && hasLabel(node2, label);
  }

  private boolean hasProperty(Object node, String property) {
    return ((GraphDbNodeSource) ((HGNode) node).getNodeSource()).getProperties().containsKey(property);
  }

  private String getProperty(Object node, String property) {
    return ((GraphDbNodeSource) ((HGNode) node).getNodeSource()).getProperties().get(property);
  }

  private int compareProperties(Object node1, Object node2, String property) {

    //
    if (!(((GraphDbNodeSource) ((HGNode) node1).getNodeSource()).getProperties().containsKey(property))) {
      return 0;
    }

    //
    if (!(((GraphDbNodeSource) ((HGNode) node2).getNodeSource()).getProperties().containsKey(property))) {
      return 0;
    }

    //
    return ((GraphDbNodeSource) ((HGNode) node1).getNodeSource()).getProperties().get(property)
        .compareTo(((GraphDbNodeSource) ((HGNode) node2).getNodeSource()).getProperties().get(property));
  }

  /**
   * <p>
   * </p>
   *
   * @param object
   * @return
   */
  private boolean hasGraphDbNodeSource(Object object) {
    return object instanceof HGNode && ((HGNode) object).getNodeSource() instanceof GraphDbNodeSource;
  }
}
