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
package org.apache.logging.log4j.simple;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
<<<<<<< HEAD
import java.io.PrintStream;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.message.MessageFactory;
import org.apache.logging.log4j.message.ParameterizedMessageFactory;
import org.apache.logging.log4j.simple.internal.SimpleProvider;
=======
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.message.MessageFactory;
import org.apache.logging.log4j.spi.AbstractLogger;
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
import org.apache.logging.log4j.spi.ExtendedLogger;
import org.apache.logging.log4j.spi.LoggerContext;
import org.apache.logging.log4j.spi.LoggerRegistry;
import org.apache.logging.log4j.util.PropertiesUtil;
<<<<<<< HEAD
import org.jspecify.annotations.Nullable;
=======
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c

/**
 * A simple {@link LoggerContext} implementation.
 */
public class SimpleLoggerContext implements LoggerContext {

    /** Singleton instance. */
    static final SimpleLoggerContext INSTANCE = new SimpleLoggerContext();

<<<<<<< HEAD
=======
    private static final String SYSTEM_OUT = "system.out";

    private static final String SYSTEM_ERR = "system.err";

>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
    /** The default format to use when formatting dates */
    protected static final String DEFAULT_DATE_TIME_FORMAT = "yyyy/MM/dd HH:mm:ss:SSS zzz";

    /** All system properties used by <code>SimpleLog</code> start with this */
    protected static final String SYSTEM_PREFIX = "org.apache.logging.log4j.simplelog.";

<<<<<<< HEAD
    private static final MessageFactory DEFAULT_MESSAGE_FACTORY = ParameterizedMessageFactory.INSTANCE;

=======
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
    private final PropertiesUtil props;

    /** Include the instance name in the log message? */
    private final boolean showLogName;

    /**
     * Include the short name (last component) of the logger in the log message. Defaults to true - otherwise we'll be
     * lost in a flood of messages without knowing who sends them.
     */
    private final boolean showShortName;

    /** Include the current time in the log message */
    private final boolean showDateTime;

    /** Include the ThreadContextMap in the log message */
    private final boolean showContextMap;

    /** The date and time format to use in the log message */
    private final String dateTimeFormat;

    private final Level defaultLevel;

    private final PrintStream stream;

    private final LoggerRegistry<ExtendedLogger> loggerRegistry = new LoggerRegistry<>();

    /**
     * Constructs a new initialized instance.
     */
    @SuppressFBWarnings(
            value = "PATH_TRAVERSAL_OUT",
            justification = "Opens a file retrieved from configuration (Log4j properties)")
    public SimpleLoggerContext() {
<<<<<<< HEAD
        final SimpleProvider.Config config = SimpleProvider.Config.INSTANCE;
        props = config.props;
        showContextMap = config.showContextMap;
        showLogName = config.showLogName;
        showShortName = config.showShortName;
        showDateTime = config.showDateTime;
        defaultLevel = config.defaultLevel;
        dateTimeFormat = config.dateTimeFormat;
        stream = config.stream;
=======
        props = new PropertiesUtil("log4j2.simplelog.properties");

        showContextMap = props.getBooleanProperty(SYSTEM_PREFIX + "showContextMap", false);
        showLogName = props.getBooleanProperty(SYSTEM_PREFIX + "showlogname", false);
        showShortName = props.getBooleanProperty(SYSTEM_PREFIX + "showShortLogname", true);
        showDateTime = props.getBooleanProperty(SYSTEM_PREFIX + "showdatetime", false);
        final String lvl = props.getStringProperty(SYSTEM_PREFIX + "level");
        defaultLevel = Level.toLevel(lvl, Level.ERROR);

        dateTimeFormat = showDateTime
                ? props.getStringProperty(
                        SimpleLoggerContext.SYSTEM_PREFIX + "dateTimeFormat", DEFAULT_DATE_TIME_FORMAT)
                : null;

        final String fileName = props.getStringProperty(SYSTEM_PREFIX + "logFile", SYSTEM_ERR);
        PrintStream ps;
        if (SYSTEM_ERR.equalsIgnoreCase(fileName)) {
            ps = System.err;
        } else if (SYSTEM_OUT.equalsIgnoreCase(fileName)) {
            ps = System.out;
        } else {
            try {
                ps = new PrintStream(new FileOutputStream(fileName));
            } catch (final FileNotFoundException fnfe) {
                ps = System.err;
            }
        }
        this.stream = ps;
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
    }

    @Override
    public Object getExternalContext() {
        return null;
    }

    @Override
    public ExtendedLogger getLogger(final String name) {
<<<<<<< HEAD
        return getLogger(name, DEFAULT_MESSAGE_FACTORY);
    }

    @Override
    public ExtendedLogger getLogger(final String name, @Nullable final MessageFactory messageFactory) {
        final MessageFactory effectiveMessageFactory =
                messageFactory != null ? messageFactory : DEFAULT_MESSAGE_FACTORY;
        final ExtendedLogger oldLogger = loggerRegistry.getLogger(name, effectiveMessageFactory);
        if (oldLogger != null) {
            return oldLogger;
        }
        final ExtendedLogger newLogger = createLogger(name, effectiveMessageFactory);
        loggerRegistry.putIfAbsent(name, effectiveMessageFactory, newLogger);
        return loggerRegistry.getLogger(name, effectiveMessageFactory);
    }

    private ExtendedLogger createLogger(final String name, @Nullable final MessageFactory messageFactory) {
        return new SimpleLogger(
=======
        return getLogger(name, null);
    }

    @Override
    public ExtendedLogger getLogger(final String name, final MessageFactory messageFactory) {
        // Note: This is the only method where we add entries to the 'loggerRegistry' ivar.
        final ExtendedLogger extendedLogger = loggerRegistry.getLogger(name, messageFactory);
        if (extendedLogger != null) {
            AbstractLogger.checkMessageFactory(extendedLogger, messageFactory);
            return extendedLogger;
        }
        final SimpleLogger simpleLogger = new SimpleLogger(
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
                name,
                defaultLevel,
                showLogName,
                showShortName,
                showDateTime,
                showContextMap,
                dateTimeFormat,
                messageFactory,
                props,
                stream);
<<<<<<< HEAD
=======
        loggerRegistry.putIfAbsent(name, messageFactory, simpleLogger);
        return loggerRegistry.getLogger(name, messageFactory);
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
    }

    /**
     * Gets the LoggerRegistry.
     *
     * @return the LoggerRegistry.
     * @since 2.17.2
     */
    @Override
    public LoggerRegistry<ExtendedLogger> getLoggerRegistry() {
        return loggerRegistry;
    }

    @Override
    public boolean hasLogger(final String name) {
<<<<<<< HEAD
        return loggerRegistry.hasLogger(name, DEFAULT_MESSAGE_FACTORY);
=======
        return false;
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
    }

    @Override
    public boolean hasLogger(final String name, final Class<? extends MessageFactory> messageFactoryClass) {
<<<<<<< HEAD
        return loggerRegistry.hasLogger(name, messageFactoryClass);
    }

    @Override
    public boolean hasLogger(final String name, @Nullable final MessageFactory messageFactory) {
        final MessageFactory effectiveMessageFactory =
                messageFactory != null ? messageFactory : DEFAULT_MESSAGE_FACTORY;
        return loggerRegistry.hasLogger(name, effectiveMessageFactory);
=======
        return false;
    }

    @Override
    public boolean hasLogger(final String name, final MessageFactory messageFactory) {
        return false;
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
    }
}
