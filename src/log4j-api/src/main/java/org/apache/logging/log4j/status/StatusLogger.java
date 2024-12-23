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
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
=======
import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.MessageFactory;
import org.apache.logging.log4j.message.ParameterizedNoReferenceMessageFactory;
<<<<<<< HEAD
import org.apache.logging.log4j.spi.AbstractLogger;
import org.apache.logging.log4j.util.Constants;

/**
 * Records events that occur in the logging system.
 * {@link StatusLogger} is expected to be a standalone, self-sufficient component that the logging system can rely on for low-level logging purposes.
 * <h2>Listeners</h3>
 * <p>
 * Each recorded event will first get buffered and then used to notify the registered {@link StatusListener}s.
 * If none are available, the <em>fallback listener</em> of type {@link StatusConsoleListener} will be used.
 * </p>
 * <p>
 * You can programmatically register listeners using {@link #registerListener(StatusListener)} method.
 * </p>
 * <h3>Configuration</h3>
 * <p>
 * The {@code StatusLogger} can be configured in following ways:
 * </p>
 * <ol>
 * <li>Passing system properties to the Java process (e.g., {@code -Dlog4j2.StatusLogger.level=INFO})</li>
 * <li>Providing properties in a {@value StatusLogger#PROPERTIES_FILE_NAME} file in the classpath</li>
 * <li>Using Log4j configuration (i.e., {@code <Configuration status="WARN" dest="out">} in a {@code log4j2.xml} in the classpath)</li>
 * <li>Programmatically (e.g., {@code StatusLogger.getLogger().setLevel(Level.WARN)})</li>
 * </ol>
 * <p>
 * It is crucial to understand that there is a time between the first {@code StatusLogger} access and a configuration file (e.g., {@code log4j2.xml}) read.
 * Consider the following example:
 * </p>
 * <ol>
 * <li>The default level (of fallback listener) is {@code ERROR}</li>
 * <li>You have {@code <Configuration status="WARN">} in your {@code log4j2.xml}</li>
 * <li>Until your {@code log4j2.xml} configuration is read, the effective level will be {@code ERROR}</li>
 * <li>Once your {@code log4j2.xml} configuration is read, the effective level will be {@code WARN} as you configured</li>
 * </ol>
 * <p>
 * Hence, unless you use either system properties or {@value StatusLogger#PROPERTIES_FILE_NAME} file in the classpath, there is a time window that only the defaults will be effective.
 * </p>
 * <p>
 * {@code StatusLogger} is designed as a singleton class accessed statically.
 * If you are running an application containing multiple Log4j configurations (e.g., in a servlet environment with multiple containers) and you happen to have differing {@code StatusLogger} configurations (e.g, one {@code log4j2.xml} containing {@code <Configuration status="ERROR">} while the other {@code <Configuration status="INFO">}), the last loaded configuration will be the effective one.
 * </p>
 * <h2>Configuration properties</h3>
 * <p>
 * The list of available properties for configuring the {@code StatusLogger} is shared below.
 * </p>
 * <table>
 * <caption>available properties for configuring the <code>StatusLogger</code></caption>
 * <tr>
 *     <th>Name</th>
 *     <th>Default</th>
 *     <th>Description</th>
 * </tr>
 * <tr>
 *     <td><code>{@value MAX_STATUS_ENTRIES}</code></td>
 *     <td>0</td>
 *     <td>
 *         The maximum number of events buffered.
 *         Once the limit is reached, older entries will be removed as new entries are added.
 *     </td>
 * </tr>
 * <tr>
 *     <td><code>{@value DEFAULT_STATUS_LISTENER_LEVEL}</code></td>
 *     <td>{@code ERROR}</td>
 *     <td>
 *         The {@link Level} name to use as the fallback listener level.<br/>
 *         The fallback listener is used when the listener registry is empty.
 *         The fallback listener will accept entries filtered by the level provided in this configuration.
 *     </td>
 * </tr>
 * <tr>
 *     <td><code>{@value STATUS_DATE_FORMAT}</code></td>
 *     <td>{@code null}</td>
 *     <td>A {@link java.time.format.DateTimeFormatter} pattern to format the created {@link StatusData}.</td>
 * </tr>
 * <tr>
 *     <td><code>{@value #DEBUG_PROPERTY_NAME}</code></td>
 *     <td>false</td>
 *     <td>The debug mode toggle.</td>
 * </tr>
 * </table>
 * <h2>Debug mode</h3>
 * <p>
 * When the {@value Constants#LOG4J2_DEBUG} system property is present, any level-related filtering will be skipped and all events will be notified to listeners.
 * If no listeners are available, the <em>fallback listener</em> of type {@link StatusConsoleListener} will be used.
 * </p>
 */
public class StatusLogger extends AbstractLogger {

    private static final long serialVersionUID = 2L;

    /**
     * The name of the system property that enables debug mode in its presence.
     * <p>
     * This is a local clone of {@link Constants#LOG4J2_DEBUG}.
     * The cloning is necessary to avoid cyclic initialization.
     * </p>
     */
    private static final String DEBUG_PROPERTY_NAME = "log4j2.debug";

