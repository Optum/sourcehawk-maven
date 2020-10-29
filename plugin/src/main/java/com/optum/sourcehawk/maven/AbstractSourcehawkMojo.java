package com.optum.sourcehawk.maven;

import com.optum.sourcehawk.core.constants.SourcehawkConstants;
import com.optum.sourcehawk.core.scan.OutputFormat;
import com.optum.sourcehawk.core.scan.ScanResult;
import com.optum.sourcehawk.core.scan.Severity;
import com.optum.sourcehawk.core.scan.Verbosity;
import com.optum.sourcehawk.core.utils.StringUtils;
import com.optum.sourcehawk.exec.ExecOptions;
import com.optum.sourcehawk.exec.ScanExecutor;
import lombok.val;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Parameter;
import org.codehaus.plexus.util.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.time.Instant;
import java.util.stream.Collectors;

/**
 * Abstract mojo which all scan types should extend
 *
 * @author Brian Wyka
 * @author Christian Oestreich
 */
public abstract class AbstractSourcehawkMojo extends AbstractMojo {

    /**
     * The user property prefix
     */
    protected static final String PROPERTY_PREFIX = "sourcehawk.";

    /**
     * The project base directory
     *
     * @since 1.0.0
     */
    @Parameter(defaultValue = "${basedir}", readonly = true, required = true)
    protected File baseDirectory;

    /**
     * Whether or not to skip execution
     *
     * @since 1.0.0
     */
    @Parameter(property = GLOBAL_PROPERTY_NAME_SKIP, defaultValue = "false")
    protected boolean skip;
    protected static final String GLOBAL_PROPERTY_NAME_SKIP = PROPERTY_PREFIX + "skip";

    /**
     * The configuration file which is used to configure Sourcehawk
     *
     * @since 1.0.0
     */
    @Parameter(property = PROPERTY_PREFIX + "configurationFile", defaultValue = SourcehawkConstants.DEFAULT_CONFIG_FILE_NAME, required = true)
    protected File configurationFile;

    /**
     * Whether or not a failed scan should fail the build
     *
     * @since 1.0.0
     */
    @Parameter(property = PROPERTY_PREFIX + "failBuild", defaultValue = "true")
    protected boolean failBuild;

    /**
     * Used for accessing the maven session
     */
    @SuppressWarnings("unused")
    @Parameter(defaultValue = "${session}", readonly = true)
    protected MavenSession mavenSession;

}
