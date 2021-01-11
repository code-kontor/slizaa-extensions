/**
 * slizaa-extensions-neo4j-graphdbfactory - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.neo4j.graphdbfactory.internal;

import org.slf4j.ILoggerFactory;
import org.slf4j.LoggerFactory;

import org.neo4j.logging.Log;
import org.neo4j.logging.LogProvider;

public class Slf4jLogProvider implements LogProvider
{
    private ILoggerFactory loggerFactory;

    public Slf4jLogProvider()
    {
        this( LoggerFactory.getILoggerFactory() );
    }

    public Slf4jLogProvider( ILoggerFactory loggerFactory )
    {
        this.loggerFactory = loggerFactory;
    }

    @Override
    public Log getLog( Class<?> loggingClass )
    {
        return new Slf4jLog( loggerFactory.getLogger( loggingClass.getName() ) );
    }

    @Override
    public Log getLog( String context )
    {
        return new Slf4jLog( loggerFactory.getLogger( context ) );
    }
}
