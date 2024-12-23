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
package org.apache.logging.log4j.util;

import aQute.bnd.annotation.Cardinality;
import aQute.bnd.annotation.Resolution;
import aQute.bnd.annotation.spi.ServiceConsumer;
import java.io.IOException;
import java.io.InputStream;
<<<<<<< HEAD
=======
import java.lang.invoke.MethodHandles;
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
import java.nio.charset.Charset;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
<<<<<<< HEAD
import java.util.Arrays;
import java.util.Collection;
=======
import java.util.HashSet;
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
<<<<<<< HEAD
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.status.StatusLogger;
=======
import java.util.concurrent.ConcurrentSkipListSet;
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c

/**
 * <em>Consider this class private.</em>
 * <p>
 * Provides utility methods for managing {@link Properties} instances as well as access to the global configuration
 * system. Properties by default are loaded from the system properties, system environment, and a classpath resource
 * file named {@value #LOG4J_PROPERTIES_FILE_NAME}. Additional properties can be loaded by implementing a custom
 * {@link PropertySource} service and specifying it via a {@link ServiceLoader} file called
 * {@code META-INF/services/org.apache.logging.log4j.util.PropertySource} with a list of fully qualified class names
 * implementing that interface.
 * </p>
 *
 * @see PropertySource
 */
@ServiceConsumer(value = PropertySource.class, resolution = Resolution.OPTIONAL, cardinality = Cardinality.MULTIPLE)
public final class PropertiesUtil {

<<<<<<< HEAD
    private static final Logger LOGGER = StatusLogger.getLogger();

    private static final String LOG4J_PROPERTIES_FILE_NAME = "log4j2.component.properties";

    private static final String LOG4J_SYSTEM_PROPERTIES_FILE_NAME = "log4j2.system.properties";

    private static final Lazy<PropertiesUtil> COMPONENT_PROPERTIES =
            Lazy.lazy(() -> new PropertiesUtil(LOG4J_PROPERTIES_FILE_NAME, false));

    private static final Pattern DURATION_PATTERN = Pattern.compile("([+-]?\\d+)\\s*(\\w+)?", Pattern.CASE_INSENSITIVE);

=======
    private static final String LOG4J_PROPERTIES_FILE_NAME = "log4j2.component.properties";
    private static final String LOG4J_SYSTEM_PROPERTIES_FILE_NAME = "log4j2.system.properties";
    private static final Lazy<PropertiesUtil> COMPONENT_PROPERTIES =
            Lazy.lazy(() -> new PropertiesUtil(LOG4J_PROPERTIES_FILE_NAME, false));

>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
    private final Environment environment;

    /**
     * Constructs a PropertiesUtil using a given Properties object as its source of defined properties.
     *
     * @param props the Properties to use by default
     */
    public PropertiesUtil(final Properties props) {
        this(new PropertiesPropertySource(props));
    }

    /**
     * Constructs a PropertiesUtil for a given properties file name on the classpath. The properties specified in this
     * file are used by default. If a property is not defined in this file, then the equivalent system property is used.
     *
     * @param propertiesFileName the location of properties file to load
     */
    public PropertiesUtil(final String propertiesFileName) {
        this(propertiesFileName, true);
    }

    private PropertiesUtil(final String propertiesFileName, final boolean useTccl) {
        this(new PropertyFilePropertySource(propertiesFileName, useTccl));
    }

    /**
     * Constructs a PropertiesUtil for a give property source as source of additional properties.
     * @param source a property source
     */
    PropertiesUtil(final PropertySource source) {
<<<<<<< HEAD
        environment = new Environment(source);
=======
        this.environment = new Environment(source);
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
    }

