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
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.MessageFactory;
import org.apache.logging.log4j.message.ParameterizedNoReferenceMessageFactory;
import org.junit.jupiter.api.Test;
=======
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogBuilder;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.MessageFactory;
import org.apache.logging.log4j.message.ParameterizedNoReferenceMessageFactory;
import org.apache.logging.log4j.simple.SimpleLogger;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c

public class StatusConsoleListenerTest {

    public static final MessageFactory MESSAGE_FACTORY = ParameterizedNoReferenceMessageFactory.INSTANCE;

    @Test
<<<<<<< HEAD
    void StatusData_getFormattedStatus_should_be_used() {

        // Create the listener.
        final PrintStream stream = mock(PrintStream.class);
        final StatusConsoleListener listener = new StatusConsoleListener(Level.ALL, stream);

        // Log a message.
        final Message message = mock(Message.class);
        final StatusData statusData = spy(new StatusData(null, Level.TRACE, message, null, null));
        listener.log(statusData);

        // Verify the call.
        verify(statusData).getFormattedStatus();
    }

    @Test
    void stream_should_be_honored() throws Exception {
=======
    void SimpleLogger_should_be_used() {

        // Create a mock `SimpleLoggerFactory`.
        final SimpleLogger logger = Mockito.mock(SimpleLogger.class);
        final LogBuilder logBuilder = Mockito.mock(LogBuilder.class);
        Mockito.when(logger.atLevel(Mockito.any())).thenReturn(logBuilder);
        Mockito.when(logBuilder.withThrowable(Mockito.any())).thenReturn(logBuilder);
        Mockito.when(logBuilder.withLocation(Mockito.any())).thenReturn(logBuilder);
        final SimpleLoggerFactory loggerFactory = Mockito.mock(SimpleLoggerFactory.class);
        Mockito.when(loggerFactory.createSimpleLogger(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(logger);

        // Create the listener.
        final PrintStream stream = Mockito.mock(PrintStream.class);
        final Level level = Mockito.mock(Level.class);
        final StatusConsoleListener listener = new StatusConsoleListener(level, stream, loggerFactory);

        // Log a message.
        final StackTraceElement caller = Mockito.mock(StackTraceElement.class);
        final Message message = Mockito.mock(Message.class);
        final Throwable throwable = Mockito.mock(Throwable.class);
        final StatusData statusData = new StatusData(caller, level, message, throwable, null);
        listener.log(statusData);

        // Verify the call.
        Mockito.verify(loggerFactory)
                .createSimpleLogger(
                        Mockito.eq("StatusConsoleListener"), Mockito.same(level), Mockito.any(), Mockito.same(stream));
        Mockito.verify(logger).atLevel(Mockito.same(level));
        Mockito.verify(logBuilder).withThrowable(Mockito.same(throwable));
        Mockito.verify(logBuilder).withLocation(Mockito.same(caller));
        Mockito.verify(logBuilder).log(Mockito.same(message));
    }

    @Test
    void level_and_stream_should_be_honored() throws Exception {
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c

        // Create the listener.
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final String encoding = "UTF-8";
        final PrintStream printStream = new PrintStream(outputStream, false, encoding);
        final StatusConsoleListener listener = new StatusConsoleListener(Level.WARN, printStream);

<<<<<<< HEAD
        // log a message that is expected to be logged.
=======
        // First, log a message that is expected to be logged.
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
        final RuntimeException expectedThrowable = new RuntimeException("expectedThrowable");
        expectedThrowable.setStackTrace(new StackTraceElement[] {
            new StackTraceElement("expectedThrowableClass", "expectedThrowableMethod", "expectedThrowableFile", 1)
        });
        final Message expectedMessage = MESSAGE_FACTORY.newMessage("expectedMessage");
        listener.log(new StatusData(
                null, // since ignored by `SimpleLogger`
                Level.WARN,
                expectedMessage,
                expectedThrowable,
                null)); // as set by `StatusLogger` itself

<<<<<<< HEAD
=======
        // Second, log a message that is expected to be discarded due to its insufficient level.
        final RuntimeException discardedThrowable = new RuntimeException("discardedThrowable");
        discardedThrowable.setStackTrace(new StackTraceElement[] {
            new StackTraceElement("discardedThrowableClass", "discardedThrowableMethod", "discardedThrowableFile", 2)
        });
        final Message discardedMessage = MESSAGE_FACTORY.newMessage("discardedMessage");
        listener.log(new StatusData(
                null, // since ignored by `SimpleLogger`
                Level.INFO,
                discardedMessage,
                discardedThrowable,
                null)); // as set by `StatusLogger` itself

>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
        // Collect the output.
        printStream.flush();
        final String output = outputStream.toString(encoding);

        // Verify the output.
<<<<<<< HEAD
        assertThat(output).contains(expectedThrowable.getMessage()).contains(expectedMessage.getFormattedMessage());
    }

    @Test
    void non_system_streams_should_be_closed() {
        final PrintStream stream = mock(PrintStream.class);
        final StatusConsoleListener listener = new StatusConsoleListener(Level.WARN, stream);
        listener.close();
        verify(stream).close();
    }

    @Test
    void close_should_reset_to_initials() {

        // Create the listener
        final PrintStream initialStream = mock(PrintStream.class);
        final Level initialLevel = Level.TRACE;
        final StatusConsoleListener listener = new StatusConsoleListener(initialLevel, initialStream);

        // Verify the initial state
        assertThat(listener.getStatusLevel()).isEqualTo(initialLevel);
        assertThat(listener).hasFieldOrPropertyWithValue("stream", initialStream);

        // Update the state
        final PrintStream newStream = mock(PrintStream.class);
        listener.setStream(newStream);
        final Level newLevel = Level.DEBUG;
        listener.setLevel(newLevel);

        // Verify the update
        verify(initialStream).close();
        assertThat(listener.getStatusLevel()).isEqualTo(newLevel);
        assertThat(listener).hasFieldOrPropertyWithValue("stream", newStream);

        // Close the listener
        listener.close();

        // Verify the reset
        verify(newStream).close();
        assertThat(listener.getStatusLevel()).isEqualTo(initialLevel);
        assertThat(listener).hasFieldOrPropertyWithValue("stream", initialStream);
=======
        Assertions.assertThat(output)
                .contains(expectedThrowable.getMessage())
                .contains(expectedMessage.getFormattedMessage())
                .doesNotContain(discardedThrowable.getMessage())
                .doesNotContain(discardedMessage.getFormattedMessage());
    }

    @Test
    void filters_should_be_honored() throws Exception {

        // Create the listener.
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final String encoding = "UTF-8";
        final PrintStream printStream = new PrintStream(outputStream, false, encoding);
        final StatusConsoleListener listener = new StatusConsoleListener(Level.TRACE, printStream);

        // Set the filter.
        final StackTraceElement caller = new StackTraceElement("callerClass", "callerMethod", "callerFile", 1);
        listener.setFilters(caller.getClassName());

        // Log the message to be filtered.
        final Message message = MESSAGE_FACTORY.newMessage("foo");
        listener.log(new StatusData(caller, Level.TRACE, message, null, null)); // as set by `StatusLogger` itself

        // Verify the filtering.
        printStream.flush();
        final String output = outputStream.toString(encoding);
        Assertions.assertThat(output).isEmpty();
    }

    @Test
    void non_system_streams_should_be_closed() throws Exception {
        final PrintStream stream = Mockito.mock(PrintStream.class);
        final StatusConsoleListener listener = new StatusConsoleListener(Level.WARN, stream);
        listener.close();
        Mockito.verify(stream).close();
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
    }
}