    /**
     * The name of the system property that can be configured with the maximum number of events buffered.
     * <p>
     * Once the limit is reached, older entries will be removed as new entries are added.
     * </p>
=======
import org.apache.logging.log4j.simple.SimpleLogger;
import org.apache.logging.log4j.simple.SimpleLoggerContext;
import org.apache.logging.log4j.spi.AbstractLogger;
import org.apache.logging.log4j.util.Constants;
import org.apache.logging.log4j.util.PropertiesUtil;

/**
 * Records events that occur in the logging system. By default, only error messages are logged to {@link System#err}.
 * Normally, the Log4j StatusLogger is configured via the root {@code <Configuration status="LEVEL"/>} node in a Log4j
 * configuration file. However, this can be overridden via a system property named
 * {@value #DEFAULT_STATUS_LISTENER_LEVEL} and will work with any Log4j provider.
 *
 * @see SimpleLogger
 * @see SimpleLoggerContext
 */
public final class StatusLogger extends AbstractLogger {

    /**
     * System property that can be configured with the number of entries in the queue. Once the limit is reached older
     * entries will be removed as new entries are added.
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
     */
    public static final String MAX_STATUS_ENTRIES = "log4j2.status.entries";

    /**
<<<<<<< HEAD
     * The default fallback listener buffer capacity.
     * <p>
     * This constant is intended for tests.
     * </p>
     *
     * @see #MAX_STATUS_ENTRIES
     */
    static final int DEFAULT_FALLBACK_LISTENER_BUFFER_CAPACITY = 0;

    /**
     * The name of the system property that can be configured with the {@link Level} name to use as the fallback listener level.
     * <p>
     * The fallback listener is used when the listener registry is empty.
     * The fallback listener will accept entries filtered by the level provided in this configuration.
     * </p>
     *
     * @since 2.8
=======
     * System property that can be configured with the {@link Level} name to use as the default level for
     * {@link StatusListener}s.
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
     */
    public static final String DEFAULT_STATUS_LISTENER_LEVEL = "log4j2.StatusLogger.level";

    /**
<<<<<<< HEAD
     * The default fallback listener level.
     * <p>
     * This constant is intended for tests and indeed makes things awfully confusing given the {@link #DEFAULT_STATUS_LISTENER_LEVEL} property, which is actually intended to be a property <em>name</em>, not its <em>default value</em>.
     * </p>
     */
    static final Level DEFAULT_FALLBACK_LISTENER_LEVEL = Level.ERROR;

    /**
     * The name of the system property that can be configured with a {@link java.time.format.DateTimeFormatter} pattern that will be used while formatting the created {@link StatusData}.
     * <p>
     * When not provided, {@link Instant#toString()} will be used.
     * </p>
     *
     * @see #STATUS_DATE_FORMAT_ZONE
     * @since 2.11.0
     */
    public static final String STATUS_DATE_FORMAT = "log4j2.StatusLogger.dateFormat";

    /**
     * The name of the system property that can be configured with a time-zone ID (e.g., {@code Europe/Amsterdam}, {@code UTC+01:00}) that will be used while formatting the created {@link StatusData}.
     * <p>
     * When not provided, {@link ZoneId#systemDefault()} will be used.
     * </p>
     *
     * @see #STATUS_DATE_FORMAT
     * @since 2.23.1
     */
    static final String STATUS_DATE_FORMAT_ZONE = "log4j2.StatusLogger.dateFormatZone";

    /**
     * The name of the file to be searched in the classpath to read properties from.
     *
     * @since 2.23.0
     */
    public static final String PROPERTIES_FILE_NAME = "log4j2.StatusLogger.properties";

    /**
     * Holder for user-provided {@link StatusLogger} configurations.
     *
     * @since 2.23.0
     */
    public static final class Config {

        private static final Config INSTANCE = new Config();

        // Visible for tests
        final boolean debugEnabled;

        // Visible for tests
        final int bufferCapacity;

        // Visible for tests
        @Nullable
        final Level fallbackListenerLevel;

        // Visible for tests
        @Nullable
        final DateTimeFormatter instantFormatter;

        /**
         * Constructs an instance using the given properties.
         * <b>Users should not create new instances, but use {@link #getInstance()} instead</b>!
         *
         * @param debugEnabled the value of the {@value DEBUG_PROPERTY_NAME} property
         * @param bufferCapacity the value of the {@value MAX_STATUS_ENTRIES} property
         * @param instantFormatter the value of the {@value STATUS_DATE_FORMAT} property
         */
        public Config(boolean debugEnabled, int bufferCapacity, @Nullable DateTimeFormatter instantFormatter) {
            this.debugEnabled = debugEnabled;
            if (bufferCapacity < 0) {
                throw new IllegalArgumentException(
                        "was expecting a positive `bufferCapacity`, found: " + bufferCapacity);
            }
            this.bufferCapacity = bufferCapacity;
            // Public ctor intentionally doesn't set `fallbackListenerLevel`.
            // Because, if public ctor is used, it means user is programmatically creating a `Config` instance.
            // Hence, they will use the public `StatusLogger` ctor too.
            // There they need to provide the fallback listener explicitly anyway.
            // Therefore, there is no need to ask for a `fallbackListenerLevel` here.
            // Since this `fallbackListenerLevel` is only used by the private `StatusLogger` ctor.
            this.fallbackListenerLevel = null;
            this.instantFormatter = instantFormatter;
        }

        /**
         * Constructs the default instance using system properties and a property file (i.e., {@value Config#PROPERTIES_FILE_NAME}) in the classpath, if available.
         */
        private Config() {
            this(PropertiesUtilsDouble.readAllAvailableProperties());
        }

