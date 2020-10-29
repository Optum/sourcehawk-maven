package com.optum.sourcehawk.maven;

/**
 * An exception which indicates a failure occurred during scan execution
 *
 * @author Brian Wyka
 */
public class ScanException extends RuntimeException {

    private static final long serialVersionUID = -669093284499316358L;

    /**
     * Constructs an instance of the exception with the supplied {@code message}
     *
     * @param message the message
     */
    ScanException(final String message) {
        super(message);
    }

}
