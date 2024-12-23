/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to you under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.logging.log4j.core.config.status;

<<<<<<< HEAD
import edu.umd.cs.findbugs.annotations.Nullable;
=======
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.net.URI;
import java.net.URISyntaxException;
<<<<<<< HEAD
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
=======
import java.util.Collection;
import java.util.concurrent.LinkedBlockingQueue;
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.util.FileUtils;
import org.apache.logging.log4j.core.util.NetUtils;
import org.apache.logging.log4j.status.StatusConsoleListener;
<<<<<<< HEAD
import org.apache.logging.log4j.status.StatusLogger;

/**
 * Configuration for setting up the {@link StatusLogger} fallback listener.
 */
public class StatusConfiguration {

    private static final StatusLogger LOGGER = StatusLogger.getLogger();

    private final Lock lock = new ReentrantLock();

    private volatile boolean initialized;

    @Nullable
    private PrintStream output;

    @Nullable
    private Level level;

    /**
     * Specifies how verbose the StatusLogger should be.
     * @deprecated This class is not used anymore and only kept for binary backward compatibility.
     */
    @Deprecated
=======
import org.apache.logging.log4j.status.StatusListener;
import org.apache.logging.log4j.status.StatusLogger;

/**
 * Configuration for setting up {@link StatusConsoleListener} instances.
 */
public class StatusConfiguration {

    @SuppressWarnings("UseOfSystemOutOrSystemErr")
    private static final PrintStream DEFAULT_STREAM = System.out;

    private static final Level DEFAULT_STATUS = Level.ERROR;
    private static final Verbosity DEFAULT_VERBOSITY = Verbosity.QUIET;

    private final Collection<String> errorMessages = new LinkedBlockingQueue<>();
    private final StatusLogger logger = StatusLogger.getLogger();

    private volatile boolean initialized;

    private PrintStream destination = DEFAULT_STREAM;
    private Level status = DEFAULT_STATUS;
    private Verbosity verbosity = DEFAULT_VERBOSITY;
    private String[] verboseClasses;

    /**
     * Specifies how verbose the StatusLogger should be.
     */
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
    public enum Verbosity {
        QUIET,
        VERBOSE;

        /**
         * Parses the verbosity property into an enum.
         *
         * @param value property value to parse.
         * @return enum corresponding to value, or QUIET by default.
<<<<<<< HEAD
         * @deprecated This class is not used anymore and only kept for binary backward compatibility.
         */
        @Deprecated
=======
         */
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
        public static Verbosity toVerbosity(final String value) {
            return Boolean.parseBoolean(value) ? VERBOSE : QUIET;
        }
    }

    /**
<<<<<<< HEAD
     * Logs an error message to the {@link StatusLogger}.
     *
     * @param message error message to log
     * @deprecated Use {@link StatusLogger#getLogger()} and then {@link StatusLogger#error(String)} instead.
     */
    @Deprecated
    public void error(final String message) {
        LOGGER.error(message);
    }

