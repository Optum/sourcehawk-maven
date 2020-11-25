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
     * @since 0.1.0
     */
    @Parameter(property = PROPERTY_PREFIX + "failBuild", defaultValue = "true")
    protected boolean failBuild;

    /**
     * Whether or not a scan with warnings should fail the build
     *
     * @since  0.2.0
     */
    @Parameter(property = PROPERTY_PREFIX + "failOnWarnings", defaultValue = "false")
    protected boolean failOnWarnings;

    /**
     * Whether or not to log warnings
     *
     * @since  0.2.0
     */
    @Parameter(property = PROPERTY_PREFIX + "showWarnings", defaultValue = "false")
    protected boolean showWarnings;

}