        /**
         * A low-level constructor intended for tests.
         */
        Config(final Properties... propertiesList) {
            this(PropertiesUtilsDouble.normalizeProperties(propertiesList));
        }

        /**
         * The lowest-level constructor.
         */
        private Config(final Map<String, Object> normalizedProperties) {
            this.debugEnabled = readDebugEnabled(normalizedProperties);
            this.bufferCapacity = readBufferCapacity(normalizedProperties);
            this.fallbackListenerLevel = readFallbackListenerLevel(normalizedProperties);
            this.instantFormatter = readInstantFormatter(normalizedProperties);
        }

        /**
         * Gets the static instance.
         *
         * @return a singleton instance
         */
        public static Config getInstance() {
            return INSTANCE;
        }

        private static boolean readDebugEnabled(final Map<String, Object> normalizedProperties) {
            final String debug = PropertiesUtilsDouble.readProperty(normalizedProperties, DEBUG_PROPERTY_NAME);
            return debug != null && !"false".equalsIgnoreCase(debug);
        }

        private static int readBufferCapacity(final Map<String, Object> normalizedProperties) {
            final String propertyName = MAX_STATUS_ENTRIES;
            final String capacityString = PropertiesUtilsDouble.readProperty(normalizedProperties, propertyName);
            final int defaultCapacity = DEFAULT_FALLBACK_LISTENER_BUFFER_CAPACITY;
            int effectiveCapacity = defaultCapacity;
            if (capacityString != null) {
                try {
                    final int capacity = Integer.parseInt(capacityString);
                    if (capacity < 0) {
                        final String message =
                                String.format("was expecting a positive buffer capacity, found: %d", capacity);
                        throw new IllegalArgumentException(message);
                    }
                    effectiveCapacity = capacity;
                } catch (final Exception error) {
                    final String message = String.format(
                            "Failed reading the buffer capacity from the `%s` property: `%s`. Falling back to the default: %d.",
                            propertyName, capacityString, defaultCapacity);
                    final IllegalArgumentException extendedError = new IllegalArgumentException(message, error);
                    // There is no logging system at this stage.
                    // There is nothing we can do but simply dumping the failure.
                    extendedError.printStackTrace(System.err);
                }
            }
            return effectiveCapacity;
        }

        private static Level readFallbackListenerLevel(final Map<String, Object> normalizedProperties) {
            final String propertyName = DEFAULT_STATUS_LISTENER_LEVEL;
            final String level = PropertiesUtilsDouble.readProperty(normalizedProperties, propertyName);
            final Level defaultLevel = DEFAULT_FALLBACK_LISTENER_LEVEL;
            try {
                return level != null ? Level.valueOf(level) : defaultLevel;
            } catch (final Exception error) {
                final String message = String.format(
                        "Failed reading the level from the `%s` property: `%s`. Falling back to the default: `%s`.",
                        propertyName, level, defaultLevel);
                final IllegalArgumentException extendedError = new IllegalArgumentException(message, error);
                // There is no logging system at this stage.
                // There is nothing we can do but simply dumping the failure.
                extendedError.printStackTrace(System.err);
                return defaultLevel;
            }
        }

        @Nullable
        private static DateTimeFormatter readInstantFormatter(final Map<String, Object> normalizedProperties) {

            // Read the format
            final String formatPropertyName = STATUS_DATE_FORMAT;
            final String format = PropertiesUtilsDouble.readProperty(normalizedProperties, formatPropertyName);
            if (format == null) {
                return null;
            }
            final DateTimeFormatter formatter;
            try {
                formatter = DateTimeFormatter.ofPattern(format);
            } catch (final Exception error) {
                final String message = String.format(
                        "failed reading the instant format from the `%s` property: `%s`", formatPropertyName, format);
                final IllegalArgumentException extendedError = new IllegalArgumentException(message, error);
                // There is no logging system at this stage.
                // There is nothing we can do but simply dumping the failure.
                extendedError.printStackTrace(System.err);
                return null;
            }

            // Read the zone
            final String zonePropertyName = STATUS_DATE_FORMAT_ZONE;
            final String zoneIdString = PropertiesUtilsDouble.readProperty(normalizedProperties, zonePropertyName);
            final ZoneId defaultZoneId = ZoneId.systemDefault();
            ZoneId zoneId = defaultZoneId;
            if (zoneIdString != null) {
                try {
                    zoneId = ZoneId.of(zoneIdString);
                } catch (final Exception error) {
                    final String message = String.format(
                            "Failed reading the instant formatting zone ID from the `%s` property: `%s`. Falling back to the default: `%s`.",
                            zonePropertyName, zoneIdString, defaultZoneId);
                    final IllegalArgumentException extendedError = new IllegalArgumentException(message, error);
                    // There is no logging system at this stage.
                    // There is nothing we can do but simply dumping the failure.
                    extendedError.printStackTrace(System.err);
                }
            }
            return formatter.withZone(zoneId);
        }
    }

    /**
     * This is a thin double of {@link org.apache.logging.log4j.util.PropertiesUtil}.
     * <p>
     * We could have used {@code PropertiesUtil}, {@link org.apache.logging.log4j.util.PropertyFilePropertySource}, etc.
     * Consequently, they would delegate to {@link org.apache.logging.log4j.util.LoaderUtil}, etc.
     * All these mechanisms expect a working {@code StatusLogger}.
     * In order to be self-sufficient, we cannot rely on them, hence this <em>double</em>.
     * </p>
     */
    static final class PropertiesUtilsDouble {

