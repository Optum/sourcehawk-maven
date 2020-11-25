package com.optum.sourcehawk.maven;

import com.optum.sourcehawk.core.constants.SourcehawkConstants;
import com.optum.sourcehawk.core.scan.OutputFormat;
import com.optum.sourcehawk.core.scan.Verbosity;
import com.optum.sourcehawk.exec.ExecOptions;
import com.optum.sourcehawk.exec.FlattenConfigExecutor;
import com.optum.sourcehawk.exec.FlattenConfigResultLogger;
import lombok.val;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.time.Instant;

/**
 * Goal which is responsible for flattening Sourcehawk configuration files.  Will recurse remote files and merge configurations.
 *
 * @author Christian Oestreich
 * @since 0.2.0
 */
@Mojo(
        name = "flatten-config",
        defaultPhase = LifecyclePhase.VALIDATE,
        threadSafe = true,
        aggregator = true
)
@SuppressWarnings("unused")
public class FlattenConfigMojo extends AbstractSourcehawkMojo {

    /**
     * The default location which to output the report
     */
    private static final String DEFAULT_REPORT_LOCATION = "${project.basedir}/sourcehawk-flatten.yml";

    /**
     * The file which the scan report will be output to, defaults to {@value #DEFAULT_REPORT_LOCATION}
     *
     * @since 0.2.0
     */
    @Parameter(property = PROPERTY_PREFIX + "flattenOutputFile", defaultValue = DEFAULT_REPORT_LOCATION)
    protected File flattenOutputFile;

    /**
     * Whether or not to output to the console, default is true which will NOT output to file
     *
     * @since 0.2.0
     */
    @Parameter(property = PROPERTY_NAME_CONSOLE_OUTPUT, defaultValue = "true")
    protected boolean flattenOutputConsole;
    private static final String PROPERTY_NAME_CONSOLE_OUTPUT = PROPERTY_PREFIX + "flattenOutputConsole";


    /**
     * {@inheritDoc}
     */
    @Override
    public void execute() throws MojoExecutionException {
        executeScan();
    }

    /**
     * Execute the scan for the given {@code scanType}
     *
     * @throws MojoExecutionException if any error occurs during scan
     */
    protected void executeScan() throws MojoExecutionException {
        if (shouldSkip()) {
            return;
        }
        validateConfigurations();
        getLog().info("Flattening Configuration...");
        final long startTime = Instant.now().toEpochMilli();
        try {
            val execOptions = ExecOptions.builder()
                    .repositoryRoot(baseDirectory.toPath())
                    .configurationFileLocation(configurationFile.getName())
                    .outputFormat(OutputFormat.TEXT)
                    .verbosity(Verbosity.ZERO)
                    .build();

            val flattenConfigResult = FlattenConfigExecutor.flatten(execOptions.getConfigurationFileLocation());
            if (flattenConfigResult.isError()) {
                throw new SourcehawkException(flattenConfigResult.getFormattedMessage());
            }
            val output = flattenOutputFile != null ? flattenOutputFile.toPath() : null;
            FlattenConfigResultLogger.log(flattenConfigResult, output);
            mavenSession.getRequest().getData().put(SourcehawkConstants.NAME, true);
        } catch (final Exception e) {
            throw new MojoExecutionException("Error executing flatten", e);
        } finally {
            getLog().info(String.format("Scan completed in %dms", (Instant.now().toEpochMilli() - startTime)));
        }
    }

    /**
     * Validate configurations
     */
    private void validateConfigurations() {
        if (!configurationFile.exists()) {
            throw new SourcehawkException("Configuration file does not exist");
        }
    }

    /**
     * Determine if the mojo execution should be skipped
     *
     * @return true if should be skipped, false otherwise
     */
    protected boolean shouldSkip() {
        if (mavenSession.getRequest().getData().containsKey(SourcehawkConstants.NAME)) {
            return true;
        }
        if (skip) {
            getLog().info(String.format("Skipping since %s = true", GLOBAL_PROPERTY_NAME_SKIP));
            return true;
        }
        if (!configurationFile.exists()) {
            getLog().warn(String.format("Skipping since configuration file %s is missing", configurationFile.getPath()));
            return true;
        }
        return false;
    }

}
