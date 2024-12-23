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
package org.apache.logging.log4j.core.config.xml;

import static org.assertj.core.api.Assertions.assertThat;
<<<<<<< HEAD
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import edu.umd.cs.findbugs.annotations.Nullable;
=======

import java.util.stream.StreamSupport;
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.test.junit.LoggerContextSource;
import org.apache.logging.log4j.core.util.Constants;
import org.apache.logging.log4j.status.StatusConsoleListener;
<<<<<<< HEAD
import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.test.junit.SetTestProperty;
import org.apache.logging.log4j.test.junit.UsingStatusLoggerMock;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@UsingStatusLoggerMock
@ExtendWith(MockitoExtension.class)
class XmlConfigurationPropsTest {

    private static final String CONFIG_NAME = "XmlConfigurationPropsTest";

    private static final String CONFIG1 = "org/apache/logging/log4j/core/config/xml/XmlConfigurationPropsTest1.xml";

    private static final String CONFIG1_NAME = "XmlConfigurationPropsTest1";

    private void testConfiguration(
            final Configuration config,
            final String expectedConfigName,
            @Nullable final Level expectedStatusLevel,
            @Nullable final Level expectedRootLevel) {
=======
import org.apache.logging.log4j.status.StatusListener;
import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.test.junit.SetTestProperty;
import org.apache.logging.log4j.test.junit.UsingTestProperties;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

@UsingTestProperties
public class XmlConfigurationPropsTest {

    private static final String CONFIG_NAME = "XmlConfigurationPropsTest";
    private static final String CONFIG1 = "org/apache/logging/log4j/core/config/xml/XmlConfigurationPropsTest1.xml";
    private static final String CONFIG1_NAME = "XmlConfigurationPropsTest1";

    private static StatusConsoleListener findStatusConsoleListener() {
        final Iterable<StatusListener> listeners = StatusLogger.getLogger().getListeners();
        return StreamSupport.stream(listeners.spliterator(), false)
                .filter(StatusConsoleListener.class::isInstance)
                .map(StatusConsoleListener.class::cast)
                .findAny()
                .orElseThrow(() -> new AssertionFailedError("Missing console status listener."));
    }

    private void testConfiguration(
            final Configuration config,
            final String expectedConfigName,
            final Level expectedStatusLevel,
            final Level expectedRootLevel) {
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
        assertThat(config)
                .isInstanceOf(XmlConfiguration.class)
                .extracting(Configuration::getName)
                .isEqualTo(expectedConfigName);
<<<<<<< HEAD
        final StatusConsoleListener fallbackListener = StatusLogger.getLogger().getFallbackListener();
        if (expectedStatusLevel == null) {
            verify(fallbackListener, never()).setLevel(any());
        } else {
            verify(fallbackListener).setLevel(eq(expectedStatusLevel));
        }
        assertThat(config.getRootLogger().getExplicitLevel()).isEqualTo(expectedRootLevel);
    }

    @Test
    @SetTestProperty(key = "status.level", value = "using gibberish values to enforce defaults")
    @SetTestProperty(key = "root.level", value = "using gibberish values to enforce defaults")
    @LoggerContextSource
    void testNoProps(final Configuration config) {
        testConfiguration(config, CONFIG_NAME, null, null);
    }

    @Test
    @SetTestProperty(key = StatusLogger.DEFAULT_STATUS_LISTENER_LEVEL, value = "INFO")
    @SetTestProperty(key = Constants.LOG4J_DEFAULT_STATUS_LEVEL, value = "WARN")
    @LoggerContextSource(value = CONFIG1)
    void testDefaultStatus(final Configuration config) {
        testConfiguration(config, CONFIG1_NAME, Level.INFO, null);
=======
        assertThat(config.getRootLogger().getExplicitLevel()).isEqualTo(expectedRootLevel);
        assertThat(findStatusConsoleListener().getStatusLevel()).isEqualTo(expectedStatusLevel);
    }

    @Test
    @LoggerContextSource
    public void testNoProps(final Configuration config) {
        testConfiguration(config, CONFIG_NAME, Level.ERROR, null);
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
    }

    @Test
    @SetTestProperty(key = Constants.LOG4J_DEFAULT_STATUS_LEVEL, value = "WARN")
    @LoggerContextSource(value = CONFIG1)
<<<<<<< HEAD
    void testDeprecatedDefaultStatus(final Configuration config) {
=======
    public void testDefaultStatus(final Configuration config) {
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
        testConfiguration(config, CONFIG1_NAME, Level.WARN, null);
    }

    @Test
    @SetTestProperty(key = "status.level", value = "INFO")
    @LoggerContextSource
<<<<<<< HEAD
    void testWithConfigProp(final Configuration config) {
=======
    public void testWithConfigProp(final Configuration config) {
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
        testConfiguration(config, CONFIG_NAME, Level.INFO, null);
    }

    @Test
    @SetTestProperty(key = "status.level", value = "INFO")
    @SetTestProperty(key = "root.level", value = "DEBUG")
    @LoggerContextSource
<<<<<<< HEAD
    void testWithProps(final Configuration config) {
=======
    public void testWithProps(final Configuration config) {
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
        testConfiguration(config, CONFIG_NAME, Level.INFO, Level.DEBUG);
    }
}
