package com.optum.sourcehawk.maven;

import com.optum.sourcehawk.core.constants.SourcehawkConstants;
import com.optum.sourcehawk.core.scan.OutputFormat;
import com.optum.sourcehawk.core.scan.ScanResult;
import com.optum.sourcehawk.core.scan.Severity;
import com.optum.sourcehawk.core.scan.Verbosity;
import com.optum.sourcehawk.exec.ExecOptions;
import com.optum.sourcehawk.exec.ScanExecutor;
import lombok.val;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.codehaus.plexus.util.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.time.Instant;
import java.util.stream.Collectors;

/**
 * Goal which is responsible for performing scan
 *
 * @author Brian Wyka
 * @author Christian Oestreich
 */
@Mojo(
        name = "scan",
        defaultPhase = LifecyclePhase.VALIDATE,
        threadSafe = true,
        aggregator = true
)
@SuppressWarnings("unused")
public class ScanMojo extends AbstractSourcehawkMojo {

    /**
     * The default location which to output the report
     */
    private static final String DEFAULT_REPORT_LOCATION = "${project.build.directory}/sourcehawk/report.txt";

    /**
     * The file which the scan report will be output to, defaults to {@value #DEFAULT_REPORT_LOCATION}
     *
     * @since 1.0.0
     */
    @Parameter(property = PROPERTY_PREFIX + "reportOutputFile", defaultValue = DEFAULT_REPORT_LOCATION, required = true)
    protected File reportOutputFile;

    /**
     * Whether or not to skip execution
     *
     * @since 1.0.0
     */
    @Parameter(property = PROPERTY_NAME_SKIP, defaultValue = "false")
    protected boolean skipScan;
    private static final String PROPERTY_NAME_SKIP = PROPERTY_PREFIX + "skipScan";


    /** {@inheritDoc} */
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
        getLog().info("Running Scan...");
        final long startTime = Instant.now().toEpochMilli();
        try {
            val execOptions = ExecOptions.builder()
                    .repositoryRoot(baseDirectory.toPath())
                    .configurationFileLocation(configurationFile.getName())
                    .outputFormat(OutputFormat.TEXT)
                    .verbosity(Verbosity.ZERO)
                    .build();
            val scanResult = ScanExecutor.scan(execOptions);
            handleScanResult(scanResult);
            mavenSession.getRequest().getData().put(SourcehawkConstants.NAME, true);
        } catch (final Exception e) {
            throw new MojoExecutionException("Error executing scan", e);
        } finally {
            getLog().info(String.format("Scan completed in %dms", (Instant.now().toEpochMilli() - startTime)));
        }
    }

    /**
     * Validate configurations
     */
    private void validateConfigurations() {
        if (!configurationFile.exists()) {
            throw new ScanException("Configuration file does not exist");
        }
    }

    /**
     * Handle the scan result
     *
     * @param scanResult the scan result
     * @throws IOException if any file writing errors occur
     * @throws ScanException if the scan resulted in failure
     */
    private void handleScanResult(final ScanResult scanResult) throws IOException {
        if (scanResult.isPassed()) {
            val message = "Scan passed without any errors!";
            getLog().info(message);
            writeReportOutputFile(message);
            return;
        }
        val errorMessage = "Scan failed, see below for errors";
        getLog().error(errorMessage);
        val reportContent = scanResult.getMessages().entrySet()
                .stream()
                .flatMap(entry -> entry.getValue().stream())
                .map(this::logAndReturn)
                .collect(Collectors.joining(System.lineSeparator()));
        writeReportOutputFile(reportContent);
        if (failBuild) {
            throw new ScanException(errorMessage);
        }
    }

    /**
     * Log the messageDescriptor with the appropriate logger and then return it
     *
     * @param messageDescriptor the messageDescriptor to the log
     * @return the original messageDescriptor
     */
    private String logAndReturn(final ScanResult.MessageDescriptor messageDescriptor) {
        val message = String.format("%s :: %s", messageDescriptor.getRepositoryPath(), messageDescriptor.getMessage());
        switch (Severity.parse(messageDescriptor.getSeverity())) {
            case ERROR:
                getLog().error(message);
                break;
            case WARNING:
                getLog().warn(message);
                break;
            default:
                getLog().info(message);
        }
        return messageDescriptor.toString();
    }

    /**
     * Write the results of the scan to the report file output
     *
     * @param reportContent the report content
     * @throws IOException if any error occurs during file writing
     */
    private void writeReportOutputFile(final String reportContent) throws IOException {
        FileUtils.forceMkdir(baseDirectory);
        FileUtils.forceMkdir(reportOutputFile.getParentFile());
        try (val fileOutputStream = new FileOutputStream(reportOutputFile, false)) {
            fileOutputStream.write(reportContent.getBytes(Charset.defaultCharset()));
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
        if (skipScan) {
            getLog().info(String.format("Skipping since %s = true", PROPERTY_NAME_SKIP));
            return true;
        }
        if (!configurationFile.exists()) {
            getLog().warn(String.format("Skipping since configuration file %s is missing", configurationFile.getPath()));
            return true;
        }
        return false;
    }

}
