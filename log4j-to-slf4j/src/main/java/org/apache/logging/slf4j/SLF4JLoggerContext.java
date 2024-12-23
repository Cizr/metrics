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
package org.apache.logging.slf4j;

import org.apache.logging.log4j.message.MessageFactory;
<<<<<<< HEAD
import org.apache.logging.log4j.message.ParameterizedMessageFactory;
import org.apache.logging.log4j.spi.ExtendedLogger;
import org.apache.logging.log4j.spi.LoggerContext;
import org.apache.logging.log4j.spi.LoggerRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SLF4JLoggerContext implements LoggerContext {

    private final LoggerRegistry<ExtendedLogger> loggerRegistry = new LoggerRegistry<>();

    private static final MessageFactory DEFAULT_MESSAGE_FACTORY = ParameterizedMessageFactory.INSTANCE;

=======
import org.apache.logging.log4j.spi.ExtendedLogger;
import org.apache.logging.log4j.spi.LoggerContext;
import org.apache.logging.log4j.spi.LoggerRegistry;
import org.slf4j.LoggerFactory;

public class SLF4JLoggerContext implements LoggerContext {
    private final LoggerRegistry<ExtendedLogger> loggerRegistry = new LoggerRegistry<>();

>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
    @Override
    public Object getExternalContext() {
        return null;
    }

    @Override
    public ExtendedLogger getLogger(final String name) {
<<<<<<< HEAD
        return getLogger(name, DEFAULT_MESSAGE_FACTORY);
=======
        if (!loggerRegistry.hasLogger(name)) {
            loggerRegistry.putIfAbsent(name, null, new SLF4JLogger(name, LoggerFactory.getLogger(name)));
        }
        return loggerRegistry.getLogger(name);
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
    }

    @Override
    public ExtendedLogger getLogger(final String name, final MessageFactory messageFactory) {
<<<<<<< HEAD
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

    private static ExtendedLogger createLogger(final String name, final MessageFactory messageFactory) {
        final Logger logger = LoggerFactory.getLogger(name);
        return new SLF4JLogger(name, messageFactory, logger);
=======
        if (!loggerRegistry.hasLogger(name, messageFactory)) {
            loggerRegistry.putIfAbsent(
                    name, messageFactory, new SLF4JLogger(name, messageFactory, LoggerFactory.getLogger(name)));
        }
        return loggerRegistry.getLogger(name, messageFactory);
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
}
