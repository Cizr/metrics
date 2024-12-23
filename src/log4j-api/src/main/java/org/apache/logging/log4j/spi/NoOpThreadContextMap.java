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
package org.apache.logging.log4j.spi;

import java.util.HashMap;
import java.util.Map;
<<<<<<< HEAD
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
=======
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c

/**
 * {@code ThreadContextMap} implementation used when either of system properties {@code disableThreadContextMap} or .
 * {@code disableThreadContext} is {@code true}. This implementation does nothing.
 *
 * @since 2.7
 */
<<<<<<< HEAD
@NullMarked
public class NoOpThreadContextMap implements ThreadContextMap {

    /**
     * @since 2.24.0
     */
    public static final ThreadContextMap INSTANCE = new NoOpThreadContextMap();

=======
public class NoOpThreadContextMap implements ThreadContextMap {
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
    @Override
    public void clear() {}

    @Override
    public boolean containsKey(final String key) {
        return false;
    }

    @Override
<<<<<<< HEAD
    public @Nullable String get(final String key) {
=======
    public String get(final String key) {
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
        return null;
    }

    @Override
    public Map<String, String> getCopy() {
        return new HashMap<>();
    }

    @Override
<<<<<<< HEAD
    public @Nullable Map<String, String> getImmutableMapOrNull() {
=======
    public Map<String, String> getImmutableMapOrNull() {
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
        return null;
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public void put(final String key, final String value) {}

    @Override
    public void remove(final String key) {}
}
