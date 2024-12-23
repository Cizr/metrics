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
package org.apache.logging.log4j.core.appender;

<<<<<<< HEAD
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
=======
import static org.junit.jupiter.api.Assertions.*;
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.test.junit.LoggerContextSource;
import org.apache.logging.log4j.status.StatusData;
import org.apache.logging.log4j.status.StatusLogger;
import org.junit.jupiter.api.Test;
<<<<<<< HEAD
import org.junitpioneer.jupiter.SetSystemProperty;
=======
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c

/**
 * OutputStreamManager Tests.
 */
<<<<<<< HEAD
@SetSystemProperty(key = "log4j2.status.entries", value = "10")
class OutputStreamManagerTest {

    @Test
    @LoggerContextSource("multipleIncompatibleAppendersTest.xml")
    void narrow(final LoggerContext context) {
        final Logger logger = context.getLogger(OutputStreamManagerTest.class);
        logger.info("test");
        StatusLogger statusLogger = StatusLogger.getLogger();
        final List<StatusData> events = statusLogger.getStatusData();
        assertThat(events).isNotEmpty();
        StatusData event = events.get(0);
        if (event.getMessage().getFormattedMessage().contains("WindowsAnsiOutputStream")) {
            event = events.get(1);
        }
        assertThat(event.getLevel()).isEqualTo(Level.ERROR);
        assertThat(event.getMessage().getFormattedMessage())
                .isEqualTo(
                        "Could not create plugin of type class org.apache.logging.log4j.core.appender.RollingRandomAccessFileAppender for element RollingRandomAccessFile");
        assertThat(event.getThrowable())
                .isNotNull()
                .asString()
                .isEqualTo(
                        "org.apache.logging.log4j.core.config.ConfigurationException: Configuration has multiple incompatible Appenders pointing to the same resource 'target/multiIncompatibleAppender.log'");
=======
public class OutputStreamManagerTest {

    @Test
    @LoggerContextSource("multipleIncompatibleAppendersTest.xml")
    public void narrow(final LoggerContext context) {
        final Logger logger = context.getLogger(OutputStreamManagerTest.class);
        logger.info("test");
        final List<StatusData> statusData = StatusLogger.getLogger().getStatusData();
        StatusData data = statusData.get(0);
        if (data.getMessage().getFormattedMessage().contains("WindowsAnsiOutputStream")) {
            data = statusData.get(1);
        }
        assertEquals(Level.ERROR, data.getLevel());
        assertEquals(
                "Could not create plugin of type class org.apache.logging.log4j.core.appender.RollingRandomAccessFileAppender for element RollingRandomAccessFile",
                data.getMessage().getFormattedMessage());
        assertEquals(
                "org.apache.logging.log4j.core.config.ConfigurationException: Configuration has multiple incompatible Appenders pointing to the same resource 'target/multiIncompatibleAppender.log'",
                data.getThrowable().toString());
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
    }

    @Test
    public void testOutputStreamAppenderFlushClearsBufferOnException() {
        final IOException exception = new IOException();
        final OutputStream throwingOutputStream = new OutputStream() {
            @Override
            public void write(final int b) throws IOException {
                throw exception;
            }
        };

        final int bufferSize = 3;
        final OutputStreamManager outputStreamManager =
                new OutputStreamManager(throwingOutputStream, "test", null, false, bufferSize);

        for (int i = 0; i < bufferSize - 1; i++) {
            outputStreamManager.getByteBuffer().put((byte) 0);
        }

        assertEquals(outputStreamManager.getByteBuffer().remaining(), 1);

        final AppenderLoggingException appenderLoggingException = assertThrows(
                AppenderLoggingException.class,
                () -> outputStreamManager.flushBuffer(outputStreamManager.getByteBuffer()));
        assertEquals(appenderLoggingException.getCause(), exception);

        assertEquals(
                outputStreamManager.getByteBuffer().limit(),
                outputStreamManager.getByteBuffer().capacity());
    }
}
