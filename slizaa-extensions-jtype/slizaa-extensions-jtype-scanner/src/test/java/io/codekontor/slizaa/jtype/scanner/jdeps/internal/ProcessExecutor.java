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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Executes a specified external processor, logging output to the given logger.
 *
 * @author Gunnar Morling
 */
public class ProcessExecutor {

    public static void run(String name, List<String> command, Log log) {
        ProcessBuilder builder = new ProcessBuilder( command );

        Process process;
        List<String> outputLines = new ArrayList<>();
        try {
            process = builder.start();

            BufferedReader in = new BufferedReader( new InputStreamReader( process.getInputStream() ) );
            String line;
            while ( ( line = in.readLine() ) != null ) {
                outputLines.add( line );
                log.debug( line );
            }

            BufferedReader err = new BufferedReader( new InputStreamReader( process.getErrorStream() ) );
            while ( ( line = err.readLine() ) != null ) {
                log.error( line );
            }

            process.waitFor();
        }
        catch (IOException | InterruptedException e) {
            for ( String line : outputLines ) {
                log.error( line );
            }

            throw new RuntimeException( "Couldn't run " + name, e );
        }

        if ( process.exitValue() != 0 ) {
            for ( String line : outputLines ) {
                log.error( line );
            }

            throw new RuntimeException( "Execution of " + name + " failed" );
        }
    }
}