        @Nullable
        static String readProperty(final Map<String, Object> normalizedProperties, final String propertyName) {
            final String normalizedPropertyName = normalizePropertyName(propertyName);
            final Object value = normalizedProperties.get(normalizedPropertyName);
            return (value instanceof String) ? (String) value : null;
        }

        static Map<String, Object> readAllAvailableProperties() {
            final Properties systemProperties = System.getProperties();
            final Properties environmentProperties = readEnvironmentProperties();
            final Properties fileProvidedProperties = readPropertiesFile(PROPERTIES_FILE_NAME);
            return normalizeProperties(systemProperties, environmentProperties, fileProvidedProperties);
        }

        private static Properties readEnvironmentProperties() {
            final Properties properties = new Properties();
            properties.putAll(System.getenv());
            return properties;
        }

        // We need to roll out our own `.properties` reader.
        // We could have used `PropertiesUtil`, `PropertyFilePropertySource`, etc.
        // Consequently, they would delegate to `LoaderUtil`, etc.
        // All these mechanisms expect a working `StatusLogger`.
        // Hence, in order to be self-sufficient, we cannot rely on them.
        static Properties readPropertiesFile(final String propertiesFileName) {
            final Properties properties = new Properties();
            // Unlike `ClassLoader#getResource()`, which takes absolute resource paths, `Class#getResource()` supports
            // relative resource paths. Without a `/` prefix, the resource must be placed into JAR resources as
            // `org/apache/logging/log4j/status/log4j2.StatusLogger.properties`. Hence, the `/` prefix.
            final String resourceName = '/' + propertiesFileName;
            final URL url = StatusLogger.class.getResource(resourceName);
            if (url == null) {
                return properties;
            }
            try (final InputStream stream = url.openStream()) {
                properties.load(stream);
            } catch (final IOException error) {
                final String message = String.format("failed reading properties from `%s`", propertiesFileName);
                final RuntimeException extendedError = new RuntimeException(message, error);
                // There is no logging system at this stage.
                // There is nothing we can do but simply dumping the failure.
                extendedError.printStackTrace(System.err);
            }
            return properties;
        }

        private static Map<String, Object> normalizeProperties(Properties... propertiesList) {
            Map<String, Object> map = new HashMap<>();
            for (Properties properties : propertiesList) {
                properties.forEach((name, value) -> {
                    final boolean relevant = isRelevantPropertyName(name);
                    if (relevant) {
                        final String normalizedName = normalizePropertyName((String) name);
                        map.put(normalizedName, value);
                    }
                });
            }
            return map;
        }

        /**
         * Filter to exclude irrelevant property names (i.e., non-string and not {@code log4j}-prefixed) to speed up matching.
         * @param propertyName a property name
         * @return {@code true}, if the property name is relevant; {@code false}, otherwise
         */
        private static boolean isRelevantPropertyName(@Nullable final Object propertyName) {
            return propertyName instanceof String && ((String) propertyName).matches("^(?i)log4j.*");
        }

        /**
         * An imperfect property name normalization routine.
         * <p>
         * It is imperfect, because {@code foo.bar} would match with {@code fo.obar}.
         * But it is good enough for the {@code StatusLogger} needs.
         * </p>
         *
         * @param propertyName the input property name
         * @return the normalized property name
         */
        private static String normalizePropertyName(final String propertyName) {
            return propertyName
                    // Remove separators:
                    // - dots (properties)
                    // - dashes (kebab-case)
                    // - underscores (environment variables)
                    .replaceAll("[._-]", "")
                    // Replace all non-ASCII characters.
                    // Don't remove, otherwise `fooàö` would incorrectly match with `foo`.
                    // It is safe to replace them with dots, since we've just removed all dots above.
                    .replaceAll("\\P{InBasic_Latin}", ".")
                    // Lowercase ASCII – this is safe, since we've just removed all non-ASCII
                    .toLowerCase(Locale.US)
                    .replaceAll("^log4j2", "log4j");
        }
    }

    /**
     * Wrapper for the default instance for lazy initialization.
     * <p>
     * The initialization will be performed when the JVM initializes the class.
     * Since {@code InstanceHolder} has no other fields or methods, class initialization occurs when the {@code INSTANCE} field is first referenced.
     * </p>
     *
     * @see <a href="https://www.infoworld.com/article/2074979/double-checked-locking--clever--but-broken.html?page=2">Double-checked locking: Clever, but broken</a>
     */
    private static final class InstanceHolder {

        private static volatile StatusLogger INSTANCE = new StatusLogger();
    }

    private final Config config;

    private final StatusConsoleListener fallbackListener;

    private final List<StatusListener> listeners;

    private final transient ReadWriteLock listenerLock = new ReentrantReadWriteLock();

    private final transient Lock listenerReadLock = listenerLock.readLock();

    private final transient Lock listenerWriteLock = listenerLock.writeLock();

    private final Queue<StatusData> buffer = new ConcurrentLinkedQueue<>();

    /**
     * Constructs the default instance.
     * <p>
     * This method is visible for tests.
     * </p>
     */
    StatusLogger() {
        this(
                StatusLogger.class.getSimpleName(),
                ParameterizedNoReferenceMessageFactory.INSTANCE,
                Config.getInstance(),
                new StatusConsoleListener(requireNonNull(Config.getInstance().fallbackListenerLevel)));
    }