    /**
     * Loads and closes the given property input stream. If an error occurs, log to the status logger.
     *
     * @param in     a property input stream.
     * @param source a source object describing the source, like a resource string or a URL.
     * @return a new Properties object
     */
    static Properties loadClose(final InputStream in, final Object source) {
        final Properties props = new Properties();
        if (null != in) {
            try {
                props.load(in);
<<<<<<< HEAD
            } catch (final IOException error) {
                LOGGER.error("Unable to read source `{}`", source, error);
            } finally {
                try {
                    in.close();
                } catch (final IOException error) {
                    LOGGER.error("Unable to close source `{}`", source, error);
=======
            } catch (final IOException e) {
                LowLevelLogUtil.logException("Unable to read " + source, e);
            } finally {
                try {
                    in.close();
                } catch (final IOException e) {
                    LowLevelLogUtil.logException("Unable to close " + source, e);
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
                }
            }
        }
        return props;
    }

    /**
     * Returns the PropertiesUtil used by Log4j.
     *
     * @return the main Log4j PropertiesUtil instance.
     */
    public static PropertiesUtil getProperties() {
        return COMPONENT_PROPERTIES.get();
    }

    /**
<<<<<<< HEAD
     * Allows a {@link PropertySource} to be added after {@code PropertiesUtil} has been created.
     * @param propertySource the {@code PropertySource} to add.
     * @since 2.19.0
     */
    public void addPropertySource(final PropertySource propertySource) {
        environment.addPropertySource(Objects.requireNonNull(propertySource));
    }

    /**
     * Removes a {@link PropertySource}.
     * @param propertySource the {@code PropertySource} to remove.
     * @since 2.24.0
     */
    public void removePropertySource(final PropertySource propertySource) {
        environment.removePropertySource(Objects.requireNonNull(propertySource));
=======
     * Allows a PropertySource to be added after PropertiesUtil has been created.
     * @param propertySource the PropertySource to add.
     */
    public void addPropertySource(final PropertySource propertySource) {
        if (environment != null) {
            environment.addPropertySource(propertySource);
        }
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
    }

    /**
     * Returns {@code true} if the specified property is defined, regardless of its value (it may not have a value).
     *
     * @param name the name of the property to verify
     * @return {@code true} if the specified property is defined, regardless of its value
     */
    public boolean hasProperty(final String name) {
        return environment.containsKey(name);
    }

    /**
     * Gets the named property as a boolean value. If the property matches the string {@code "true"} (case-insensitive),
     * then it is returned as the boolean value {@code true}. Any other non-{@code null} text in the property is
     * considered {@code false}.
     *
     * @param name the name of the property to look up
     * @return the boolean value of the property or {@code false} if undefined.
     */
    public boolean getBooleanProperty(final String name) {
        return getBooleanProperty(name, false);
    }

    /**
     * Gets the named property as a boolean value.
     *
     * @param name         the name of the property to look up
     * @param defaultValue the default value to use if the property is undefined
     * @return the boolean value of the property or {@code defaultValue} if undefined.
     */
    public boolean getBooleanProperty(final String name, final boolean defaultValue) {
        final String prop = getStringProperty(name);
        return prop == null ? defaultValue : "true".equalsIgnoreCase(prop);
    }

    /**
     * Gets the named property as a boolean value.
     *
     * @param name                  the name of the property to look up
     * @param defaultValueIfAbsent  the default value to use if the property is undefined
     * @param defaultValueIfPresent the default value to use if the property is defined but not assigned
     * @return the boolean value of the property or {@code defaultValue} if undefined.
     */
    public boolean getBooleanProperty(
            final String name, final boolean defaultValueIfAbsent, final boolean defaultValueIfPresent) {
        final String prop = getStringProperty(name);
        return prop == null
                ? defaultValueIfAbsent
                : prop.isEmpty() ? defaultValueIfPresent : "true".equalsIgnoreCase(prop);
    }

    /**
     * Retrieves a property that may be prefixed by more than one string.
     * @param prefixes The array of prefixes.
     * @param key The key to locate.
     * @param supplier The method to call to derive the default value. If the value is null, null will be returned
     * if no property is found.
     * @return The value or null if it is not found.
     * @since 2.13.0
     */
<<<<<<< HEAD
    public Boolean getBooleanProperty(final String[] prefixes, final String key, final Supplier<Boolean> supplier) {
        for (final String prefix : prefixes) {
=======
    @SuppressWarnings("deprecation")
    public Boolean getBooleanProperty(final String[] prefixes, final String key, final Supplier<Boolean> supplier) {
        for (String prefix : prefixes) {
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
            if (hasProperty(prefix + key)) {
                return getBooleanProperty(prefix + key);
            }
        }
        return supplier != null ? supplier.get() : null;
    }

    /**
     * Gets the named property as a Charset value.
     *
     * @param name the name of the property to look up
     * @return the Charset value of the property or {@link Charset#defaultCharset()} if undefined.
     */
    public Charset getCharsetProperty(final String name) {
        return getCharsetProperty(name, Charset.defaultCharset());
    }

    /**
     * Gets the named property as a Charset value. If we cannot find the named Charset, see if it is mapped in
     * file {@code Log4j-charsets.properties} on the class path.
     *
     * @param name         the name of the property to look up
     * @param defaultValue the default value to use if the property is undefined
     * @return the Charset value of the property or {@code defaultValue} if undefined.
     */
    public Charset getCharsetProperty(final String name, final Charset defaultValue) {
        final String charsetName = getStringProperty(name);
        if (charsetName == null) {
            return defaultValue;
        }
        if (Charset.isSupported(charsetName)) {
            return Charset.forName(charsetName);
        }
        final ResourceBundle bundle = getCharsetsResourceBundle();
        if (bundle.containsKey(name)) {
            final String mapped = bundle.getString(name);
            if (Charset.isSupported(mapped)) {
                return Charset.forName(mapped);
            }
        }
<<<<<<< HEAD
        LOGGER.warn(
                "Unable to read charset `{}` from property `{}`. Falling back to the default: `{}`",
                charsetName,
                name,
                defaultValue);
=======
        LowLevelLogUtil.log("Unable to get Charset '" + charsetName + "' for property '" + name + "', using default "
                + defaultValue + " and continuing.");
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
        return defaultValue;
    }

    /**
     * Gets the named property as a double.
     *
     * @param name         the name of the property to look up
     * @param defaultValue the default value to use if the property is undefined
     * @return the parsed double value of the property or {@code defaultValue} if it was undefined or could not be parsed.
     */
    public double getDoubleProperty(final String name, final double defaultValue) {
        final String prop = getStringProperty(name);
        if (prop != null) {
            try {
                return Double.parseDouble(prop);
<<<<<<< HEAD
            } catch (final NumberFormatException e) {
                LOGGER.warn(
                        "Unable to read double `{}` from property `{}`. Falling back to the default: `{}`",
                        prop,
                        name,
                        defaultValue,
                        e);
=======
            } catch (final Exception ignored) {
                // returns default value
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
            }
        }
        return defaultValue;
    }

    /**
     * Gets the named property as an integer.
     *
     * @param name         the name of the property to look up
     * @param defaultValue the default value to use if the property is undefined
     * @return the parsed integer value of the property or {@code defaultValue} if it was undefined or could not be
     * parsed.
     */
    public int getIntegerProperty(final String name, final int defaultValue) {
        final String prop = getStringProperty(name);
        if (prop != null) {
            try {
                return Integer.parseInt(prop.trim());
<<<<<<< HEAD
            } catch (final NumberFormatException e) {
                LOGGER.warn(
                        "Unable to read int `{}` from property `{}`. Falling back to the default: `{}`",
                        prop,
                        name,
                        defaultValue,
                        e);
=======
            } catch (final Exception ignored) {
                // ignore
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
            }
        }
        return defaultValue;
    }

    /**
     * Retrieves a property that may be prefixed by more than one string.
     * @param prefixes The array of prefixes.
     * @param key The key to locate.
     * @param supplier The method to call to derive the default value. If the value is null, null will be returned
     * if no property is found.
     * @return The value or null if it is not found.
     * @since 2.13.0
     */
<<<<<<< HEAD
    public Integer getIntegerProperty(final String[] prefixes, final String key, final Supplier<Integer> supplier) {
        for (final String prefix : prefixes) {
=======
    @SuppressWarnings("deprecation")
    public Integer getIntegerProperty(final String[] prefixes, final String key, final Supplier<Integer> supplier) {
        for (String prefix : prefixes) {
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
            if (hasProperty(prefix + key)) {
                return getIntegerProperty(prefix + key, 0);
            }
        }
        return supplier != null ? supplier.get() : null;
    }

    /**
     * Gets the named property as a long.
     *
     * @param name         the name of the property to look up
     * @param defaultValue the default value to use if the property is undefined
     * @return the parsed long value of the property or {@code defaultValue} if it was undefined or could not be parsed.
     */
    public long getLongProperty(final String name, final long defaultValue) {
        final String prop = getStringProperty(name);
        if (prop != null) {
            try {
                return Long.parseLong(prop);
<<<<<<< HEAD
            } catch (final NumberFormatException e) {
                LOGGER.warn(
                        "Unable to read long `{}` from property `{}`. Falling back to the default: `{}`",
                        prop,
                        name,
                        defaultValue,
                        e);
=======
            } catch (final Exception ignored) {
                // returns the default value
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
            }
        }
        return defaultValue;
    }

    /**
     * Retrieves a property that may be prefixed by more than one string.
     * @param prefixes The array of prefixes.
     * @param key The key to locate.
     * @param supplier The method to call to derive the default value. If the value is null, null will be returned
     * if no property is found.
     * @return The value or null if it is not found.
     * @since 2.13.0
     */
<<<<<<< HEAD
    public Long getLongProperty(final String[] prefixes, final String key, final Supplier<Long> supplier) {
        for (final String prefix : prefixes) {
=======
    @SuppressWarnings("deprecation")
    public Long getLongProperty(final String[] prefixes, final String key, final Supplier<Long> supplier) {
        for (String prefix : prefixes) {
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
            if (hasProperty(prefix + key)) {
                return getLongProperty(prefix + key, 0);
            }
        }
        return supplier != null ? supplier.get() : null;
    }

    /**
     * Retrieves a Duration where the String is of the format nnn[unit] where nnn represents an integer value
     * and unit represents a time unit.
     * @param name The property name.
     * @param defaultValue The default value.
     * @return The value of the String as a Duration or the default value, which may be null.
     * @since 2.13.0
     */
    public Duration getDurationProperty(final String name, final Duration defaultValue) {
        final String prop = getStringProperty(name);
<<<<<<< HEAD
        try {
            return parseDuration(prop);
        } catch (final IllegalArgumentException e) {
            LOGGER.warn(
                    "Unable to read duration `{}` from property `{}`.\nExpected format 'n unit', where 'n' is an "
                            + "integer and 'unit' is one of: {}.",
                    prop,
                    name,
                    TimeUnit.getValidUnits().collect(Collectors.joining(", ")),
                    e);
=======
        if (prop != null) {
            return TimeUnit.getDuration(prop);
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
        }
        return defaultValue;
    }

    /**
     * Retrieves a property that may be prefixed by more than one string.
     * @param prefixes The array of prefixes.
     * @param key The key to locate.
     * @param supplier The method to call to derive the default value. If the value is null, null will be returned
     * if no property is found.
     * @return The value or null if it is not found.
     * @since 2.13.0
     */
<<<<<<< HEAD
    public Duration getDurationProperty(final String[] prefixes, final String key, final Supplier<Duration> supplier) {
        for (final String prefix : prefixes) {
=======
    @SuppressWarnings("deprecation")
    public Duration getDurationProperty(final String[] prefixes, final String key, final Supplier<Duration> supplier) {
        for (String prefix : prefixes) {
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
            if (hasProperty(prefix + key)) {
                return getDurationProperty(prefix + key, null);
            }
        }
        return supplier != null ? supplier.get() : null;
    }

    /**
     * Retrieves a property that may be prefixed by more than one string.
     * @param prefixes The array of prefixes.
     * @param key The key to locate.
     * @param supplier The method to call to derive the default value. If the value is null, null will be returned
     * if no property is found.
     * @return The value or null if it is not found.
     * @since 2.13.0
     */
<<<<<<< HEAD
    public String getStringProperty(final String[] prefixes, final String key, final Supplier<String> supplier) {
        for (final String prefix : prefixes) {
=======
    @SuppressWarnings("deprecation")
    public String getStringProperty(final String[] prefixes, final String key, final Supplier<String> supplier) {
        for (String prefix : prefixes) {
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
            final String result = getStringProperty(prefix + key);
            if (result != null) {
                return result;
            }
        }
        return supplier != null ? supplier.get() : null;
    }

    /**
     * Gets the named property as a String.
     *
     * @param name the name of the property to look up
     * @return the String value of the property or {@code null} if undefined.
     */
    public String getStringProperty(final String name) {
        return environment.get(name);
    }

    /**
     * Gets the named property as a String.
     *
     * @param name         the name of the property to look up
     * @param defaultValue the default value to use if the property is undefined
     * @return the String value of the property or {@code defaultValue} if undefined.
     */
    public String getStringProperty(final String name, final String defaultValue) {
        final String prop = getStringProperty(name);
        return prop == null ? defaultValue : prop;
    }

    /**
     * Return the system properties or an empty Properties object if an error occurs.
     *
     * @return The system properties.
     */
    public static Properties getSystemProperties() {
        try {
            return new Properties(System.getProperties());
<<<<<<< HEAD
        } catch (final SecurityException error) {
            LOGGER.error("Unable to access system properties.", error);
=======
        } catch (final SecurityException ex) {
            LowLevelLogUtil.logException("Unable to access system properties.", ex);
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
            // Sandboxed - can't read System Properties
            return new Properties();
        }
    }

    /**
     * Reloads all properties. This is primarily useful for unit tests.
     *
     * @since 2.10.0
<<<<<<< HEAD
     * @deprecated since 2.24.0 caching of property values is disabled.
     */
    @Deprecated
    public void reload() {}
=======
     */
    public void reload() {
        environment.reload();
    }
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c

    /**
     * Provides support for looking up global configuration properties via environment variables, property files,
     * and system properties, in three variations:
     * <p>
     * Normalized: all log4j-related prefixes removed, remaining property is camelCased with a log4j2 prefix for
     * property files and system properties, or follows a LOG4J_FOO_BAR format for environment variables.
     * <p>
     * Legacy: the original property name as defined in the source pre-2.10.0.
     * <p>
     * Tokenized: loose matching based on word boundaries.
     *
     * @since 2.10.0
     */
    private static final class Environment {

<<<<<<< HEAD
        private final Set<PropertySource> sources = ConcurrentHashMap.newKeySet();
        private final ThreadLocal<PropertySource> CURRENT_PROPERTY_SOURCE = new ThreadLocal<>();

        private Environment(final PropertySource propertySource) {
            final PropertySource sysProps = new PropertyFilePropertySource(LOG4J_SYSTEM_PROPERTIES_FILE_NAME, false);
=======
        private final Set<PropertySource> sources = new ConcurrentSkipListSet<>(new PropertySource.Comparator());
        /**
         * Maps a key to its value or the value of its normalization in the lowest priority source that contains it.
         */
        private final Map<String, String> literal = new ConcurrentHashMap<>();

        private final Map<List<CharSequence>, String> tokenized = new ConcurrentHashMap<>();

        private Environment(final PropertySource propertySource) {
            final PropertyFilePropertySource sysProps =
                    new PropertyFilePropertySource(LOG4J_SYSTEM_PROPERTIES_FILE_NAME, false);
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
            try {
                sysProps.forEach((key, value) -> {
                    if (System.getProperty(key) == null) {
                        System.setProperty(key, value);
                    }
                });
<<<<<<< HEAD
            } catch (final SecurityException e) {
                LOGGER.warn(
                        "Unable to set Java system properties from {} file, due to security restrictions.",
                        LOG4J_SYSTEM_PROPERTIES_FILE_NAME,
                        e);
            }
            sources.add(propertySource);
            // We don't log anything on the status logger.
            ServiceLoaderUtil.safeStream(
                            PropertySource.class,
                            ServiceLoader.load(PropertySource.class, PropertiesUtil.class.getClassLoader()),
                            LOGGER)
                    .forEach(sources::add);
        }

        private void addPropertySource(final PropertySource propertySource) {
            sources.add(propertySource);
        }

        private void removePropertySource(final PropertySource propertySource) {
            sources.remove(propertySource);
        }

        private String get(final String key) {
            final List<CharSequence> tokens = PropertySource.Util.tokenize(key);
            return sources.stream()
                    .sorted(PropertySource.Comparator.INSTANCE)
                    .map(source -> {
                        if (!tokens.isEmpty()) {
                            final String normalKey = Objects.toString(source.getNormalForm(tokens), null);
                            if (normalKey != null && sourceContainsProperty(source, normalKey)) {
                                return sourceGetProperty(source, normalKey);
                            }
                        }
                        return sourceGetProperty(source, key);
                    })
                    .filter(Objects::nonNull)
                    .findFirst()
                    .orElse(null);
        }

        private boolean sourceContainsProperty(final PropertySource source, final String key) {
            PropertySource recursiveSource = CURRENT_PROPERTY_SOURCE.get();
            if (recursiveSource == null) {
                CURRENT_PROPERTY_SOURCE.set(source);
                try {
                    return source.containsProperty(key);
                } catch (final Exception e) {
                    LOGGER.warn("Failed to retrieve Log4j property {} from property source {}.", key, source, e);
                } finally {
                    CURRENT_PROPERTY_SOURCE.remove();
                }
            }
            LOGGER.warn("Recursive call to `containsProperty()` from property source {}.", recursiveSource);
            return false;
        }

        private String sourceGetProperty(final PropertySource source, final String key) {
            PropertySource recursiveSource = CURRENT_PROPERTY_SOURCE.get();
            if (recursiveSource == null) {
                CURRENT_PROPERTY_SOURCE.set(source);
                try {
                    return source.getProperty(key);
                } catch (final Exception e) {
                    LOGGER.warn("Failed to retrieve Log4j property {} from property source {}.", key, source, e);
                } finally {
                    CURRENT_PROPERTY_SOURCE.remove();
                }
            }
            LOGGER.warn("Recursive call to `getProperty()` from property source {}.", recursiveSource);
            return null;
=======
            } catch (SecurityException ex) {
                // Access to System Properties is restricted so just skip it.
            }
            sources.add(propertySource);
            // We don't log anything on the status logger.
            ServiceLoaderUtil.loadServices(PropertySource.class, MethodHandles.lookup(), false, false)
                    .forEach(sources::add);

            reload();
        }

        /**
         * Allow a PropertySource to be added.
         * @param propertySource The PropertySource to add.
         */
        public void addPropertySource(final PropertySource propertySource) {
            sources.add(propertySource);
        }

        private synchronized void reload() {
            literal.clear();
            tokenized.clear();
            // 1. Collects all property keys from enumerable sources.
            final Set<String> keys = new HashSet<>();
            sources.stream().map(PropertySource::getPropertyNames).forEach(keys::addAll);
            // 2. Fills the property caches. Sources with higher priority values don't override the previous ones.
            keys.stream().filter(Objects::nonNull).forEach(key -> {
                final List<CharSequence> tokens = PropertySource.Util.tokenize(key);
                final boolean hasTokens = !tokens.isEmpty();
                sources.forEach(source -> {
                    if (source.containsProperty(key)) {
                        final String value = source.getProperty(key);
                        if (hasTokens) {
                            tokenized.putIfAbsent(tokens, value);
                        }
                    }
                    if (hasTokens) {
                        final String normalKey = Objects.toString(source.getNormalForm(tokens), null);
                        if (normalKey != null && source.containsProperty(normalKey)) {
                            literal.putIfAbsent(key, source.getProperty(normalKey));
                        } else if (source.containsProperty(key)) {
                            literal.putIfAbsent(key, source.getProperty(key));
                        }
                    }
                });
            });
        }

        private String get(final String key) {
            if (literal.containsKey(key)) {
                return literal.get(key);
            }
            final List<CharSequence> tokens = PropertySource.Util.tokenize(key);
            final boolean hasTokens = !tokens.isEmpty();
            for (final PropertySource source : sources) {
                if (hasTokens) {
                    final String normalKey = Objects.toString(source.getNormalForm(tokens), null);
                    if (normalKey != null && source.containsProperty(normalKey)) {
                        return source.getProperty(normalKey);
                    }
                }
                if (source.containsProperty(key)) {
                    return source.getProperty(key);
                }
            }
            return tokenized.get(tokens);
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
        }

        private boolean containsKey(final String key) {
            final List<CharSequence> tokens = PropertySource.Util.tokenize(key);
<<<<<<< HEAD
            return sources.stream().anyMatch(s -> {
                final CharSequence normalizedKey = tokens.isEmpty() ? null : s.getNormalForm(tokens);
                return sourceContainsProperty(s, key)
                        || (normalizedKey != null && sourceContainsProperty(s, normalizedKey.toString()));
            });
=======
            return literal.containsKey(key)
                    || tokenized.containsKey(tokens)
                    || sources.stream().anyMatch(s -> {
                        final CharSequence normalizedKey = s.getNormalForm(tokens);
                        return s.containsProperty(key)
                                || (normalizedKey != null && s.containsProperty(normalizedKey.toString()));
                    });
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
        }
    }

    /**
     * Extracts properties that start with or are equals to the specific prefix and returns them in a new Properties
     * object with the prefix removed.
     *
     * @param properties The Properties to evaluate.
     * @param prefix     The prefix to extract.
     * @return The subset of properties.
     */
    public static Properties extractSubset(final Properties properties, final String prefix) {
        final Properties subset = new Properties();

<<<<<<< HEAD
        if (prefix == null || prefix.isEmpty()) {
=======
        if (prefix == null || prefix.length() == 0) {
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
            return subset;
        }

        final String prefixToMatch = prefix.charAt(prefix.length() - 1) != '.' ? prefix + '.' : prefix;

<<<<<<< HEAD
        final Collection<String> keys = new ArrayList<>();
=======
        final List<String> keys = new ArrayList<>();
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c

        for (final String key : properties.stringPropertyNames()) {
            if (key.startsWith(prefixToMatch)) {
                subset.setProperty(key.substring(prefixToMatch.length()), properties.getProperty(key));
                keys.add(key);
            }
        }
        for (final String key : keys) {
            properties.remove(key);
        }

        return subset;
    }

    static ResourceBundle getCharsetsResourceBundle() {
        return ResourceBundle.getBundle("Log4j-charsets");
    }

    /**
     * Partitions a properties map based on common key prefixes up to the first period.
     *
     * @param properties properties to partition
     * @return the partitioned properties where each key is the common prefix (minus the period) and the values are
     * new property maps without the prefix and period in the key
     * @since 2.6
     */
    public static Map<String, Properties> partitionOnCommonPrefixes(final Properties properties) {
        return partitionOnCommonPrefixes(properties, false);
    }

    /**
     * Partitions a properties map based on common key prefixes up to the first period.
     *
     * @param properties properties to partition
     * @param includeBaseKey when true if a key exists with no '.' the key will be included.
     * @return the partitioned properties where each key is the common prefix (minus the period) and the values are
     * new property maps without the prefix and period in the key
     * @since 2.17.2
     */
    public static Map<String, Properties> partitionOnCommonPrefixes(
            final Properties properties, final boolean includeBaseKey) {
        final Map<String, Properties> parts = new ConcurrentHashMap<>();
        for (final String key : properties.stringPropertyNames()) {
            final int idx = key.indexOf('.');
            if (idx < 0) {
                if (includeBaseKey) {
                    if (!parts.containsKey(key)) {
                        parts.put(key, new Properties());
                    }
                    parts.get(key).setProperty("", properties.getProperty(key));
                }
                continue;
            }
            final String prefix = key.substring(0, idx);
            if (!parts.containsKey(prefix)) {
                parts.put(prefix, new Properties());
            }
            parts.get(prefix).setProperty(key.substring(idx + 1), properties.getProperty(key));
        }
        return parts;
    }

    /**
     * Returns true if system properties tell us we are running on Windows.
     *
     * @return true if system properties tell us we are running on Windows.
     */
    public boolean isOsWindows() {
<<<<<<< HEAD
        return SystemPropertiesPropertySource.getSystemProperty("os.name", "").startsWith("Windows");
    }

    static Duration parseDuration(final CharSequence value) {
        final Matcher matcher = DURATION_PATTERN.matcher(value);
        if (matcher.matches()) {
            return Duration.of(parseDurationAmount(matcher.group(1)), TimeUnit.parseUnit(matcher.group(2)));
        }
        throw new IllegalArgumentException("Invalid duration value '" + value + "'.");
    }

    private static long parseDurationAmount(final String amount) {
        try {
            return Long.parseLong(amount);
        } catch (final NumberFormatException e) {
            throw new IllegalArgumentException("Invalid duration amount '" + amount + "'", e);
        }
    }

    private enum TimeUnit {
        NANOS(new String[] {"ns", "nano", "nanos", "nanosecond", "nanoseconds"}, ChronoUnit.NANOS),
        MICROS(new String[] {"us", "micro", "micros", "microsecond", "microseconds"}, ChronoUnit.MICROS),
        MILLIS(new String[] {"ms", "milli", "millis", "millisecond", "milliseconds"}, ChronoUnit.MILLIS),
        SECONDS(new String[] {"s", "second", "seconds"}, ChronoUnit.SECONDS),
        MINUTES(new String[] {"m", "minute", "minutes"}, ChronoUnit.MINUTES),
        HOURS(new String[] {"h", "hour", "hours"}, ChronoUnit.HOURS),
        DAYS(new String[] {"d", "day", "days"}, ChronoUnit.DAYS);

        private final String[] descriptions;
        private final TemporalUnit timeUnit;

        TimeUnit(final String[] descriptions, final TemporalUnit timeUnit) {
            this.descriptions = descriptions;
            this.timeUnit = timeUnit;
        }

        private static Stream<String> getValidUnits() {
            return Arrays.stream(values()).flatMap(unit -> Arrays.stream(unit.descriptions));
        }

        private static TemporalUnit parseUnit(final String unit) {
            if (unit != null) {
                for (final TimeUnit value : values()) {
                    for (final String description : value.descriptions) {
                        if (unit.equals(description)) {
                            return value.timeUnit;
                        }
                    }
                }
                throw new IllegalArgumentException("Invalid duration unit '" + unit + "'");
            }
            return ChronoUnit.MILLIS;
=======
        return getStringProperty("os.name", "").startsWith("Windows");
    }

    private enum TimeUnit {
        NANOS("ns,nano,nanos,nanosecond,nanoseconds", ChronoUnit.NANOS),
        MICROS("us,micro,micros,microsecond,microseconds", ChronoUnit.MICROS),
        MILLIS("ms,milli,millis,millsecond,milliseconds", ChronoUnit.MILLIS),
        SECONDS("s,second,seconds", ChronoUnit.SECONDS),
        MINUTES("m,minute,minutes", ChronoUnit.MINUTES),
        HOURS("h,hour,hours", ChronoUnit.HOURS),
        DAYS("d,day,days", ChronoUnit.DAYS);

        /*
         * https://errorprone.info/bugpattern/ImmutableEnumChecker
         * This field is effectively immutable.
         */
        @SuppressWarnings("ImmutableEnumChecker")
        private final String[] descriptions;

        private final ChronoUnit timeUnit;

        TimeUnit(final String descriptions, final ChronoUnit timeUnit) {
            this.descriptions = descriptions.split(",");
            this.timeUnit = timeUnit;
        }

        static Duration getDuration(final String time) {
            final String value = time.trim();
            TemporalUnit temporalUnit = ChronoUnit.MILLIS;
            long timeVal = 0;
            for (TimeUnit timeUnit : values()) {
                for (String suffix : timeUnit.descriptions) {
                    if (value.endsWith(suffix)) {
                        temporalUnit = timeUnit.timeUnit;
                        timeVal = Long.parseLong(value.substring(0, value.length() - suffix.length()));
                    }
                }
            }
            return Duration.of(timeVal, temporalUnit);
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
        }
    }
}