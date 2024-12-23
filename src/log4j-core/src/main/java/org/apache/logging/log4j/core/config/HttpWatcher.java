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
package org.apache.logging.log4j.core.config;

<<<<<<< HEAD
import static java.util.Objects.requireNonNull;
import static org.apache.logging.log4j.core.util.internal.HttpInputStreamUtil.readStream;
import static org.apache.logging.log4j.util.Strings.toRootUpperCase;

=======
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
<<<<<<< HEAD
import java.time.Instant;
=======
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAliases;
<<<<<<< HEAD
=======
import org.apache.logging.log4j.core.net.ssl.SslConfiguration;
import org.apache.logging.log4j.core.net.ssl.SslConfigurationFactory;
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
import org.apache.logging.log4j.core.util.AbstractWatcher;
import org.apache.logging.log4j.core.util.AuthorizationProvider;
import org.apache.logging.log4j.core.util.Source;
import org.apache.logging.log4j.core.util.Watcher;
import org.apache.logging.log4j.core.util.internal.HttpInputStreamUtil;
import org.apache.logging.log4j.core.util.internal.LastModifiedSource;
import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.util.PropertiesUtil;

/**
 *
 */
@Plugin(name = "http", category = Watcher.CATEGORY, elementType = Watcher.ELEMENT_TYPE, printObject = true)
@PluginAliases("https")
public class HttpWatcher extends AbstractWatcher {

    private final Logger LOGGER = StatusLogger.getLogger();

<<<<<<< HEAD
=======
    private final SslConfiguration sslConfiguration;
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
    private AuthorizationProvider authorizationProvider;
    private URL url;
    private volatile long lastModifiedMillis;
    private static final String HTTP = "http";
    private static final String HTTPS = "https";

    public HttpWatcher(
            final Configuration configuration,
            final Reconfigurable reconfigurable,
            final List<ConfigurationListener> configurationListeners,
            final long lastModifiedMillis) {
        super(configuration, reconfigurable, configurationListeners);
<<<<<<< HEAD
=======
        sslConfiguration = SslConfigurationFactory.getSslConfiguration();
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
        this.lastModifiedMillis = lastModifiedMillis;
    }

    @Override
    public long getLastModified() {
        return lastModifiedMillis;
    }

    @Override
    public boolean isModified() {
        return refreshConfiguration();
    }

    @Override
    public void watching(final Source source) {
        if (!source.getURI().getScheme().equals(HTTP)
                && !source.getURI().getScheme().equals(HTTPS)) {
            throw new IllegalArgumentException("HttpWatcher requires a url using the HTTP or HTTPS protocol, not "
                    + source.getURI().getScheme());
        }
        try {
            url = source.getURI().toURL();
            authorizationProvider = ConfigurationFactory.authorizationProvider(PropertiesUtil.getProperties());
        } catch (final MalformedURLException ex) {
            throw new IllegalArgumentException("Invalid URL for HttpWatcher " + source.getURI(), ex);
        }
        super.watching(source);
    }

    @Override
    public Watcher newWatcher(
            final Reconfigurable reconfigurable,
            final List<ConfigurationListener> listeners,
            final long lastModifiedMillis) {
        final HttpWatcher watcher = new HttpWatcher(getConfiguration(), reconfigurable, listeners, lastModifiedMillis);
        if (getSource() != null) {
            watcher.watching(getSource());
        }
        return watcher;
    }

    private boolean refreshConfiguration() {
        try {
            final LastModifiedSource source = new LastModifiedSource(url.toURI(), lastModifiedMillis);
            final HttpInputStreamUtil.Result result = HttpInputStreamUtil.getInputStream(source, authorizationProvider);
<<<<<<< HEAD
            // Update lastModifiedMillis
            // https://github.com/apache/logging-log4j2/issues/2937
            lastModifiedMillis = source.getLastModified();
            // The result of the HTTP/HTTPS request is already logged at `DEBUG` by `HttpInputStreamUtil`
            // We only log the important events at `INFO` or more.
            switch (result.getStatus()) {
                case NOT_MODIFIED: {
=======
            switch (result.getStatus()) {
                case NOT_MODIFIED: {
                    LOGGER.debug("Configuration Not Modified");
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
                    return false;
                }
                case SUCCESS: {
                    final ConfigurationSource configSource = getConfiguration().getConfigurationSource();
                    try {
<<<<<<< HEAD
                        // In this case `result.getInputStream()` is not null.
                        configSource.setData(readStream(requireNonNull(result.getInputStream())));
                        configSource.setModifiedMillis(source.getLastModified());
                        LOGGER.info(
                                "{} resource at {} was modified on {}",
                                () -> toRootUpperCase(url.getProtocol()),
                                () -> url.toExternalForm(),
                                () -> Instant.ofEpochMilli(source.getLastModified()));
                        return true;
                    } catch (final IOException e) {
                        // Dead code since result.getInputStream() is a ByteArrayInputStream
                        LOGGER.error("Error accessing configuration at {}", url.toExternalForm(), e);
=======
                        configSource.setData(HttpInputStreamUtil.readStream(result.getInputStream()));
                        configSource.setModifiedMillis(source.getLastModified());
                        LOGGER.debug("Content was modified for {}", url.toString());
                        return true;
                    } catch (final IOException e) {
                        LOGGER.error("Error accessing configuration at {}: {}", url, e.getMessage());
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
                        return false;
                    }
                }
                case NOT_FOUND: {
<<<<<<< HEAD
                    LOGGER.warn(
                            "{} resource at {} was not found",
                            () -> toRootUpperCase(url.getProtocol()),
                            () -> url.toExternalForm());
                    return false;
                }
                default: {
                    LOGGER.warn(
                            "Unexpected error retrieving {} resource at {}",
                            () -> toRootUpperCase(url.getProtocol()),
                            () -> url.toExternalForm());
=======
                    LOGGER.info("Unable to locate configuration at {}", url.toString());
                    return false;
                }
                default: {
                    LOGGER.warn("Unexpected error accessing configuration at {}", url.toString());
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
                    return false;
                }
            }
        } catch (final URISyntaxException ex) {
<<<<<<< HEAD
            LOGGER.error("Bad configuration file URL {}", url.toExternalForm(), ex);
=======
            LOGGER.error("Bad configuration URL: {}, {}", url.toString(), ex.getMessage());
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
            return false;
        }
    }
}
