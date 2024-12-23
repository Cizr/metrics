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
package org.apache.logging.log4j;

import static org.junit.jupiter.api.Assertions.assertNull;

<<<<<<< HEAD
import org.apache.logging.log4j.test.junit.SetTestProperty;
import org.apache.logging.log4j.test.junit.UsingThreadContextMap;
import org.junit.jupiter.api.Test;
=======
import org.apache.logging.log4j.test.junit.InitializesThreadContext;
import org.apache.logging.log4j.test.junit.UsingThreadContextMap;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.SetSystemProperty;
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c

/**
 * Tests {@link ThreadContext}.
 */
<<<<<<< HEAD
@SetTestProperty(key = "log4j2.disableThreadContext", value = "true")
@SetTestProperty(key = "log4j2.disableThreadContextMap", value = "true")
=======
@SetSystemProperty(key = "log4j2.disableThreadContext", value = "true")
@SetSystemProperty(key = "log4j2.disableThreadContextMap", value = "true")
@InitializesThreadContext
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
@UsingThreadContextMap
public class NoopThreadContextTest {

    @Test
    public void testNoop() {
        ThreadContext.put("Test", "Test");
        final String value = ThreadContext.get("Test");
        assertNull(value, "value was saved");
    }
}
