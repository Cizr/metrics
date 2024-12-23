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
package org.apache.logging.log4j.spi;

<<<<<<< HEAD
import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.MessageFactory;
import org.apache.logging.log4j.message.ParameterizedMessageFactory;
import org.jspecify.annotations.Nullable;

/**
 * Convenience class to be used as an {@link ExtendedLogger} registry by {@code LoggerContext} implementations.
 */
public class LoggerRegistry<T extends ExtendedLogger> {

    private final Map<String, Map<MessageFactory, T>> loggerByMessageFactoryByName = new HashMap<>();

    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    private final Lock readLock = lock.readLock();

    private final Lock writeLock = lock.writeLock();

    /**
     * Data structure contract for the internal storage of admitted loggers.
     *
     * @param <T> subtype of {@code ExtendedLogger}
     */
    public interface MapFactory<T extends ExtendedLogger> {

=======
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.apache.logging.log4j.message.MessageFactory;

/**
 * Convenience class to be used by {@code LoggerContext} implementations.
 */
public class LoggerRegistry<T extends ExtendedLogger> {
    private static final String DEFAULT_FACTORY_KEY = AbstractLogger.DEFAULT_MESSAGE_FACTORY_CLASS.getName();
    private final MapFactory<T> factory;
    private final Map<String, Map<String, T>> map;

    /**
     * Interface to control the data structure used by the registry to store the Loggers.
     * @param <T> subtype of {@code ExtendedLogger}
     */
    public interface MapFactory<T extends ExtendedLogger> {
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
        Map<String, T> createInnerMap();

        Map<String, Map<String, T>> createOuterMap();

        void putIfAbsent(Map<String, T> innerMap, String name, T logger);
    }

    /**
<<<<<<< HEAD
     * {@link MapFactory} implementation using {@link ConcurrentHashMap}.
     *
     * @param <T> subtype of {@code ExtendedLogger}
     */
    public static class ConcurrentMapFactory<T extends ExtendedLogger> implements MapFactory<T> {

=======
     * Generates ConcurrentHashMaps for use by the registry to store the Loggers.
     * @param <T> subtype of {@code ExtendedLogger}
     */
    public static class ConcurrentMapFactory<T extends ExtendedLogger> implements MapFactory<T> {
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
        @Override
        public Map<String, T> createInnerMap() {
            return new ConcurrentHashMap<>();
        }

        @Override
        public Map<String, Map<String, T>> createOuterMap() {
            return new ConcurrentHashMap<>();
        }

        @Override
        public void putIfAbsent(final Map<String, T> innerMap, final String name, final T logger) {
<<<<<<< HEAD
            innerMap.putIfAbsent(name, logger);
=======
            ((ConcurrentMap<String, T>) innerMap).putIfAbsent(name, logger);
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
        }
    }

    /**
<<<<<<< HEAD
     * {@link MapFactory} implementation using {@link WeakHashMap}.
     *
     * @param <T> subtype of {@code ExtendedLogger}
     */
    public static class WeakMapFactory<T extends ExtendedLogger> implements MapFactory<T> {

=======
     * Generates WeakHashMaps for use by the registry to store the Loggers.
     * @param <T> subtype of {@code ExtendedLogger}
     */
    public static class WeakMapFactory<T extends ExtendedLogger> implements MapFactory<T> {
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
        @Override
        public Map<String, T> createInnerMap() {
            return new WeakHashMap<>();
        }

        @Override
        public Map<String, Map<String, T>> createOuterMap() {
            return new WeakHashMap<>();
        }

        @Override
        public void putIfAbsent(final Map<String, T> innerMap, final String name, final T logger) {
            innerMap.put(name, logger);
        }
    }

<<<<<<< HEAD
    public LoggerRegistry() {}

    /**
     * Constructs an instance <b>ignoring</b> the given the map factory.
     *
     * @param mapFactory a map factory
     */
    public LoggerRegistry(final MapFactory<T> mapFactory) {
        this();
    }

    /**
     * Returns the logger associated with the given name.
     * <p>
     * There can be made no assumptions on the message factory of the returned logger.
     * Callers are strongly advised to switch to {@link #getLogger(String, MessageFactory)} and <b>provide a message factory parameter!</b>
     * </p>
     *
     * @param name a logger name
     * @return the logger associated with the name
     */
    public @Nullable T getLogger(final String name) {
        requireNonNull(name, "name");
        return getLogger(name, null);
    }