    /**
     * Constructs an instance using given properties.
     * <b>Users should not create new instances, but use {@link #getLogger()} instead!</b>
     *
     * @param name the logger name
     * @param messageFactory the message factory
     * @param config the configuration
     * @param fallbackListener the fallback listener
     * @throws NullPointerException on null {@code name}, {@code messageFactory}, {@code config}, or {@code fallbackListener}
     * @since 2.23.0
     */
    public StatusLogger(
            final String name,
            final MessageFactory messageFactory,
            final Config config,
            final StatusConsoleListener fallbackListener) {
        super(requireNonNull(name, "name"), requireNonNull(messageFactory, "messageFactory"));
        this.config = requireNonNull(config, "config");
        this.fallbackListener = requireNonNull(fallbackListener, "fallbackListener");
        this.listeners = new ArrayList<>();
    }

    /**
     * Gets the static instance.
     *
     * @return the singleton instance
     */
    public static StatusLogger getLogger() {
        return InstanceHolder.INSTANCE;
    }

    /**
     * Sets the static (i.e., singleton) instance returned by {@link #getLogger()}.
     * This method is intended for testing purposes and can have unforeseen consequences if used in production code.
     *
     * @param logger a logger instance
     * @throws NullPointerException on null {@code logger}
     * @since 2.23.0
     */
    public static void setLogger(final StatusLogger logger) {
        InstanceHolder.INSTANCE = requireNonNull(logger, "logger");
    }

    /**
     * Returns the fallback listener.
     *
     * @return the fallback listener
     */
    public StatusConsoleListener getFallbackListener() {
        return fallbackListener;
    }

