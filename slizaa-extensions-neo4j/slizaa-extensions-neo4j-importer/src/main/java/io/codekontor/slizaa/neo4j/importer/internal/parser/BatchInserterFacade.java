/**
 * slizaa-extensions-neo4j-importer - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.neo4j.importer.internal.parser;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import org.neo4j.batchinsert.BatchInserter;
import org.neo4j.batchinsert.BatchInserters;
import org.neo4j.io.layout.DatabaseLayout;
import org.neo4j.io.layout.Neo4jLayout;

import io.codekontor.slizaa.scanner.spi.contentdefinition.IContentDefinition;
import io.codekontor.slizaa.scanner.spi.contentdefinition.filebased.IFile;
import io.codekontor.slizaa.scanner.spi.parser.model.INode;
import io.codekontor.slizaa.scanner.spi.parser.model.IRelationship;
import io.codekontor.slizaa.scanner.spi.parser.model.Label;
import io.codekontor.slizaa.scanner.spi.parser.model.NodeFactory;
import io.codekontor.slizaa.scanner.spi.parser.model.RelationshipType;
import io.codekontor.slizaa.scanner.spi.parser.model.resource.CoreModelElementType;
import io.codekontor.slizaa.scanner.spi.parser.model.resource.CoreModelRelationshipType;
import io.codekontor.slizaa.scanner.spi.parser.model.resource.IDirectoryNode;
import io.codekontor.slizaa.scanner.spi.parser.model.resource.IModuleNode;
import io.codekontor.slizaa.scanner.spi.parser.model.resource.IResourceNode;
import io.codekontor.slizaa.scanner.spi.parser.model.resource.ResourceType;

/**
 * <p>
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class BatchInserterFacade implements AutoCloseable {

  /** - */
  private BatchInserter                  _batchInserter;

  /** - */
  private String                         _storeDir;

  /** - */
  private Map<String, INode>             _directoriesMap;

  /** - */
  private Map<IFile, INode>          _resourcesMap;

  /** - */
  private Map<IContentDefinition, INode> _modulesMap;

  /**
   * <p>
   * Creates a new instance of type {@link BatchInserterFacade}.
   * </p>
   * 
   * @param storeDir
   */
  public BatchInserterFacade(String storeDir) {

    //
    checkNotNull(storeDir);
    _storeDir = storeDir;

    try {
      _batchInserter = BatchInserters.inserter(DatabaseLayout.ofFlat(new File(_storeDir, "data/databases/neo4j").toPath()));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    //
    _resourcesMap = new HashMap<>();
    _modulesMap = new HashMap<>();
    _directoriesMap = new HashMap<>();
  }

  BatchInserter batchInserter() {
    return _batchInserter;
  }

  /**
   * <p>
   * </p>
   *
   * @param contentDefinition
   * @return
   */
  public INode getOrCreateModuleNode(final IContentDefinition contentDefinition) {

    //
    if (!_modulesMap.containsKey(contentDefinition)) {

      //
      INode moduleNode = NodeFactory.createNode();
      moduleNode.addLabel(CoreModelElementType.Module);
      moduleNode.putProperty(IModuleNode.PROPERTY_MODULE_NAME, contentDefinition.getName());
      moduleNode.putProperty(INode.FQN, contentDefinition.getName());
      moduleNode.putProperty(IModuleNode.PROPERTY_MODULE_VERSION, contentDefinition.getVersion());
      _modulesMap.put(contentDefinition, moduleNode);
    }

    //
    return (INode) _modulesMap.get(contentDefinition);
  }

  /**
   * <p>
   * </p>
   * 
   * @param resource
   * @return
   */
  public INode getOrCreateResourceNode(final INode parentModuleNode,
      final INode parentDirectoryNode, final IFile resource, final ResourceType resourceType) {

    //
    return getOrCreateResourceNode(resource, () -> {
      INode resourceNode = NodeFactory.createNode();
      resourceNode.addLabel(CoreModelElementType.Resource);
      resourceNode.addLabel(resourceType);
      resourceNode.putProperty(INode.NAME, resource.getName());
      resourceNode.putProperty(INode.FQN, resource.getPath());
      resourceNode.putProperty(IResourceNode.PROPERTY_PATH, resource.getPath());
      resourceNode.putProperty(IResourceNode.PROPERTY_ROOT, resource.getRoot());
      resourceNode.putProperty(IResourceNode.PROPERTY_PATH, resource.getPath());
      resourceNode.putProperty(IResourceNode.PROPERTY_ERRONEOUS, false);
      resourceNode.putProperty(IResourceNode.PROPERTY_ANALYSE_REFERENCES, true);
      parentModuleNode.addRelationship(resourceNode, CoreModelRelationshipType.CONTAINS);

      synchronized (parentDirectoryNode) {
        ((INode) parentDirectoryNode).addRelationship(resourceNode, CoreModelRelationshipType.CONTAINS);
      }

      return resourceNode;
    });
  }

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  public Map<IFile, INode> getResourcesMap() {
    return Collections.unmodifiableMap(_resourcesMap);
  }

  /**
   * <p>
   * </p>
   *
   * @return
   */
  public Map<String, INode> getDirectoriesMap() {
    return Collections.unmodifiableMap(_directoriesMap);
  }

  public void clearResourceAndDirectoriesMap() {
    _resourcesMap.clear();
    _directoriesMap.clear();
  }

  /**
   * <p>
   * </p>
   * 
   * @param nodeBean
   * @return
   */
  public long create(INode nodeBean) {

    //
    long id = -1;

    if (!nodeBean.hasNodeId()) {

      //
      id = _batchInserter.createNode(nodeBean.getProperties(),
          LabelAndRelationshipCache.convert(nodeBean.getLabels().toArray(new Label[0])));

      ((INode) nodeBean).setNodeId(id);

      //
      for (Map.Entry<RelationshipType, List<IRelationship>> entry : nodeBean.getRelationships().entrySet()) {
        for (IRelationship relationship : entry.getValue()) {
          long newId = create(relationship.getTargetBean());
          _batchInserter.createRelationship(id, newId, LabelAndRelationshipCache.convert(entry.getKey()),
              relationship.getRelationshipProperties());
        }
      }
    }

    return nodeBean.getId();
  }

  public INode getOrCreateDirectoyNode(Directory directoryPath, INode moduleNode) {

    //
    String[] pathElements = directoryPath.getPath().split("/");

    // create complete structure
    String currentPath = "";
    INode hierarchicalParent = moduleNode;
    INode currentDirectory = null;
    for (String element : pathElements) {
      currentPath = currentPath.isEmpty() ? element : currentPath + "/" + element;
      currentDirectory = _getOrCreateDirectoyNode(currentPath, hierarchicalParent, moduleNode);
      hierarchicalParent = currentDirectory;
    }

    //
    currentDirectory.putProperty(IDirectoryNode.PROPERTY_IS_EMPTY, false);

    //
    return currentDirectory;
  }

  /**
   * <p>
   * </p>
   *
   * ${tags}
   */
  private INode _getOrCreateDirectoyNode(String path, INode hierarchicalParent,
      INode moduleNode) {

    //
    return _directoriesMap.computeIfAbsent(path, id -> {

      //
      INode directoryNode = NodeFactory.createNode();
      directoryNode.addLabel(CoreModelElementType.Directory);
      directoryNode.putProperty(INode.FQN, path);
      String name = path.lastIndexOf('/') != -1 ? path.substring(path.lastIndexOf('/') + 1) : path;
      directoryNode.putProperty(INode.NAME, name);
      directoryNode.putProperty(IDirectoryNode.PROPERTY_PATH, path);
      directoryNode.putProperty(IDirectoryNode.PROPERTY_IS_EMPTY, true);

      //
      if (hierarchicalParent != null && !hierarchicalParent.equals(moduleNode)) {
        hierarchicalParent.addRelationship(directoryNode, CoreModelRelationshipType.CONTAINS);
      }
      moduleNode.addRelationship(directoryNode, CoreModelRelationshipType.CONTAINS);

      //
      return directoryNode;
    });
  }

  /**
   * <p>
   * </p>
   * 
   * @param resourceId
   * @param nodeCreator
   * @return
   */
  private INode getOrCreateResourceNode(IFile resourceId, Supplier<INode> nodeCreator) {

    //
    INode node = _resourcesMap.get(resourceId);
    if (node == null) {
      synchronized (this) {
        if (!_resourcesMap.containsKey(resourceId)) {
          node = nodeCreator.get();
          _resourcesMap.put(resourceId, node);
        }
      }
    }

    //
    return _resourcesMap.get(resourceId);
  }

  /**
   * {@inheritDoc}
   */
  public synchronized void close() {
    _batchInserter.shutdown();
  }
}
