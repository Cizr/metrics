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
package org.apache.logging.log4j.status;

<<<<<<< HEAD
import static java.util.Objects.requireNonNull;

import edu.umd.cs.findbugs.annotations.Nullable;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.logging.log4j.Level;

/**
 * A {@link StatusListener} that writes to the console.
=======
import java.io.IOException;
import java.io.PrintStream;
import java.util.Objects;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.ParameterizedNoReferenceMessageFactory;

/**
 * {@link StatusListener} that writes to the console.
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
 */
@SuppressWarnings("UseOfSystemOutOrSystemErr")
public class StatusConsoleListener implements StatusListener {

<<<<<<< HEAD
    private final Lock lock = new ReentrantLock();

    private final Level initialLevel;

    private final PrintStream initialStream;

    // `volatile` is necessary to correctly read the `level` without holding the lock
    private volatile Level level;

    // `volatile` is necessary to correctly read the `stream` without holding the lock
    private volatile PrintStream stream;
=======
    private Level level;

    private String[] filters;

    private final PrintStream stream;

    private final Logger logger;
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c

    /**
     * Constructs a {@link StatusConsoleListener} instance writing to {@link System#out} using the supplied level.
     *
     * @param level the level of status messages that should appear on the console
     * @throws NullPointerException on null {@code level}
     */
    public StatusConsoleListener(final Level level) {
        this(level, System.out);
    }

    /**
     * Constructs a {@link StatusConsoleListener} instance using the supplied level and stream.
     * <p>
     * Make sure not to use a logger stream of some sort to avoid creating an infinite loop of indirection!
     * </p>
     *
     * @param level the level of status messages that should appear on the console
     * @param stream the stream to write to
     * @throws NullPointerException on null {@code level} or {@code stream}
     */
    public StatusConsoleListener(final Level level, final PrintStream stream) {
<<<<<<< HEAD
        this.initialLevel = this.level = requireNonNull(level, "level");
        this.initialStream = this.stream = requireNonNull(stream, "stream");
=======
        this(level, stream, SimpleLoggerFactory.getInstance());
    }

    StatusConsoleListener(final Level level, final PrintStream stream, final SimpleLoggerFactory loggerFactory) {
        this.level = Objects.requireNonNull(level, "level");
        this.stream = Objects.requireNonNull(stream, "stream");
        this.logger = Objects.requireNonNull(loggerFactory, "loggerFactory")
                .createSimpleLogger(
                        "StatusConsoleListener", level, ParameterizedNoReferenceMessageFactory.INSTANCE, stream);
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
    }

    /**
     * Sets the level to a new value.
<<<<<<< HEAD
     *
     * @param level the new level
     * @throws NullPointerException on null {@code level}
     */
    public void setLevel(final Level level) {
        requireNonNull(level, "level");
        // Check if a mutation (and locking!) is necessary at all
        if (!this.level.equals(level)) {
            lock.lock();
            try {
                this.level = level;
            } finally {
                lock.unlock();
            }
        }
    }

    /**
     * Sets the output stream to a new value.
     *
     * @param stream the new output stream
     * @throws NullPointerException on null {@code stream}
     * @since 2.23.0
     */
    public void setStream(final PrintStream stream) {
        requireNonNull(stream, "stream");
        // Check if a mutation (and locking!) is necessary at all
        if (this.stream != stream) {
            @Nullable OutputStream oldStream = null;
            lock.lock();
            try {
                if (this.stream != stream) {
                    oldStream = this.stream;
                    this.stream = stream;
                }
            } finally {
                lock.unlock();
            }
            if (oldStream != null) {
                closeNonSystemStream(oldStream);
            }
        }
    }

    /**
     * Returns the level for which the listener should receive events.
     *
     * @return the log level
     */
    @Override
    public Level getStatusLevel() {
        return level;
=======
     * @param level The new Level.
     */
    public void setLevel(final Level level) {
        this.level = level;
    }

    /**
     * Return the Log Level for which the Listener should receive events.
     * @return the Log Level.
     */
    @Override
    public Level getStatusLevel() {
        return this.level;
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
    }

    /**
     * Writes status messages to the console.
<<<<<<< HEAD
     *
     * @param data a status data
     * @throws NullPointerException on null {@code data}
     */
    @Override
    public void log(final StatusData data) {
        requireNonNull(data, "data");
        final String formattedStatus = data.getFormattedStatus();
        stream.println(formattedStatus);
=======
     * @param data The StatusData.
     */
    @Override
    public void log(final StatusData data) {
        final boolean filtered = filtered(data);
        if (!filtered) {
            logger
                    // Logging using _only_ the following 4 fields set by `StatusLogger#logMessage()`:
                    .atLevel(data.getLevel())
                    .withThrowable(data.getThrowable())
                    .withLocation(data.getStackTraceElement())
                    .log(data.getMessage());
        }
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
    }

    /**
     * Adds package name filters to exclude.
<<<<<<< HEAD
     *
     * @param filters An array of package names to exclude.
     * @deprecated This method is ineffective and only kept for binary backward compatibility.
     */
    @Deprecated
    public void setFilters(final String... filters) {}

    /**
     * Resets the level and output stream to its initial values, and closes the output stream, if it is a non-system one.
     */
    @Override
    public void close() {
        final OutputStream oldStream;
        lock.lock();
        try {
            oldStream = stream;
            stream = initialStream;
            level = initialLevel;
        } finally {
            lock.unlock();
        }
        closeNonSystemStream(oldStream);
    }

    private static void closeNonSystemStream(final OutputStream stream) {
        // Close only non-system streams
        if (stream != System.out && stream != System.err) {
            try {
                stream.close();
            } catch (IOException error) {
                // We are at the lowest level of the system.
                // Hence, there is nothing better we can do but dumping the failure.
                error.printStackTrace(System.err);
            }
        }
=======
     * @param filters An array of package names to exclude.
     */
    public void setFilters(final String... filters) {
        this.filters = filters;
    }

    private boolean filtered(final StatusData data) {
        if (filters == null) {
            return false;
        }
        final String caller = data.getStackTraceElement().getClassName();
        for (final String filter : filters) {
            if (caller.startsWith(filter)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void close() throws IOException {
        // only want to close non-system streams
        if (this.stream != System.out && this.stream != System.err) {
            this.stream.close();
        }
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
    }
}
