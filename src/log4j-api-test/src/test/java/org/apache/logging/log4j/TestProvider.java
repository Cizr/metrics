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

import org.apache.logging.log4j.spi.Provider;
import org.apache.logging.log4j.test.TestLoggerContextFactory;

/**
 * Binding for the Log4j API.
 */
public class TestProvider extends Provider {
    public TestProvider() {
<<<<<<< HEAD
        super(5, CURRENT_VERSION, TestLoggerContextFactory.class);
=======
        super(0, "2.6.0", TestLoggerContextFactory.class);
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
    }
}