    /**
     * Sets the output of the {@link StatusLogger} fallback listener.
     * <p>
     * Accepted values are as follows:
     * </p>
     * <ul>
     * <li>{@code out} (i.e., {@link System#out})</li>
     * <li>{@code err} (i.e., {@link System#err})</li>
     * <li>a URI (e.g., {@code file:///path/to/log4j-status-logs.txt})</li>
     * </ul>
     * <p>
     * Invalid values will be ignored.
     * </p>
     *
     * @param destination destination where {@link StatusLogger} messages should be output
     * @return {@code this}
     */
    public StatusConfiguration withDestination(@Nullable final String destination) {
        try {
            this.output = parseStreamName(destination);
        } catch (final URISyntaxException error) {
            LOGGER.error("Could not parse provided URI: {}", destination, error);
        } catch (final FileNotFoundException error) {
            LOGGER.error("File could not be found: {}", destination, error);
=======
     * Logs an error message to the StatusLogger. If the StatusLogger hasn't been set up yet, queues the message to be
     * logged after initialization.
     *
     * @param message error message to log.
     */
    public void error(final String message) {
        if (!this.initialized) {
            this.errorMessages.add(message);
        } else {
            this.logger.error(message);
        }
    }

    /**
     * Specifies the destination for StatusLogger events. This can be {@code out} (default) for using
     * {@link System#out standard out}, {@code err} for using {@link System#err standard error}, or a file URI to
     * which log events will be written. If the provided URI is invalid, then the default destination of standard
     * out will be used.
     *
     * @param destination where status log messages should be output.
     * @return {@code this}
     */
    public StatusConfiguration withDestination(final String destination) {
        try {
            this.destination = parseStreamName(destination);
        } catch (final URISyntaxException e) {
            this.error("Could not parse URI [" + destination + "]. Falling back to default of stdout.");
            this.destination = DEFAULT_STREAM;
        } catch (final FileNotFoundException e) {
            this.error("File could not be found at [" + destination + "]. Falling back to default of stdout.");
            this.destination = DEFAULT_STREAM;
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
        }
        return this;
    }

<<<<<<< HEAD
    @Nullable
    private static PrintStream parseStreamName(@Nullable final String name)
            throws URISyntaxException, FileNotFoundException {
        if (name != null) {
            if (name.equalsIgnoreCase("out")) {
                return System.out;
            } else if (name.equalsIgnoreCase("err")) {
                return System.err;
            } else {
                final URI destUri = NetUtils.toURI(name);
                final File output = FileUtils.fileFromUri(destUri);
                if (output != null) {
                    final FileOutputStream fos = new FileOutputStream(output);
                    return new PrintStream(fos, true);
                }
            }
        }
        return null;
    }

    /**
     * Sets the level of the {@link StatusLogger} fallback listener.
     *
     * @param level a level name
     * @return {@code this}
     */
    public StatusConfiguration withStatus(@Nullable final String level) {
        this.level = Level.toLevel(level, null);
        if (this.level == null) {
            LOGGER.error("Invalid status level: {}", level);
=======
    private PrintStream parseStreamName(final String name) throws URISyntaxException, FileNotFoundException {
        if (name == null || name.equalsIgnoreCase("out")) {
            return DEFAULT_STREAM;
        }
        if (name.equalsIgnoreCase("err")) {
            return System.err;
        }
        final URI destUri = NetUtils.toURI(name);
        final File output = FileUtils.fileFromUri(destUri);
        if (output == null) {
            // don't want any NPEs, no sir
            return DEFAULT_STREAM;
        }
        final FileOutputStream fos = new FileOutputStream(output);
        return new PrintStream(fos, true);
    }

    /**
     * Specifies the logging level by name to use for filtering StatusLogger messages.
     *
     * @param status name of logger level to filter below.
     * @return {@code this}
     * @see Level
     */
    public StatusConfiguration withStatus(final String status) {
        this.status = Level.toLevel(status, null);
        if (this.status == null) {
            this.error("Invalid status level specified: " + status + ". Defaulting to ERROR.");
            this.status = Level.ERROR;
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
        }
        return this;
    }

    /**
<<<<<<< HEAD
     * Sets the level of the {@link StatusLogger} fallback listener.
     *
     * @param level a level
     * @return {@code this}
     */
    public StatusConfiguration withStatus(@Nullable final Level level) {
        this.level = level;
=======
     * Specifies the logging level to use for filtering StatusLogger messages.
     *
     * @param status logger level to filter below.
     * @return {@code this}
     */
    public StatusConfiguration withStatus(final Level status) {
        this.status = status;
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
        return this;
    }

    /**
     * Specifies the verbosity level to log at. This only applies to classes configured by
     * {@link #withVerboseClasses(String...) verboseClasses}.
     *
     * @param verbosity basic filter for status logger messages.
     * @return {@code this}
<<<<<<< HEAD
     * @deprecated This method is ineffective and only kept for binary backward compatibility.
     */
    @Deprecated
    public StatusConfiguration withVerbosity(final String verbosity) {
=======
     */
    public StatusConfiguration withVerbosity(final String verbosity) {
        this.verbosity = Verbosity.toVerbosity(verbosity);
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
        return this;
    }

    /**
     * Specifies which class names to filter if the configured verbosity level is QUIET.
     *
     * @param verboseClasses names of classes to filter if not using VERBOSE.
     * @return {@code this}
<<<<<<< HEAD
     * @deprecated This method is ineffective and only kept for binary backward compatibility.
     */
    @Deprecated
    public StatusConfiguration withVerboseClasses(final String... verboseClasses) {
=======
     */
    public StatusConfiguration withVerboseClasses(final String... verboseClasses) {
        this.verboseClasses = verboseClasses;
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
        return this;
    }

    /**
     * Configures and initializes the StatusLogger using the configured options in this instance.
     */
    public void initialize() {
<<<<<<< HEAD
        lock.lock();
        try {
            if (!this.initialized) {
                final StatusConsoleListener fallbackListener = LOGGER.getFallbackListener();
                if (output != null) {
                    fallbackListener.setStream(output);
                }
                if (level != null) {
                    fallbackListener.setLevel(level);
                }
                initialized = true;
            }
        } finally {
            lock.unlock();
        }
    }
=======
        if (!this.initialized) {
            if (this.status == Level.OFF) {
                this.initialized = true;
            } else {
                final boolean configured = configureExistingStatusConsoleListener();
                if (!configured) {
                    registerNewStatusConsoleListener();
                }
                migrateSavedLogMessages();
            }
        }
    }

    private boolean configureExistingStatusConsoleListener() {
        boolean configured = false;
        for (final StatusListener statusListener : this.logger.getListeners()) {
            if (statusListener instanceof StatusConsoleListener) {
                final StatusConsoleListener listener = (StatusConsoleListener) statusListener;
                listener.setLevel(this.status);
                this.logger.updateListenerLevel(this.status);
                if (this.verbosity == Verbosity.QUIET) {
                    listener.setFilters(this.verboseClasses);
                }
                configured = true;
            }
        }
        return configured;
    }

    private void registerNewStatusConsoleListener() {
        final StatusConsoleListener listener = new StatusConsoleListener(this.status, this.destination);
        if (this.verbosity == Verbosity.QUIET) {
            listener.setFilters(this.verboseClasses);
        }
        this.logger.registerListener(listener);
    }

    private void migrateSavedLogMessages() {
        for (final String message : this.errorMessages) {
            this.logger.error(message);
        }
        this.initialized = true;
        this.errorMessages.clear();
    }
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
}
