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
package io.codekontor.slizaa.jtype.scanner.jdeps.internal;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JDepsWrapper {

  public Map<String, List<String>> analyze(String jarFile) {

    //
    checkNotNull(jarFile);

    CollectionLog log = new CollectionLog();

    File jdepsFile = new File(System.getProperty("java.home"), "../bin/jdeps.exe");
    List<String> command = new ArrayList<>();
    command.add(jdepsFile.getAbsolutePath());
    command.add("-verbose:class");
    command.add("-filter:none");
    command.add(jarFile);

    ProcessExecutor.run("jdeps", command, log);

    return log.getResult();
  }

  public static void main(String[] args) throws Exception {

/*    //
    MavenResolver mavenResolver = MavenResolvers.createMavenResolver(null, null);
    File jarFile = mavenResolver.resolve("org.mapstruct", "mapstruct", null, null, "1.2.0.CR2");

    //
    JDepsWrapper jdepsWrapper = new JDepsWrapper();

    Map<String, List<String>> map = jdepsWrapper.analyze(jarFile.getAbsolutePath());
    System.out.println(map);*/
  }
}
