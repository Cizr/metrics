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
package org.apache.logging.log4j.core.util.internal;

<<<<<<< HEAD
import static org.apache.logging.log4j.util.Strings.toRootUpperCase;

=======
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
<<<<<<< HEAD
import java.time.Instant;
=======
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.ConfigurationException;
import org.apache.logging.log4j.core.net.UrlConnectionFactory;
import org.apache.logging.log4j.core.net.ssl.SslConfigurationFactory;
import org.apache.logging.log4j.core.util.AuthorizationProvider;
<<<<<<< HEAD
import org.apache.logging.log4j.core.util.Source;
import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.util.Supplier;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
=======
import org.apache.logging.log4j.status.StatusLogger;
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c

/**
 * Utility method for reading data from an HTTP InputStream.
 */
public final class HttpInputStreamUtil {

    private static final Logger LOGGER = StatusLogger.getLogger();
    private static final int NOT_MODIFIED = 304;
    private static final int NOT_AUTHORIZED = 401;
<<<<<<< HEAD
    private static final int FORBIDDEN = 403;
=======
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
    private static final int NOT_FOUND = 404;
    private static final int OK = 200;
    private static final int BUF_SIZE = 1024;

<<<<<<< HEAD
    /**
     * Retrieves an HTTP resource if it has been modified.
     * <p>
     *     Side effects: if the request is successful, the last modified time of the {@code source}
     *     parameter is modified.
     * </p>
     * @param source The location of the HTTP resource
     * @param authorizationProvider The authentication data for the HTTP request
     * @return A {@link Result} object containing the status code and body of the response
     */
=======
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
    public static Result getInputStream(
            final LastModifiedSource source, final AuthorizationProvider authorizationProvider) {
        final Result result = new Result();
        try {
            final long lastModified = source.getLastModified();
            final HttpURLConnection connection = UrlConnectionFactory.createConnection(
                    source.getURI().toURL(),
                    lastModified,
                    SslConfigurationFactory.getSslConfiguration(),
                    authorizationProvider);
            connection.connect();
            try {
                final int code = connection.getResponseCode();
                switch (code) {
                    case NOT_MODIFIED: {
<<<<<<< HEAD
                        LOGGER.debug(
                                "{} resource {}: not modified since {}",
                                formatProtocol(source),
                                () -> source,
                                () -> Instant.ofEpochMilli(lastModified));
=======
                        LOGGER.debug("Configuration not modified");
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
                        result.status = Status.NOT_MODIFIED;
                        return result;
                    }
                    case NOT_FOUND: {
<<<<<<< HEAD
                        LOGGER.debug("{} resource {}: not found", formatProtocol(source), () -> source);
=======
                        LOGGER.debug("Unable to access {}: Not Found", source.toString());
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
                        result.status = Status.NOT_FOUND;
                        return result;
                    }
                    case OK: {
                        try (final InputStream is = connection.getInputStream()) {
                            source.setLastModified(connection.getLastModified());
                            LOGGER.debug(
<<<<<<< HEAD
                                    "{} resource {}: last modified on {}",
                                    formatProtocol(source),
                                    () -> source,
                                    () -> Instant.ofEpochMilli(connection.getLastModified()));
                            result.status = Status.SUCCESS;
                            result.bytes = readStream(is);
                            return result;
                        } catch (final IOException e) {
                            try (final InputStream es = connection.getErrorStream()) {
                                if (LOGGER.isDebugEnabled()) {
                                    LOGGER.debug(
                                            "Error accessing {} resource at {}: {}",
                                            formatProtocol(source).get(),
                                            source,
                                            readStream(es),
                                            e);
                                }
                            } catch (final IOException ioe) {
                                LOGGER.debug(
                                        "Error accessing {} resource at {}",
                                        formatProtocol(source),
                                        () -> source,
                                        () -> e);
                            }
                            throw new ConfigurationException("Unable to access " + source, e);
                        }
                    }
                    case NOT_AUTHORIZED: {
                        throw new ConfigurationException("Authentication required for " + source);
                    }
                    case FORBIDDEN: {
                        throw new ConfigurationException("Access denied to " + source);
                    }
                    default: {
                        if (code < 0) {
                            LOGGER.debug("{} resource {}: invalid response code", formatProtocol(source), source);
                        } else {
                            LOGGER.debug(
                                    "{} resource {}: unexpected response code {}",
                                    formatProtocol(source),
                                    source,
                                    code);
                        }
                        throw new ConfigurationException("Unable to access " + source);
=======
                                    "Content was modified for {}. previous lastModified: {}, new lastModified: {}",
                                    source.toString(),
                                    lastModified,
                                    connection.getLastModified());
                            result.status = Status.SUCCESS;
                            result.inputStream = new ByteArrayInputStream(readStream(is));
                            return result;
                        } catch (final IOException e) {
                            try (final InputStream es = connection.getErrorStream()) {
                                LOGGER.info(
                                        "Error accessing configuration at {}: {}", source.toString(), readStream(es));
                            } catch (final IOException ioe) {
                                LOGGER.error(
                                        "Error accessing configuration at {}: {}", source.toString(), e.getMessage());
                            }
                            throw new ConfigurationException("Unable to access " + source.toString(), e);
                        }
                    }
                    case NOT_AUTHORIZED: {
                        throw new ConfigurationException("Authorization failed");
                    }
                    default: {
                        if (code < 0) {
                            LOGGER.info("Invalid response code returned");
                        } else {
                            LOGGER.info("Unexpected response code returned {}", code);
                        }
                        throw new ConfigurationException("Unable to access " + source.toString());
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
                    }
                }
            } finally {
                connection.disconnect();
            }
        } catch (IOException e) {
<<<<<<< HEAD
            LOGGER.debug("Error accessing {} resource at {}", formatProtocol(source), source, e);
            throw new ConfigurationException("Unable to access " + source, e);
        }
    }

    private static Supplier<String> formatProtocol(Source source) {
        return () -> toRootUpperCase(source.getURI().getScheme());
    }

=======
            LOGGER.warn("Error accessing {}: {}", source.toString(), e.getMessage());
            throw new ConfigurationException("Unable to access " + source.toString(), e);
        }
    }

>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
    public static byte[] readStream(final InputStream is) throws IOException {
        final ByteArrayOutputStream result = new ByteArrayOutputStream();
        final byte[] buffer = new byte[BUF_SIZE];
        int length;
        while ((length = is.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }
        return result.toByteArray();
    }

<<<<<<< HEAD
    @NullMarked
    public static class Result {

        private byte @Nullable [] bytes = null;
        private Status status;

        public Result() {
            this(Status.ERROR);
        }
=======
    public static class Result {

        private InputStream inputStream;
        private Status status;

        public Result() {}
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c

        public Result(final Status status) {
            this.status = status;
        }

<<<<<<< HEAD
        /**
         * Returns the data if the status is {@link Status#SUCCESS}.
         * <p>
         *     In any other case the result is {@code null}.
         * </p>
         * @return The contents of the HTTP response or null if empty.
         */
        public @Nullable InputStream getInputStream() {
            return bytes != null ? new ByteArrayInputStream(bytes) : null;
=======
        public InputStream getInputStream() {
            return inputStream;
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
        }

        public Status getStatus() {
            return status;
        }
    }
}