    /**
     * Sets the level of the fallback listener.
     *
     * @param level a level
     * @deprecated Instead use the {@link StatusConsoleListener#setLevel(Level) setLevel(Level)} method on the fallback listener returned by {@link #getFallbackListener()}.
     */
    @Deprecated
    public void setLevel(final Level level) {
        requireNonNull(level, "level");
        fallbackListener.setLevel(level);
=======
     * System property that can be configured with a date-time format string to use as the format for timestamps
     * in the status logger output. See {@link java.text.SimpleDateFormat} for supported formats.
     * @since 2.11.0
     */
    public static final String STATUS_DATE_FORMAT = "log4j2.StatusLogger.DateFormat";

    private static final long serialVersionUID = 2L;

    private static final String NOT_AVAIL = "?";

    static final PropertiesUtil PROPS = new PropertiesUtil("log4j2.StatusLogger.properties");

    private static final int MAX_ENTRIES = PROPS.getIntegerProperty(MAX_STATUS_ENTRIES, 200);

    private static final String DEFAULT_STATUS_LEVEL = PROPS.getStringProperty(DEFAULT_STATUS_LISTENER_LEVEL);

    static final boolean DEBUG_ENABLED =
            PropertiesUtil.getProperties().getBooleanProperty(Constants.LOG4J2_DEBUG, false, true);

    // LOG4J2-1176: normal parameterized message remembers param object, causing memory leaks.
    private static final StatusLogger STATUS_LOGGER = new StatusLogger(
            StatusLogger.class.getName(),
            ParameterizedNoReferenceMessageFactory.INSTANCE,
            SimpleLoggerFactory.getInstance());

    private final SimpleLogger logger;

    private final Collection<StatusListener> listeners = new CopyOnWriteArrayList<>();

    @SuppressWarnings("NonSerializableFieldInSerializableClass")
    // ReentrantReadWriteLock is Serializable
    private final ReadWriteLock listenersLock = new ReentrantReadWriteLock();

    private final Queue<StatusData> messages = new BoundedQueue<>(MAX_ENTRIES);

    @SuppressWarnings("NonSerializableFieldInSerializableClass")
    // ReentrantLock is Serializable
    private final Lock msgLock = new ReentrantLock();

    private int listenersLevel;

    /**
     * Constructs the singleton instance for the STATUS_LOGGER constant.
     * <p>
     * This is now the logger level is set:
     * </p>
     * <ol>
     * <li>If the property {@value Constants#LOG4J2_DEBUG} is {@code "true"}, then use {@link Level#TRACE}, otherwise,</li>
     * <li>Use {@link Level#ERROR}</li>
     * </ol>
     * <p>
     * This is now the listener level is set:
     * </p>
     * <ol>
     * <li>If the property {@value #DEFAULT_STATUS_LISTENER_LEVEL} is set, then use <em>it</em>, otherwise,</li>
     * <li>Use {@link Level#WARN}</li>
     * </ol>
     * <p>
     * See:
     * <ol>
     * <li>LOG4J2-1813 Provide shorter and more intuitive way to switch on Log4j internal debug logging. If system property
     * "log4j2.debug" is defined, print all status logging.</li>
     * <li>LOG4J2-3340 StatusLogger's log Level cannot be changed as advertised.</li>
     * </ol>
     * </p>
     *
     * @param name The logger name.
     * @param messageFactory The message factory.
     */
    private StatusLogger(
            final String name, final MessageFactory messageFactory, final SimpleLoggerFactory loggerFactory) {
        super(name, messageFactory);
        final Level loggerLevel = DEBUG_ENABLED ? Level.TRACE : Level.ERROR;
        this.logger = loggerFactory.createSimpleLogger("StatusLogger", loggerLevel, messageFactory, System.err);
        this.listenersLevel = Level.toLevel(DEFAULT_STATUS_LEVEL, Level.WARN).intLevel();
    }

    /**
     * Retrieve the StatusLogger.
     *
     * @return The StatusLogger.
     */
    public static StatusLogger getLogger() {
        return STATUS_LOGGER;
    }

    public void setLevel(final Level level) {
        logger.setLevel(level);
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
    }

    /**
     * Registers a new listener.
     *
<<<<<<< HEAD
     * @param listener a listener to register
     */
    public void registerListener(final StatusListener listener) {
        requireNonNull(listener, "listener");
        listenerWriteLock.lock();
        try {
            listeners.add(listener);
        } finally {
            listenerWriteLock.unlock();
=======
     * @param listener The StatusListener to register.
     */
    public void registerListener(final StatusListener listener) {
        listenersLock.writeLock().lock();
        try {
            listeners.add(listener);
            final Level lvl = listener.getStatusLevel();
            if (listenersLevel < lvl.intLevel()) {
                listenersLevel = lvl.intLevel();
            }
        } finally {
            listenersLock.writeLock().unlock();
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
        }
    }

    /**
<<<<<<< HEAD
     * Removes the given listener.
     *
     * @param listener a listener to remove
     */
    public void removeListener(final StatusListener listener) {
        requireNonNull(listener, "listener");
        listenerWriteLock.lock();
        try {
            listeners.remove(listener);
            closeListenerSafely(listener);
        } finally {
            listenerWriteLock.unlock();
=======
     * Removes a StatusListener.
     *
     * @param listener The StatusListener to remove.
     */
    public void removeListener(final StatusListener listener) {
        closeSilently(listener);
        listenersLock.writeLock().lock();
        try {
            listeners.remove(listener);
            int lowest = Level.toLevel(DEFAULT_STATUS_LEVEL, Level.WARN).intLevel();
            for (final StatusListener statusListener : listeners) {
                final int level = statusListener.getStatusLevel().intLevel();
                if (lowest < level) {
                    lowest = level;
                }
            }
            listenersLevel = lowest;
        } finally {
            listenersLock.writeLock().unlock();
        }
    }

    public void updateListenerLevel(final Level status) {
        if (status.intLevel() > listenersLevel) {
            listenersLevel = status.intLevel();
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
        }
    }

    /**
<<<<<<< HEAD
     * Sets the level of the fallback listener.
     *
     * @param level a level
     * @deprecated Instead use the {@link StatusConsoleListener#setLevel(Level) setLevel(Level)} method on the fallback listener returned by {@link #getFallbackListener()}.
     */
    @Deprecated
    public void updateListenerLevel(final Level level) {
        requireNonNull(level, "level");
        fallbackListener.setLevel(level);
    }

    /**
     * Returns the listener collection.
     *
     * @return a thread-safe read-only collection of listeners
     */
    public Iterable<StatusListener> getListeners() {
        listenerReadLock.lock();
        try {
            return Collections.unmodifiableCollection(listeners);
        } finally {
            listenerReadLock.unlock();
        }
    }

    /**
     * Clears the event buffer, removes the <em>registered</em> (not the fallback one!) listeners, and resets the fallback listener.
     */
    public void reset() {
        listenerWriteLock.lock();
        try {
            final Iterator<StatusListener> listenerIterator = listeners.iterator();
            while (listenerIterator.hasNext()) {
                final StatusListener listener = listenerIterator.next();
                closeListenerSafely(listener);
                listenerIterator.remove();
            }
        } finally {
            listenerWriteLock.unlock();
        }
        fallbackListener.close();
        buffer.clear();
    }

    private static void closeListenerSafely(final StatusListener listener) {
        try {
            listener.close();
        } catch (final IOException error) {
            final String message = String.format("failed closing listener: %s", listener);
            final RuntimeException extendedError = new RuntimeException(message, error);
            // There is no logging system at this stage.
            // There is nothing we can do but simply dumping the failure.
            extendedError.printStackTrace(System.err);
=======
     * Returns a thread safe Iterable for the StatusListener.
     *
     * @return An Iterable for the list of StatusListeners.
     */
    public Iterable<StatusListener> getListeners() {
        return listeners;
    }

    /**
     * Clears the list of status events and listeners.
     */
    public void reset() {
        listenersLock.writeLock().lock();
        try {
            for (final StatusListener listener : listeners) {
                closeSilently(listener);
            }
        } finally {
            listeners.clear();
            listenersLock.writeLock().unlock();
            // note this should certainly come after the unlock to avoid unnecessary nested locking
            clear();
        }
    }

    private static void closeSilently(final Closeable resource) {
        try {
            resource.close();
        } catch (final IOException ignored) {
            // ignored
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
        }
    }

    /**
<<<<<<< HEAD
     * Returns buffered events.
     *
     * @deprecated Instead of relying on the buffering provided by {@code StatusLogger}, users should register their own listeners to access to logged events.
     * @return a thread-safe read-only collection of buffered events
     */
    @Deprecated
    public List<StatusData> getStatusData() {
        // Wrapping the buffer clone with an unmodifiable list.
        // By disallowing modifications, we make it clear to the user that mutations will not get propagated.
        // `Collections.unmodifiableList(new ArrayList<>(...))` should be replaced with `List.of()` in Java 9+.
        return Collections.unmodifiableList(new ArrayList<>(buffer));
    }

    /**
     * Clears the event buffer.
     *
     * @deprecated Instead of relying on the buffering provided by {@code StatusLogger}, users should register their own listeners to access to logged events.
     */
    @Deprecated
    public void clear() {
        buffer.clear();
    }

    /**
     * Returns the least specific level among listeners, if registered any; otherwise, the fallback listener level.
     *
     * @return the least specific listener level, if registered any; otherwise, the fallback listener level
     */
    @Override
    public Level getLevel() {
        Level leastSpecificLevel = fallbackListener.getStatusLevel();
        // noinspection ForLoopReplaceableByForEach (avoid iterator instantiation)
        for (int listenerIndex = 0; listenerIndex < listeners.size(); listenerIndex++) {
            final StatusListener listener = listeners.get(listenerIndex);
            final Level listenerLevel = listener.getStatusLevel();
            if (listenerLevel.isLessSpecificThan(leastSpecificLevel)) {
                leastSpecificLevel = listenerLevel;
            }
        }
        return leastSpecificLevel;
    }

    @Override
    @SuppressFBWarnings("INFORMATION_EXPOSURE_THROUGH_AN_ERROR_MESSAGE")
    public void logMessage(
            final String fqcn,
            final Level level,
            final Marker marker,
            final Message message,
            final Throwable throwable) {
        try {
            final StatusData statusData = createStatusData(fqcn, level, message, throwable);
            buffer(statusData);
            notifyListeners(statusData);
        } catch (final Exception error) {
            // We are at the lowest level of the system.
            // Hence, there is nothing better we can do but dumping the failure.
            error.printStackTrace(System.err);
        }
    }

    private void buffer(final StatusData statusData) {
        if (config.bufferCapacity == 0) {
            return;
        }
        buffer.add(statusData);
        while (buffer.size() >= config.bufferCapacity) {
            buffer.remove();
        }
    }

    private void notifyListeners(final StatusData statusData) {
        final boolean foundListeners;
        listenerReadLock.lock();
        try {
            foundListeners = !listeners.isEmpty();
            listeners.forEach(listener -> notifyListener(listener, statusData));
        } finally {
            listenerReadLock.unlock();
        }
        if (!foundListeners) {
            notifyListener(fallbackListener, statusData);
        }
    }

    private void notifyListener(final StatusListener listener, final StatusData statusData) {
        final boolean levelEnabled = isLevelEnabled(listener.getStatusLevel(), statusData.getLevel());
        if (levelEnabled) {
            listener.log(statusData);
        }
    }

    private StatusData createStatusData(
            @Nullable final String fqcn,
            final Level level,
            final Message message,
            @Nullable final Throwable throwable) {
        final StackTraceElement caller = getStackTraceElement(fqcn);
        final Instant instant = Instant.now();
        return new StatusData(caller, level, message, throwable, null, config.instantFormatter, instant);
    }

    @Nullable
    private static StackTraceElement getStackTraceElement(@Nullable final String fqcn) {
=======
     * Returns a List of all events as StatusData objects.
     *
     * @return The list of StatusData objects.
     */
    public List<StatusData> getStatusData() {
        msgLock.lock();
        try {
            return new ArrayList<>(messages);
        } finally {
            msgLock.unlock();
        }
    }

    /**
     * Clears the list of status events.
     */
    public void clear() {
        msgLock.lock();
        try {
            messages.clear();
        } finally {
            msgLock.unlock();
        }
    }

    @Override
    public Level getLevel() {
        return logger.getLevel();
    }

    /**
     * Adds an event.
     *
     * @param marker The Marker
     * @param fqcn The fully qualified class name of the <b>caller</b>
     * @param level The logging level
     * @param msg The message associated with the event.
     * @param t A Throwable or null.
     */
    @Override
    public void logMessage(
            final String fqcn, final Level level, final Marker marker, final Message msg, final Throwable t) {
        StackTraceElement element = null;
        if (fqcn != null) {
            element = getStackTraceElement(fqcn, Thread.currentThread().getStackTrace());
        }
        final StatusData data = new StatusData(element, level, msg, t, null);
        msgLock.lock();
        try {
            messages.add(data);
        } finally {
            msgLock.unlock();
        }
        // LOG4J2-1813 if system property "log4j2.debug" is defined, all status logging is enabled
        if (DEBUG_ENABLED || (listeners.size() <= 0)) {
            logger.logMessage(fqcn, level, marker, msg, t);
        } else {
            for (final StatusListener listener : listeners) {
                if (data.getLevel().isMoreSpecificThan(listener.getStatusLevel())) {
                    listener.log(data);
                }
            }
        }
    }

    private StackTraceElement getStackTraceElement(final String fqcn, final StackTraceElement[] stackTrace) {
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
        if (fqcn == null) {
            return null;
        }
        boolean next = false;
<<<<<<< HEAD
        final StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
=======
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
        for (final StackTraceElement element : stackTrace) {
            final String className = element.getClassName();
            if (next && !fqcn.equals(className)) {
                return element;
            }
            if (fqcn.equals(className)) {
                next = true;
<<<<<<< HEAD
            } else if ("?".equals(className)) {
=======
            } else if (NOT_AVAIL.equals(className)) {
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
                break;
            }
        }
        return null;
    }

    @Override
<<<<<<< HEAD
    public boolean isEnabled(final Level level, final Marker marker, final String message, final Throwable throwable) {
=======
    public boolean isEnabled(final Level level, final Marker marker, final String message, final Throwable t) {
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
        return isEnabled(level, marker);
    }

    @Override
    public boolean isEnabled(final Level level, final Marker marker, final String message) {
        return isEnabled(level, marker);
    }

    @Override
    public boolean isEnabled(final Level level, final Marker marker, final String message, final Object... params) {
        return isEnabled(level, marker);
    }

    @Override
    public boolean isEnabled(final Level level, final Marker marker, final String message, final Object p0) {
        return isEnabled(level, marker);
    }

    @Override
    public boolean isEnabled(
            final Level level, final Marker marker, final String message, final Object p0, final Object p1) {
        return isEnabled(level, marker);
    }

    @Override
    public boolean isEnabled(
            final Level level,
            final Marker marker,
            final String message,
            final Object p0,
            final Object p1,
            final Object p2) {
        return isEnabled(level, marker);
    }

    @Override
    public boolean isEnabled(
            final Level level,
            final Marker marker,
            final String message,
            final Object p0,
            final Object p1,
            final Object p2,
            final Object p3) {
        return isEnabled(level, marker);
    }

    @Override
    public boolean isEnabled(
            final Level level,
            final Marker marker,
            final String message,
            final Object p0,
            final Object p1,
            final Object p2,
            final Object p3,
            final Object p4) {
        return isEnabled(level, marker);
    }

    @Override
    public boolean isEnabled(
            final Level level,
            final Marker marker,
            final String message,
            final Object p0,
            final Object p1,
            final Object p2,
            final Object p3,
            final Object p4,
            final Object p5) {
        return isEnabled(level, marker);
    }

    @Override
    public boolean isEnabled(
            final Level level,
            final Marker marker,
            final String message,
            final Object p0,
            final Object p1,
            final Object p2,
            final Object p3,
            final Object p4,
            final Object p5,
            final Object p6) {
        return isEnabled(level, marker);
    }

    @Override
    public boolean isEnabled(
            final Level level,
            final Marker marker,
            final String message,
            final Object p0,
            final Object p1,
            final Object p2,
            final Object p3,
            final Object p4,
            final Object p5,
            final Object p6,
            final Object p7) {
        return isEnabled(level, marker);
    }

    @Override
    public boolean isEnabled(
            final Level level,
            final Marker marker,
            final String message,
            final Object p0,
            final Object p1,
            final Object p2,
            final Object p3,
            final Object p4,
            final Object p5,
            final Object p6,
            final Object p7,
            final Object p8) {
        return isEnabled(level, marker);
    }

    @Override
    public boolean isEnabled(
            final Level level,
            final Marker marker,
            final String message,
            final Object p0,
            final Object p1,
            final Object p2,
            final Object p3,
            final Object p4,
            final Object p5,
            final Object p6,
            final Object p7,
            final Object p8,
            final Object p9) {
        return isEnabled(level, marker);
    }

    @Override
<<<<<<< HEAD
    public boolean isEnabled(
            final Level level, final Marker marker, final CharSequence message, final Throwable throwable) {
=======
    public boolean isEnabled(final Level level, final Marker marker, final CharSequence message, final Throwable t) {
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
        return isEnabled(level, marker);
    }

    @Override
<<<<<<< HEAD
    public boolean isEnabled(final Level level, final Marker marker, final Object message, final Throwable throwable) {
=======
    public boolean isEnabled(final Level level, final Marker marker, final Object message, final Throwable t) {
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
        return isEnabled(level, marker);
    }

    @Override
<<<<<<< HEAD
    public boolean isEnabled(final Level level, final Marker marker, final Message message, final Throwable throwable) {
=======
    public boolean isEnabled(final Level level, final Marker marker, final Message message, final Throwable t) {
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
        return isEnabled(level, marker);
    }

    @Override
<<<<<<< HEAD
    public boolean isEnabled(final Level messageLevel, final Marker marker) {
        requireNonNull(messageLevel, "messageLevel");
        final Level loggerLevel = getLevel();
        return isLevelEnabled(loggerLevel, messageLevel);
    }

    /**
     * Checks if the message level is allowed for the filtering level (e.g., of logger, of listener) by taking debug mode into account.
     *
     * @param filteringLevel the level (e.g., of logger, of listener) to filter messages
     * @param messageLevel the level of the message
     * @return {@code true}, if the sink level is less specific than the message level; {@code false}, otherwise
     */
    private boolean isLevelEnabled(final Level filteringLevel, final Level messageLevel) {
        return config.debugEnabled || filteringLevel.isLessSpecificThan(messageLevel);
=======
    public boolean isEnabled(final Level level, final Marker marker) {
        if (DEBUG_ENABLED) {
            return true;
        }
        if (listeners.size() > 0) {
            return listenersLevel >= level.intLevel();
        }
        return logger.isEnabled(level, marker);
    }

    /**
     * Queues for status events.
     *
     * @param <E> Object type to be stored in the queue.
     */
    private class BoundedQueue<E> extends ConcurrentLinkedQueue<E> {

        private static final long serialVersionUID = -3945953719763255337L;

        private final int size;

        BoundedQueue(final int size) {
            this.size = size;
        }

        @Override
        public boolean add(final E object) {
            super.add(object);
            while (messages.size() > size) {
                messages.poll();
            }
            return size > 0;
        }
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
    }
}
