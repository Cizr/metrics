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
import static org.apache.logging.log4j.util.Chars.SPACE;

import edu.umd.cs.findbugs.annotations.Nullable;
=======
import static org.apache.logging.log4j.util.Chars.SPACE;

>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.Serializable;
<<<<<<< HEAD
import java.time.Instant;
import java.time.format.DateTimeFormatter;
=======
import java.text.SimpleDateFormat;
import java.util.Date;
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.message.Message;

/**
 * The Status data.
 */
public class StatusData implements Serializable {

    private static final long serialVersionUID = -4341916115118014017L;

<<<<<<< HEAD
    private final Instant instant;

    private final DateTimeFormatter instantFormatter;

    @Nullable
    private final StackTraceElement caller;

    private final Level level;

    private final Message message;

    private final String threadName;

    @Nullable
    private final Throwable throwable;

    /**
     * Constructs the instance using given properties.
     *
     * @param caller the method that created the event
     * @param level a logging level
     * @param message a message
     * @param throwable the error occurred
     * @param threadName the thread name
     */
    public StatusData(
            @Nullable final StackTraceElement caller,
            final Level level,
            final Message message,
            @Nullable final Throwable throwable,
            @Nullable final String threadName) {
        this(caller, level, message, throwable, threadName, null, Instant.now());
    }

    StatusData(
            @Nullable final StackTraceElement caller,
            final Level level,
            final Message message,
            @Nullable final Throwable throwable,
            @Nullable final String threadName,
            @Nullable final DateTimeFormatter instantFormatter,
            final Instant instant) {
        // DateTimeFormatter.ISO_INSTANT is the default used in instant.toString()
        this.instantFormatter = instantFormatter != null ? instantFormatter : DateTimeFormatter.ISO_INSTANT;
        this.instant = instant;
        this.caller = caller;
        this.level = requireNonNull(level, "level");
        this.message = requireNonNull(message, "message");
        this.throwable = throwable;
        this.threadName =
                threadName != null ? threadName : Thread.currentThread().getName();
    }

    /**
     * Returns the instant of the event.
     *
     * @return the event's instant
     */
    public Instant getInstant() {
        return instant;
    }

    /**
     * Returns the instant of the event.
     *
     * @return the event's instant
     * @deprecated Use {@link #getInstant()} instead.
     */
    @Deprecated
    public long getTimestamp() {
        return instant.toEpochMilli();
    }

    /**
     * Returns the method that created the event.
     *
     * @return the method that created the event
     */
    @Nullable
=======
    private final long timestamp;
    private final StackTraceElement caller;
    private final Level level;
    private final Message msg;
    private String threadName;
    private final Throwable throwable;

    /**
     * Creates the StatusData object.
     *
     * @param caller The method that created the event.
     * @param level The logging level.
     * @param msg The message String.
     * @param t The Error or Exception that occurred.
     * @param threadName The thread name
     */
    public StatusData(
            final StackTraceElement caller,
            final Level level,
            final Message msg,
            final Throwable t,
            final String threadName) {
        this.timestamp = System.currentTimeMillis();
        this.caller = caller;
        this.level = level;
        this.msg = msg;
        this.throwable = t;
        this.threadName = threadName;
    }

    /**
     * Returns the event's timestamp.
     *
     * @return The event's timestamp.
     */
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * Returns the StackTraceElement for the method that created the event.
     *
     * @return The StackTraceElement.
     */
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
    public StackTraceElement getStackTraceElement() {
        return caller;
    }

    /**
     * Returns the logging level for the event.
     *
<<<<<<< HEAD
     * @return the event's logging level
=======
     * @return The logging level.
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
     */
    public Level getLevel() {
        return level;
    }

    /**
     * Returns the message associated with the event.
     *
<<<<<<< HEAD
     * @return the message associated with the event
     */
    public Message getMessage() {
        return message;
    }

    /**
     * Returns the name of the thread associated with the event.
     *
     * @return the name of the thread associated with the event
     */
    public String getThreadName() {
=======
     * @return The message associated with the event.
     */
    public Message getMessage() {
        return msg;
    }

    public String getThreadName() {
        if (threadName == null) {
            threadName = Thread.currentThread().getName();
        }
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
        return threadName;
    }

    /**
<<<<<<< HEAD
     * Returns the error associated with the event.
     *
     * @return the error associated with the event
     */
    @Nullable
=======
     * Returns the Throwable associated with the event.
     *
     * @return The Throwable associated with the event.
     */
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
    public Throwable getThrowable() {
        return throwable;
    }

    /**
<<<<<<< HEAD
     * Formats the event in to a log line for viewing.
     *
     * @return the formatted event
=======
     * Formats the StatusData for viewing.
     *
     * @return The formatted status data as a String.
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
     */
    @SuppressFBWarnings(
            value = "INFORMATION_EXPOSURE_THROUGH_AN_ERROR_MESSAGE",
            justification = "Log4j prints stacktraces only to logs, which should be private.")
    @SuppressWarnings("DefaultCharset")
    public String getFormattedStatus() {
        final StringBuilder sb = new StringBuilder();
<<<<<<< HEAD
        instantFormatter.formatTo(instant, sb);
=======
        final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");
        sb.append(format.format(new Date(timestamp)));
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
        sb.append(SPACE);
        sb.append(getThreadName());
        sb.append(SPACE);
        sb.append(level.toString());
        sb.append(SPACE);
<<<<<<< HEAD
        sb.append(message.getFormattedMessage());
        final Object[] parameters = message.getParameters();
        Throwable effectiveThrowable;
        if (throwable == null && parameters != null && parameters[parameters.length - 1] instanceof Throwable) {
            effectiveThrowable = (Throwable) parameters[parameters.length - 1];
        } else {
            effectiveThrowable = throwable;
        }
        if (effectiveThrowable != null) {
            sb.append(SPACE);
            final ByteArrayOutputStream baos = new ByteArrayOutputStream();
            effectiveThrowable.printStackTrace(new PrintStream(baos));
=======
        sb.append(msg.getFormattedMessage());
        final Object[] params = msg.getParameters();
        Throwable t;
        if (throwable == null && params != null && params[params.length - 1] instanceof Throwable) {
            t = (Throwable) params[params.length - 1];
        } else {
            t = throwable;
        }
        if (t != null) {
            sb.append(SPACE);
            final ByteArrayOutputStream baos = new ByteArrayOutputStream();
            t.printStackTrace(new PrintStream(baos));
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
            /*
             * https://errorprone.info/bugpattern/DefaultCharset
             *
             * Since Java 9 we'll be able to provide a charset.
             */
<<<<<<< HEAD
            sb.append(baos);
=======
            sb.append(baos.toString());
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
        }
        return sb.toString();
    }

<<<<<<< HEAD
=======
    /**
     * Used in tests
     */
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
    @Override
    public String toString() {
        return getMessage().getFormattedMessage();
    }
}