    /**
     * Returns the logger associated with the given name and message factory.
     * <p>
     * In the absence of a message factory, there can be made no assumptions on the message factory of the returned logger.
     * This lenient behaviour is only kept for backward compatibility.
     * Callers are strongly advised to <b>provide a message factory parameter to the method!</b>
     * </p>
     *
     * @param name a logger name
     * @param messageFactory a message factory
     * @return the logger associated with the given name and message factory
     */
    public @Nullable T getLogger(final String name, @Nullable final MessageFactory messageFactory) {
        requireNonNull(name, "name");
        readLock.lock();
        try {
            final @Nullable Map<MessageFactory, T> loggerByMessageFactory = loggerByMessageFactoryByName.get(name);
            final MessageFactory effectiveMessageFactory =
                    messageFactory != null ? messageFactory : ParameterizedMessageFactory.INSTANCE;
            return loggerByMessageFactory == null ? null : loggerByMessageFactory.get(effectiveMessageFactory);
        } finally {
            readLock.unlock();
        }
    }

    public Collection<T> getLoggers() {
        readLock.lock();
        try {
            return loggerByMessageFactoryByName.values().stream()
                    .flatMap(loggerByMessageFactory -> loggerByMessageFactory.values().stream())
                    .collect(Collectors.toList());
        } finally {
            readLock.unlock();
        }
    }

    public Collection<T> getLoggers(final Collection<T> destination) {
        requireNonNull(destination, "destination");
        destination.addAll(getLoggers());
        return destination;
    }

    /**
     * Checks if a logger associated with the given name exists.
     * <p>
     * There can be made no assumptions on the message factory of the found logger.
     * Callers are strongly advised to switch to {@link #hasLogger(String, MessageFactory)} and <b>provide a message factory parameter!</b>
     * </p>
     *
     * @param name a logger name
     * @return {@code true}, if the logger exists; {@code false} otherwise.
     */
    public boolean hasLogger(final String name) {
        requireNonNull(name, "name");
        final @Nullable T logger = getLogger(name);
        return logger != null;
    }

    /**
     * Checks if a logger associated with the given name and message factory exists.
     * <p>
     * In the absence of a message factory, there can be made no assumptions on the message factory of the found logger.
     * This lenient behaviour is only kept for backward compatibility.
     * Callers are strongly advised to <b>provide a message factory parameter to the method!</b>
     * </p>
     *
     * @param name a logger name
     * @param messageFactory a message factory
     * @return {@code true}, if the logger exists; {@code false} otherwise.
     * @since 2.5
     */
    public boolean hasLogger(final String name, final MessageFactory messageFactory) {
        requireNonNull(name, "name");
        final @Nullable T logger = getLogger(name, messageFactory);
        return logger != null;
    }

    /**
     * Checks if a logger associated with the given name and message factory type exists.
     *
     * @param name a logger name
     * @param messageFactoryClass a message factory class
     * @return {@code true}, if the logger exists; {@code false} otherwise.
     * @since 2.5
     */
    public boolean hasLogger(final String name, final Class<? extends MessageFactory> messageFactoryClass) {
        requireNonNull(name, "name");
        requireNonNull(messageFactoryClass, "messageFactoryClass");
        readLock.lock();
        try {
            return loggerByMessageFactoryByName.getOrDefault(name, Collections.emptyMap()).keySet().stream()
                    .anyMatch(messageFactory -> messageFactoryClass.equals(messageFactory.getClass()));
        } finally {
            readLock.unlock();
        }
    }

