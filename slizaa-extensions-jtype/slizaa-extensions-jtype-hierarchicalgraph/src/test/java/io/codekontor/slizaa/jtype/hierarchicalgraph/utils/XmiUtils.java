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
package io.codekontor.slizaa.jtype.hierarchicalgraph.utils;

import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGNode;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGRootNode;

public class XmiUtils {

  /**
   * <p>
   * </p>
   *
   * @param fileName
   * @return
   */
  public static HGRootNode load(String fileName) {

    // register extension
    Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
    Map<String, Object> m = reg.getExtensionToFactoryMap();
    m.put("hggraph", new XMIResourceFactoryImpl());

    // Obtain a new resource set
    ResourceSet resSet = new ResourceSetImpl();

    // Get the resource
    Resource resource = resSet.getResource(URI.createFileURI(fileName), true);

    //
    return (HGRootNode) resource.getContents().get(0);
  }

  /**
   * <p>
   * </p>
   *
   * @param fileName
   * @param rootNode
   * @throws IOException
   */
  public static void save(String fileName, HGRootNode rootNode) throws IOException {

    //
    Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
    Map<String, Object> m = reg.getExtensionToFactoryMap();
    m.put("hggraph", new XMIResourceFactoryImpl());

    ResourceSet resSet = new ResourceSetImpl();
    Resource resource = resSet.createResource(URI.createFileURI(fileName));
    resource.getContents().add(rootNode);

    //
    for (Iterator<?> iter = EcoreUtil.getAllContents(Collections.singleton(rootNode)); iter.hasNext();) {

      Object containedElement = iter.next();

      //
      if (containedElement instanceof HGNode) {
        HGNode node = (HGNode) containedElement;
        resource.getContents().addAll(node.getOutgoingCoreDependencies());
      }
    }

    resource.save(Collections.EMPTY_MAP);
  }
}
