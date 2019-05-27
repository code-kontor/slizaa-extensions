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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class JavapWrapper {

  public static void doIt(String classpath, String className) {

    File javapFile = new File(System.getProperty("java.home"), "../bin/javap.exe");

    List<String> command = new ArrayList<>();
    command.add(javapFile.getAbsolutePath());
    command.add("-v");
    command.add("-constants");
    command.add("-cp");
    command.add(classpath);
    command.add(className);

    //
    ProcessExecutor.run("jdeps", command, new Log() {

      @Override
      public void info(CharSequence message) {
        System.out.println(message);
      }

      @Override
      public void error(CharSequence message) {
        System.out.println(message);
      }

      @Override
      public void debug(CharSequence message) {
        System.out.println(message);
      }
    });
  }
  
  public static void main(String[] args) {
    doIt("D:\\50-Development\\environments\\slizaa-master\\ws\\TestReferenceProject\\bin", "test.InnerOuterTest$InnerClass");
  }
}