/*
 * Copyright (c) 2002-2017 "Neo Technology,"
 * Network Engine for Objects in Lund AB [http://neotechnology.com]
 *
 * This file is part of Neo4j.
 *
 * Neo4j is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.neo4j.kernel.api.index;

import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import java.io.File;

import org.neo4j.io.fs.FileSystemAbstraction;
import org.neo4j.kernel.api.schema_new.LabelSchemaDescriptor;
import org.neo4j.kernel.api.schema_new.index.NewIndexDescriptor;
import org.neo4j.test.rule.TestDirectory;
import org.neo4j.test.rule.fs.DefaultFileSystemRule;
import org.neo4j.test.runner.ParameterizedSuiteRunner;

@RunWith( ParameterizedSuiteRunner.class )
@Suite.SuiteClasses( {
        NonUniqueIndexPopulatorCompatibility.class,
        UniqueIndexPopulatorCompatibility.class,
        NonUniqueIndexAccessorCompatibility.class,
        UniqueIndexAccessorCompatibility.class,
        UniqueConstraintCompatibility.class,
        CompositeIndexAccessorCompatibility.class
} )
public abstract class IndexProviderCompatibilityTestSuite
{
    protected abstract SchemaIndexProvider createIndexProvider( FileSystemAbstraction fs, File graphDbDir );

    public abstract static class Compatibility
    {
        @Rule
        public final DefaultFileSystemRule fileSystemRule = new DefaultFileSystemRule();
        @Rule
        public final TestDirectory testDir = TestDirectory.testDirectory( getClass() );

        protected File graphDbDir;
        protected FileSystemAbstraction fs;
        protected final IndexProviderCompatibilityTestSuite testSuite;
        protected SchemaIndexProvider indexProvider;
        protected NewIndexDescriptor descriptor;

        @Before
        public void setup()
        {
            fs = fileSystemRule.get();
            graphDbDir = testDir.graphDbDir();
            indexProvider = testSuite.createIndexProvider( fs, graphDbDir );
        }

        public Compatibility( IndexProviderCompatibilityTestSuite testSuite, NewIndexDescriptor descriptor )
        {
            this.testSuite = testSuite;
            this.descriptor = descriptor;
        }
    }
}
