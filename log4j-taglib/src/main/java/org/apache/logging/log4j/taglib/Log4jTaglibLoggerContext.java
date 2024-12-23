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
package org.apache.logging.log4j.taglib;

<<<<<<< HEAD
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import javax.servlet.ServletContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.message.MessageFactory;
import org.apache.logging.log4j.message.ParameterizedMessageFactory;
=======
import java.util.WeakHashMap;
import javax.servlet.ServletContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.message.MessageFactory;
import org.apache.logging.log4j.spi.AbstractLogger;
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
import org.apache.logging.log4j.spi.ExtendedLogger;
import org.apache.logging.log4j.spi.LoggerContext;
import org.apache.logging.log4j.spi.LoggerRegistry;

/**
 * This bridge between the tag library and the Log4j API ensures that instances of {@link Log4jTaglibLogger} are
 * appropriately held in memory and not constantly recreated.
 *
 * @since 2.0
 */
final class Log4jTaglibLoggerContext implements LoggerContext {
<<<<<<< HEAD

    private static final ReadWriteLock LOCK = new ReentrantReadWriteLock();

    private static final Lock READ_LOCK = LOCK.readLock();

    private static final Lock WRITE_LOCK = LOCK.writeLock();

    private static final Map<ServletContext, Log4jTaglibLoggerContext> LOGGER_CONTEXT_BY_SERVLET_CONTEXT =
            new WeakHashMap<>();

    private static final MessageFactory DEFAULT_MESSAGE_FACTORY = ParameterizedMessageFactory.INSTANCE;

    private final LoggerRegistry<Log4jTaglibLogger> loggerRegistry = new LoggerRegistry<>();
=======
    // These were change to WeakHashMaps to avoid ClassLoader (memory) leak, something that's particularly
    // important in Servlet containers.
    private static final WeakHashMap<ServletContext, Log4jTaglibLoggerContext> CONTEXTS = new WeakHashMap<>();

    private final LoggerRegistry<Log4jTaglibLogger> loggerRegistry =
            new LoggerRegistry<>(new LoggerRegistry.WeakMapFactory<Log4jTaglibLogger>());
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c

    private final ServletContext servletContext;

    private Log4jTaglibLoggerContext(final ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @Override
    public Object getExternalContext() {
<<<<<<< HEAD
        return servletContext;
=======
        return this.servletContext;
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
    }

    @Override
    public Log4jTaglibLogger getLogger(final String name) {
<<<<<<< HEAD
        return getLogger(name, DEFAULT_MESSAGE_FACTORY);
=======
        return this.getLogger(name, null);
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
    }

    @Override
    public Log4jTaglibLogger getLogger(final String name, final MessageFactory messageFactory) {
<<<<<<< HEAD
        final MessageFactory effectiveMessageFactory =
                messageFactory != null ? messageFactory : DEFAULT_MESSAGE_FACTORY;
        final Log4jTaglibLogger oldLogger = loggerRegistry.getLogger(name, effectiveMessageFactory);
        if (oldLogger != null) {
            return oldLogger;
        }
        final Log4jTaglibLogger newLogger = createLogger(name, effectiveMessageFactory);
        loggerRegistry.putIfAbsent(name, effectiveMessageFactory, newLogger);
        return loggerRegistry.getLogger(name, effectiveMessageFactory);
    }

    private Log4jTaglibLogger createLogger(final String name, final MessageFactory messageFactory) {
        final LoggerContext loggerContext = LogManager.getContext(false);
        final ExtendedLogger delegateLogger = loggerContext.getLogger(name, messageFactory);
        return new Log4jTaglibLogger(delegateLogger, name, delegateLogger.getMessageFactory());
=======
        // Note: This is the only method where we add entries to the 'loggerRegistry' ivar.
        Log4jTaglibLogger logger = this.loggerRegistry.getLogger(name, messageFactory);
        if (logger != null) {
            AbstractLogger.checkMessageFactory(logger, messageFactory);
            return logger;
        }

        synchronized (this.loggerRegistry) {
            logger = this.loggerRegistry.getLogger(name, messageFactory);
            if (logger == null) {
                final LoggerContext context = LogManager.getContext(false);
                final ExtendedLogger original =
                        messageFactory == null ? context.getLogger(name) : context.getLogger(name, messageFactory);
                // wrap a logger from an underlying implementation
                logger = new Log4jTaglibLogger(original, name, original.getMessageFactory());
                this.loggerRegistry.putIfAbsent(name, messageFactory, logger);
            }
        }

        return logger;
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
    }

    @Override
    public boolean hasLogger(final String name) {
<<<<<<< HEAD
        return loggerRegistry.hasLogger(name, DEFAULT_MESSAGE_FACTORY);
=======
        return loggerRegistry.hasLogger(name);
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
    }

    @Override
    public boolean hasLogger(final String name, final MessageFactory messageFactory) {
<<<<<<< HEAD
        final MessageFactory effectiveMessageFactory =
                messageFactory != null ? messageFactory : DEFAULT_MESSAGE_FACTORY;
        return loggerRegistry.hasLogger(name, effectiveMessageFactory);
=======
        return loggerRegistry.hasLogger(name, messageFactory);
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
    }

    @Override
    public boolean hasLogger(final String name, final Class<? extends MessageFactory> messageFactoryClass) {
        return loggerRegistry.hasLogger(name, messageFactoryClass);
    }

<<<<<<< HEAD
    static Log4jTaglibLoggerContext getInstance(final ServletContext servletContext) {

        // Get the associated logger context, if exists
        READ_LOCK.lock();
        try {
            final Log4jTaglibLoggerContext loggerContext = LOGGER_CONTEXT_BY_SERVLET_CONTEXT.get(servletContext);
            if (loggerContext != null) {
                return loggerContext;
            }
        } finally {
            READ_LOCK.unlock();
        }

        // Create the logger context
        WRITE_LOCK.lock();
        try {
            return LOGGER_CONTEXT_BY_SERVLET_CONTEXT.computeIfAbsent(servletContext, Log4jTaglibLoggerContext::new);
        } finally {
            WRITE_LOCK.unlock();
        }
=======
    static synchronized Log4jTaglibLoggerContext getInstance(final ServletContext servletContext) {
        Log4jTaglibLoggerContext loggerContext = CONTEXTS.get(servletContext);
        if (loggerContext != null) {
            return loggerContext;
        }

        synchronized (CONTEXTS) {
            loggerContext = CONTEXTS.get(servletContext);
            if (loggerContext == null) {
                loggerContext = new Log4jTaglibLoggerContext(servletContext);
                CONTEXTS.put(servletContext, loggerContext);
            }
        }

        return loggerContext;
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
    }
}
