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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.test.appender.ListAppender;
import org.apache.logging.log4j.core.test.junit.LoggerContextSource;
import org.apache.logging.log4j.core.test.junit.Named;
import org.junit.jupiter.api.Test;

<<<<<<< HEAD
@LoggerContextSource("SequenceNumberPatternConverterTest.yaml")
=======
@LoggerContextSource("SequenceNumberPatternConverterTest.yml")
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
public class SequenceNumberPatternConverterTest {

    @Test
    public void testSequenceIncreases(final LoggerContext context, @Named("List") final ListAppender app) {
        app.clear();
        final Logger logger = context.getLogger(getClass().getName());
        logger.info("Message 1");
        logger.info("Message 2");
        logger.info("Message 3");
        logger.info("Message 4");
        logger.info("Message 5");

        final List<String> messages = app.getMessages();
        assertThat(messages, contains("1", "2", "3", "4", "5"));
    }
}
