package com.optum.sourcehawk.maven;

/**
 * An exception which indicates a failure occurred during mojo execution
 *
 * @author Brian Wyka
 * @author Christian Oestreich
 */
public class SourcehawkException extends RuntimeException {

    private static final long serialVersionUID = -669093284499316358L;

    /**
     * Constructs an instance of the exception with the supplied {@code message}
     *
     * @param message the message
     */
    SourcehawkException(final String message) {
        super(message);
    }

}
