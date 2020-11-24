package com.optum.sourcehawk.maven;

import org.apache.maven.plugins.annotations.Parameter;

/**
 * Abstract mojo which all scan types should extend
 *
 * @author Brian Wyka
 * @author Christian Oestreich
 */
public abstract class AbstractBuildFailingSourcehawkMojo extends AbstractSourcehawkMojo {

    /**
     * Whether or not a failed scan should fail the build
     *
     * @since 1.0.0
     */
    @Parameter(property = PROPERTY_PREFIX + "failBuild", defaultValue = "true")
    protected boolean failBuild;

}
