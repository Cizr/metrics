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
package org.apache.logging.log4j.core.pattern;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
<<<<<<< HEAD
import org.apache.logging.log4j.core.impl.LocationAware;
=======
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c

/**
 * Returns the event's line location information in a StringBuilder.
 */
@Plugin(name = "FileLocationPatternConverter", category = PatternConverter.CATEGORY)
@ConverterKeys({"F", "file"})
<<<<<<< HEAD
public final class FileLocationPatternConverter extends LogEventPatternConverter implements LocationAware {
=======
public final class FileLocationPatternConverter extends LogEventPatternConverter {
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
    /**
     * Singleton.
     */
    private static final FileLocationPatternConverter INSTANCE = new FileLocationPatternConverter();

    /**
     * Private constructor.
     */
    private FileLocationPatternConverter() {
        super("File Location", "file");
    }

    /**
     * Obtains an instance of pattern converter.
     *
     * @param options options, may be null.
     * @return instance of pattern converter.
     */
    public static FileLocationPatternConverter newInstance(final String[] options) {
        return INSTANCE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void format(final LogEvent event, final StringBuilder output) {
        final StackTraceElement element = event.getSource();

        if (element != null) {
            output.append(element.getFileName());
        }
    }
<<<<<<< HEAD

    @Override
    public boolean requiresLocation() {
        return true;
    }
=======
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
}