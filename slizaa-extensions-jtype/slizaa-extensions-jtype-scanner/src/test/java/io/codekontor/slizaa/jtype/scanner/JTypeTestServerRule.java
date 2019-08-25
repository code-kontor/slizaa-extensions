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
package io.codekontor.slizaa.jtype.scanner;

import io.codekontor.slizaa.core.progressmonitor.DefaultProgressMonitor;
import io.codekontor.slizaa.jtype.scanner.bytecode.JTypeByteCodeParserFactory;
import io.codekontor.slizaa.neo4j.graphdbfactory.internal.GraphDbFactory;
import io.codekontor.slizaa.neo4j.importer.internal.ModelImporterFactory;
import io.codekontor.slizaa.scanner.api.cypherregistry.ICypherStatementRegistry;
import io.codekontor.slizaa.scanner.api.graphdb.IGraphDb;
import io.codekontor.slizaa.scanner.api.graphdb.IGraphDbFactory;
import io.codekontor.slizaa.scanner.api.importer.IModelImporter;
import io.codekontor.slizaa.scanner.api.importer.IModelImporterFactory;
import io.codekontor.slizaa.scanner.cypherregistry.CypherRegistryUtils;
import io.codekontor.slizaa.scanner.cypherregistry.CypherStatementRegistry;
import io.codekontor.slizaa.scanner.spi.contentdefinition.IContentDefinitionProvider;
import io.codekontor.slizaa.scanner.spi.parser.IParserFactory;
import io.codekontor.slizaa.scanner.testfwk.internal.ZipUtil;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
 */
public class JTypeTestServerRule implements TestRule {

    /**
     * -
     */
    private File _databaseDirectory;

    /**
     * -
     */
    private IGraphDb _graphDb;

    /**
     * -
     */
    private IGraphDbFactory _graphDbFactory;

    /**
     * -
     */
    private IContentDefinitionProvider _contentDefinitionProvider;

    private boolean _noDeletation;

    /**
     * <p>
     * Creates a new instance of type {@link io.codekontor.slizaa.scanner.testfwk.AbstractSlizaaTestServerRule}.
     * </p>
     */
    public JTypeTestServerRule(IContentDefinitionProvider contentDefinitions) {
        this._contentDefinitionProvider = contentDefinitions;
    }

    public JTypeTestServerRule withDatabaseDirectory(File databaseDirectory) {
        checkNotNull(databaseDirectory);
        this._databaseDirectory = databaseDirectory;
        return this;
    }

    public JTypeTestServerRule withNoDeletionAfterTest() {
        this._noDeletation = true;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Statement apply(Statement base, Description description) {

        this._databaseDirectory =
                this._databaseDirectory != null ? this._databaseDirectory : createDatabaseDirectory();

        if (!this._databaseDirectory.exists()) {
            this._databaseDirectory.mkdirs();
        }

        return new Statement() {

            @Override
            public void evaluate() throws Throwable {

                //
                _graphDb = createGraphDatabase();

                try {
                    base.evaluate();
                } finally {
                    try {
                        _graphDb.close();
                        if (!_noDeletation) {
                            Files.walk(_databaseDirectory.toPath()).map(Path::toFile)
                                    .sorted((o1, o2) -> -o1.compareTo(o2)).forEach(File::delete);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };
    }

    public void exportDatabaseAsZipFile(String file, boolean restart) throws Exception {
        _graphDb.shutdown();

        ZipUtil.zipFile(_databaseDirectory.getAbsolutePath(), checkNotNull(file), true);
        if (restart) {
            _graphDb = _graphDbFactory.newGraphDb(5001, _databaseDirectory).create();
        }
    }

    private IGraphDb createGraphDatabase() {

        ClassLoader classLoader = this.getClass().getClassLoader();

        //
        IGraphDbFactory graphDbFactory = new GraphDbFactory();
        IModelImporterFactory modelImporterFactory = new ModelImporterFactory();
        List<IParserFactory> parserFactories = Arrays.asList(new JTypeByteCodeParserFactory());
        ICypherStatementRegistry cypherStatementRegistry = new CypherStatementRegistry(() -> CypherRegistryUtils.getCypherStatementsFromClasspath(classLoader));

        if (_databaseDirectory.list().length == 0) {


            // parse
            IModelImporter modelImporter = modelImporterFactory.createModelImporter(
                    _contentDefinitionProvider, _databaseDirectory,
                    parserFactories, cypherStatementRegistry.getAllStatements());

            //
            DefaultProgressMonitor progressMonitor = new DefaultProgressMonitor("Parse", 100,
                    DefaultProgressMonitor.consoleLogger());

            //
            executeWithThreadContextClassLoader(classLoader,
                    () -> modelImporter.parse(progressMonitor,
                            () -> graphDbFactory.newGraphDb(5001, _databaseDirectory).create()));

            //
            return modelImporter.getGraphDb();
        }
        //
        else {
            return graphDbFactory.newGraphDb(5001, _databaseDirectory).create();
        }
    }

    /**
     * <p>
     * </p>
     *
     * @param runnable
     * @param classLoader
     */
    private void executeWithThreadContextClassLoader(ClassLoader classLoader, Runnable runnable) {

        //
        checkNotNull(runnable);
        checkNotNull(classLoader);

        //
        ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();

        try {
            Thread.currentThread().setContextClassLoader(classLoader);
            runnable.run();
        } finally {
            Thread.currentThread().setContextClassLoader(oldClassLoader);
        }
    }

    /**
     * <p>
     * </p>
     *
     * @return
     */
    private static File createDatabaseDirectory() {
        try {
            return Files.createTempDirectory("slizaaTestDatabases").toFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
