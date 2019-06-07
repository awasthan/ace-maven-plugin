package org.apache.maven.plugin.dependency.utils;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

/**
 *
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.maven.artifact.Artifact;

/**
 * @author <a href="mailto:brianf@apache.org">Brian Fox</a>
 * @version $Id: DependencyStatusSets.java 1482068 2013-05-13 20:06:08Z pgier $
 */
public class DependencyStatusSets
{

    Set<Artifact> resolvedDependencies = null;

    Set<Artifact> unResolvedDependencies = null;

    Set<Artifact> skippedDependencies = null;

    public DependencyStatusSets()
    {

    }

    public DependencyStatusSets( Set<Artifact> resolved, Set<Artifact> unResolved, Set<Artifact> skipped )
    {
        if ( resolved != null )
        {
            this.resolvedDependencies = new LinkedHashSet<Artifact>( resolved );
        }
        if ( unResolved != null )
        {
            this.unResolvedDependencies = new LinkedHashSet<Artifact>( unResolved );
        }
        if ( skipped != null )
        {
            this.skippedDependencies = new LinkedHashSet<Artifact>( skipped );
        }
    }

    /**
     * @return Returns the resolvedDependencies.
     */
    public Set<Artifact> getResolvedDependencies()
    {
        return this.resolvedDependencies;
    }

    /**
     * @param resolvedDependencies
     *            The resolvedDependencies to set.
     */
    public void setResolvedDependencies( Set<Artifact> resolvedDependencies )
    {
        if ( resolvedDependencies != null )
        {
            this.resolvedDependencies = new LinkedHashSet<Artifact>( resolvedDependencies );
        }
        else
        {
            this.resolvedDependencies = null;
        }
    }

    /**
     * @return Returns the skippedDependencies.
     */
    public Set<Artifact> getSkippedDependencies()
    {
        return this.skippedDependencies;
    }

    /**
     * @param skippedDependencies
     *            The skippedDependencies to set.
     */
    public void setSkippedDependencies( Set<Artifact> skippedDependencies )
    {
        if ( skippedDependencies != null )
        {
            this.skippedDependencies = new LinkedHashSet<Artifact>( skippedDependencies );
        }
        else
        {
            this.skippedDependencies = null;
        }
    }

    /**
     * @return Returns the unResolvedDependencies.
     */
    public Set<Artifact> getUnResolvedDependencies()
    {
        return this.unResolvedDependencies;
    }

    /**
     * @param unResolvedDependencies
     *            The unResolvedDependencies to set.
     */
    public void setUnResolvedDependencies( Set<Artifact> unResolvedDependencies )
    {
        if ( unResolvedDependencies != null )
        {
            this.unResolvedDependencies = new LinkedHashSet<Artifact>( unResolvedDependencies );
        }
        else
        {
            this.unResolvedDependencies = null;
        }
    }

    public String getOutput( boolean outputAbsoluteArtifactFilename )
    {
        return getOutput( outputAbsoluteArtifactFilename, true );
    }

    public String getOutput( boolean outputAbsoluteArtifactFilename, boolean outputScope )
    {
        return getOutput(outputAbsoluteArtifactFilename, outputScope, false);
    }

    public String getOutput( boolean outputAbsoluteArtifactFilename, boolean outputScope, boolean sort )
    {
        StringBuilder sb = new StringBuilder();
        sb.append( "\n" );
        sb.append( "The following files have been resolved:\n" );
        if ( this.resolvedDependencies == null || this.resolvedDependencies.isEmpty() )
        {
            sb.append( "   none\n" );
        }
        else
        {
            sb.append( buildArtifactListOutput( resolvedDependencies, outputAbsoluteArtifactFilename, outputScope, sort ) );
        }

        if ( this.skippedDependencies != null && !this.skippedDependencies.isEmpty() )
        {
            sb.append( "\n" );
            sb.append( "The following files were skipped:\n" );
            Set<Artifact> skippedDependencies = new LinkedHashSet<Artifact>();
            skippedDependencies.addAll( this.skippedDependencies );
            sb.append( buildArtifactListOutput( skippedDependencies, outputAbsoluteArtifactFilename, outputScope, sort ) );
        }

        if ( this.unResolvedDependencies != null && !this.unResolvedDependencies.isEmpty() )
        {
            sb.append( "\n" );
            sb.append( "The following files have NOT been resolved:\n" );
            Set<Artifact> unResolvedDependencies = new LinkedHashSet<Artifact>();
            unResolvedDependencies.addAll( this.unResolvedDependencies );
            sb.append( buildArtifactListOutput( unResolvedDependencies, outputAbsoluteArtifactFilename, outputScope, sort ) );
        }
        sb.append( "\n" );

        return sb.toString();
    }

    private StringBuilder buildArtifactListOutput(Set<Artifact> artifacts,  boolean outputAbsoluteArtifactFilename, boolean outputScope, boolean sort )
    {
        StringBuilder sb = new StringBuilder();
        List<String> artifactStringList = new ArrayList<String>();
        for ( Artifact artifact : artifacts )
        {
            String artifactFilename = null;
            if ( outputAbsoluteArtifactFilename )
            {
                try
                {
                    // we want to print the absolute file name here
                    artifactFilename = artifact.getFile().getAbsoluteFile().getPath();
                }
                catch ( NullPointerException e )
                {
                    // ignore the null pointer, we'll output a null string
                    artifactFilename = null;
                }
            }

            String id = outputScope ? artifact.toString() : artifact.getId();

            artifactStringList.add( "   " + id + ( outputAbsoluteArtifactFilename ? ":" + artifactFilename : "" ) + "\n" );
        }
        if ( sort )
        {
            Collections.sort( artifactStringList );
        }
        for (String artifactString : artifactStringList)
        {
            sb.append( artifactString );
        }
        return sb;
    }
}