    /**
     * Registers the provided logger.
     * <p>
     *   The logger will be registered using the keys provided by the {@code name} and {@code messageFactory} parameters
     *   and the values of {@link Logger#getName()} and {@link Logger#getMessageFactory()}.
     * </p>
     *
     * @param name a logger name
     * @param messageFactory a message factory
     * @param logger a logger instance
     */
    public void putIfAbsent(final String name, @Nullable final MessageFactory messageFactory, final T logger) {

        // Check arguments
        requireNonNull(name, "name");
        requireNonNull(logger, "logger");

        // Insert the logger
        writeLock.lock();
        try {
            final MessageFactory effectiveMessageFactory =
                    messageFactory != null ? messageFactory : ParameterizedMessageFactory.INSTANCE;
            // Register using the keys provided by the caller
            loggerByMessageFactoryByName
                    .computeIfAbsent(name, this::createLoggerRefByMessageFactoryMap)
                    .putIfAbsent(effectiveMessageFactory, logger);
            // Also register using the values extracted from `logger`
            if (!name.equals(logger.getName()) || !effectiveMessageFactory.equals(logger.getMessageFactory())) {
                loggerByMessageFactoryByName
                        .computeIfAbsent(logger.getName(), this::createLoggerRefByMessageFactoryMap)
                        .putIfAbsent(logger.getMessageFactory(), logger);
            }
        } finally {
            writeLock.unlock();
        }
    }

    private Map<MessageFactory, T> createLoggerRefByMessageFactoryMap(final String ignored) {
        return new WeakHashMap<>();
=======
    public LoggerRegistry() {
        this(new ConcurrentMapFactory<T>());
    }

    public LoggerRegistry(final MapFactory<T> factory) {
        this.factory = Objects.requireNonNull(factory, "factory");
        this.map = factory.createOuterMap();
    }

    private static String factoryClassKey(final Class<? extends MessageFactory> messageFactoryClass) {
        return messageFactoryClass == null ? DEFAULT_FACTORY_KEY : messageFactoryClass.getName();
    }

    private static String factoryKey(final MessageFactory messageFactory) {
        return messageFactory == null
                ? DEFAULT_FACTORY_KEY
                : messageFactory.getClass().getName();
    }

    /**
     * Returns an ExtendedLogger.
     * @param name The name of the Logger to return.
     * @return The logger with the specified name.
     */
    public T getLogger(final String name) {
        return getOrCreateInnerMap(DEFAULT_FACTORY_KEY).get(name);
    }

    /**
     * Returns an ExtendedLogger.
     * @param name The name of the Logger to return.
     * @param messageFactory The message factory is used only when creating a logger, subsequent use does not change
     *                       the logger but will log a warning if mismatched.
     * @return The logger with the specified name.
     */
    public T getLogger(final String name, final MessageFactory messageFactory) {
        return getOrCreateInnerMap(factoryKey(messageFactory)).get(name);
    }

    public Collection<T> getLoggers() {
        return getLoggers(new ArrayList<T>());
    }

    public Collection<T> getLoggers(final Collection<T> destination) {
        for (final Map<String, T> inner : map.values()) {
            destination.addAll(inner.values());
        }
        return destination;
    }

    private Map<String, T> getOrCreateInnerMap(final String factoryName) {
        Map<String, T> inner = map.get(factoryName);
        if (inner == null) {
            inner = factory.createInnerMap();
            map.put(factoryName, inner);
        }
        return inner;
    }

    /**
     * Detects if a Logger with the specified name exists.
     * @param name The Logger name to search for.
     * @return true if the Logger exists, false otherwise.
     */
    public boolean hasLogger(final String name) {
        return getOrCreateInnerMap(DEFAULT_FACTORY_KEY).containsKey(name);
    }

    /**
     * Detects if a Logger with the specified name and MessageFactory exists.
     * @param name The Logger name to search for.
     * @param messageFactory The message factory to search for.
     * @return true if the Logger exists, false otherwise.
     * @since 2.5
     */
    public boolean hasLogger(final String name, final MessageFactory messageFactory) {
        return getOrCreateInnerMap(factoryKey(messageFactory)).containsKey(name);
    }

    /**
     * Detects if a Logger with the specified name and MessageFactory type exists.
     * @param name The Logger name to search for.
     * @param messageFactoryClass The message factory class to search for.
     * @return true if the Logger exists, false otherwise.
     * @since 2.5
     */
    public boolean hasLogger(final String name, final Class<? extends MessageFactory> messageFactoryClass) {
        return getOrCreateInnerMap(factoryClassKey(messageFactoryClass)).containsKey(name);
    }

    public void putIfAbsent(final String name, final MessageFactory messageFactory, final T logger) {
        factory.putIfAbsent(getOrCreateInnerMap(factoryKey(messageFactory)), name, logger);
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
    }
}
