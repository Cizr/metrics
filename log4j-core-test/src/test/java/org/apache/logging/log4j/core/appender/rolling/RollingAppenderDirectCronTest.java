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
package org.apache.logging.log4j.core.appender.rolling;

<<<<<<< HEAD
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.waitAtMost;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.RollingFileAppender;
import org.apache.logging.log4j.core.test.junit.LoggerContextSource;
import org.apache.logging.log4j.core.test.junit.Named;
import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.test.junit.TempLoggingDir;
import org.apache.logging.log4j.test.junit.UsingStatusListener;
import org.junit.jupiter.api.Test;

@UsingStatusListener
class RollingAppenderDirectCronTest {

    private static final Pattern FILE_PATTERN =
            Pattern.compile("test-(\\d{4}-\\d{2}-\\d{2}T\\d{2}-\\d{2}-\\d{2})\\.log");

    @TempLoggingDir
    private static Path loggingPath;

    @Test
    @LoggerContextSource("classpath:appender/rolling/RollingAppenderDirectCronTest.xml")
    void testAppender(final LoggerContext ctx, @Named("RollingFile") final RollingFileAppender app) throws Exception {
        final Logger logger = ctx.getLogger(RollingAppenderDirectCronTest.class);
        int msgNumber = 1;
        logger.debug("This is test message number {}.", msgNumber++);
        assertThat(loggingPath).isNotEmptyDirectory();
        final RolloverDelay delay = new RolloverDelay(app.getManager());
        delay.waitForRollover();

        delay.reset(3);
        final int MAX_TRIES = 30;
        for (int i = 0; i < MAX_TRIES; ++i) {
            logger.debug("This is test message number {}.", msgNumber++);
            Thread.sleep(110);
=======
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.appender.RollingFileAppender;
import org.apache.logging.log4j.core.test.junit.LoggerContextRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;

/**
 *
 */
public class RollingAppenderDirectCronTest {

    private static final String CONFIG = "log4j-rolling-direct-cron.xml";
    private static final String DIR = "target/rolling-direct-cron";

    private final LoggerContextRule loggerContextRule =
            LoggerContextRule.createShutdownTimeoutLoggerContextRule(CONFIG);

    @Rule
    public RuleChain chain = loggerContextRule.withCleanFoldersRule(DIR);

    private final Pattern filePattern = Pattern.compile(".*(\\d\\d-\\d\\d-\\d\\d-\\d\\d-\\d\\d-\\d\\d).*$");

    @Test
    public void testAppender() throws Exception {
        // TODO Is there a better way to test than putting the thread to sleep all over the place?
        final RollingFileAppender app = loggerContextRule.getAppender("RollingFile");
        final Logger logger = loggerContextRule.getLogger();
        logger.debug("This is test message number 1");
        final RolloverDelay delay = new RolloverDelay(app.getManager());
        delay.waitForRollover();
        final File dir = new File(DIR);
        final File[] files = dir.listFiles();
        assertTrue("Directory not created", dir.exists() && files != null && files.length > 0);
        delay.reset(3);

        final int MAX_TRIES = 30;
        for (int i = 0; i < MAX_TRIES; ++i) {
            logger.debug("Adding new event {}", i);
            Thread.sleep(100);
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
        }
        delay.waitForRollover();
    }

<<<<<<< HEAD
    private static class RolloverDelay implements RolloverListener {
        private final Logger logger = StatusLogger.getLogger();
        private volatile CountDownLatch latch;
        private volatile AssertionError assertion;
=======
    private class RolloverDelay implements RolloverListener {
        private volatile CountDownLatch latch;
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c

        public RolloverDelay(final RollingFileManager manager) {
            latch = new CountDownLatch(1);
            manager.addRolloverListener(this);
        }

        public void waitForRollover() {
<<<<<<< HEAD
            waitAtMost(5, TimeUnit.SECONDS)
                    .alias("Rollover timeout")
                    .until(() -> latch.getCount() == 0 || assertion != null);
            if (assertion != null) {
                throw assertion;
=======
            try {
                if (!latch.await(3, TimeUnit.SECONDS)) {
                    fail("failed to rollover");
                }
            } catch (InterruptedException ex) {
                fail("failed to rollover");
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
            }
        }

        public void reset(final int count) {
            latch = new CountDownLatch(count);
        }

        @Override
<<<<<<< HEAD
        public void rolloverTriggered(final String fileName) {
            logger.info("Rollover triggered for file {}.", fileName);
        }

        @Override
        public void rolloverComplete(final String fileName) {
            logger.info("Rollover completed for file {}.", fileName);
            try {
                final Path path = Paths.get(fileName);
                final Matcher matcher = FILE_PATTERN.matcher(path.getFileName().toString());
                assertThat(matcher).as("Rolled file").matches();
                try {
                    final List<String> lines = Files.readAllLines(path);
                    assertThat(lines).isNotEmpty();
                    assertThat(lines.get(0)).startsWith(matcher.group(1));
                } catch (final IOException ex) {
                    fail("Unable to read file " + fileName + ": " + ex.getMessage());
                }
                latch.countDown();
            } catch (final AssertionError ex) {
                assertion = ex;
            }
=======
        public void rolloverTriggered(final String fileName) {}

        @Override
        public void rolloverComplete(final String fileName) {
            final java.util.regex.Matcher matcher = filePattern.matcher(fileName);
            assertTrue("Invalid file name: " + fileName, matcher.matches());
            final Path path = new File(fileName).toPath();
            try {
                final List<String> lines = Files.readAllLines(path);
                assertTrue("Not enough lines in " + fileName + ":" + lines.size(), lines.size() > 0);
                assertTrue(
                        "log and file times don't match. file: " + matcher.group(1) + ", log: " + lines.get(0),
                        lines.get(0).startsWith(matcher.group(1)));
            } catch (IOException ex) {
                fail("Unable to read file " + fileName + ": " + ex.getMessage());
            }
            latch.countDown();
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
        }
    }
}
