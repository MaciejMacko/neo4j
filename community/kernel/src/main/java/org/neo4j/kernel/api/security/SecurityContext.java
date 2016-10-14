/*
 * Copyright (c) 2002-2016 "Neo Technology,"
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
package org.neo4j.kernel.api.security;

/** Controls the capabilities of a KernelTransaction. */
public interface SecurityContext
{
    AccessMode mode();
    AuthSubject subject();

    SecurityContext freeze();
    SecurityContext freeze( AccessMode mode );

    default String defaultString( String name )
    {
        return String.format( "%s{ username=%s, accessMode=%s }", name, subject().username(), mode() );
    }

    /** Allows all operations. */
    SecurityContext AUTH_DISABLED = new SecurityContext()
    {
        @Override
        public AccessMode mode()
        {
            return AccessMode.Static.FULL;
        }

        @Override
        public String toString()
        {
            return defaultString( "auth-disabled" );
        }

        @Override
        public AuthSubject subject()
        {
            return AuthSubject.AUTH_DISABLED;
        }

        @Override
        public SecurityContext freeze()
        {
            return this;
        }

        @Override
        public SecurityContext freeze( AccessMode mode )
        {
            return new Frozen( subject(), mode );
        }
    };

    class Frozen implements SecurityContext
    {
        private final AuthSubject subject;
        private final AccessMode mode;

        public Frozen( AuthSubject subject, AccessMode mode )
        {
            this.subject = subject;
            this.mode = mode;
        }

        @Override
        public AccessMode mode()
        {
            return mode;
        }

        @Override
        public AuthSubject subject()
        {
            return subject;
        }

        @Override
        public SecurityContext freeze()
        {
            return this;
        }

        @Override
        public SecurityContext freeze( AccessMode mode )
        {
            return new Frozen( subject, mode );
        }
    }
}
