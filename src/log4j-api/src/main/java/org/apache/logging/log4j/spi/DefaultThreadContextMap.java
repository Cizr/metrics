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

<<<<<<< HEAD
import static org.apache.logging.log4j.internal.map.UnmodifiableArrayBackedMap.getMap;

=======
import java.util.Collections;
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.apache.logging.log4j.util.BiConsumer;
import org.apache.logging.log4j.util.PropertiesUtil;
import org.apache.logging.log4j.util.ReadOnlyStringMap;
import org.apache.logging.log4j.util.TriConsumer;

/**
<<<<<<< HEAD
 * The default implementation of {@link ThreadContextMap}
 * <p>
 *      An instance of UnmodifiableArrayBackedMap can be represented as a single {@code Object[]}, which can safely
 *      be stored on the {@code ThreadLocal} with no fear of classloader-related memory leaks.
 *  </p>
 *  <p>
 *      Performance of the underlying {@link org.apache.logging.log4j.internal.map.UnmodifiableArrayBackedMap} exceeds
 *      {@link HashMap} in all supported operations other than {@code get()}. Note that {@code get()} performance scales
 *      linearly with the current map size, and callers are advised to minimize this work.
 * </p>
 */
public class DefaultThreadContextMap implements ThreadContextMap, ReadOnlyStringMap {
    private static final long serialVersionUID = -2635197170958057849L;

    /**
     * Property name ({@value}) for selecting {@code InheritableThreadLocal} (value "true") or plain
=======
 * The actual ThreadContext Map. A new ThreadContext Map is created each time it is updated and the Map stored is always
 * immutable. This means the Map can be passed to other threads without concern that it will be updated. Since it is
 * expected that the Map will be passed to many more log events than the number of keys it contains the performance
 * should be much better than if the Map was copied for each event.
 */
public class DefaultThreadContextMap implements ThreadContextMap, ReadOnlyStringMap {
    private static final long serialVersionUID = 8218007901108944053L;

    /**
     * Property name ({@value} ) for selecting {@code InheritableThreadLocal} (value "true") or plain
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
     * {@code ThreadLocal} (value is not "true") in the implementation.
     */
    public static final String INHERITABLE_MAP = "isThreadContextMapInheritable";

<<<<<<< HEAD
    private ThreadLocal<Object[]> localState;

    public DefaultThreadContextMap() {
        this(PropertiesUtil.getProperties());
    }

    /**
     * @deprecated Since 2.24.0. Use {@link NoOpThreadContextMap} for a no-op implementation.
     */
    @Deprecated
    public DefaultThreadContextMap(final boolean ignored) {
        this(PropertiesUtil.getProperties());
    }

    DefaultThreadContextMap(final PropertiesUtil properties) {
        localState = properties.getBooleanProperty(INHERITABLE_MAP)
                ? new InheritableThreadLocal<Object[]>() {
                    @Override
                    protected Object[] childValue(final Object[] parentValue) {
                        return parentValue;
                    }
                }
                : new ThreadLocal<>();
=======
    private final boolean useMap;
    private final ThreadLocal<Map<String, String>> localMap;

    private static boolean inheritableMap;

    static {
        init();
    }

    // LOG4J2-479: by default, use a plain ThreadLocal, only use InheritableThreadLocal if configured.
    // (This method is package protected for JUnit tests.)
    static ThreadLocal<Map<String, String>> createThreadLocalMap(final boolean isMapEnabled) {
        if (inheritableMap) {
            return new InheritableThreadLocal<Map<String, String>>() {
                @Override
                protected Map<String, String> childValue(final Map<String, String> parentValue) {
                    return parentValue != null && isMapEnabled //
                            ? Collections.unmodifiableMap(new HashMap<>(parentValue)) //
                            : null;
                }
            };
        }
        // if not inheritable, return plain ThreadLocal with null as initial value
        return new ThreadLocal<>();
    }

    static void init() {
        inheritableMap = PropertiesUtil.getProperties().getBooleanProperty(INHERITABLE_MAP);
    }

    public DefaultThreadContextMap() {
        this(true);
    }

    public DefaultThreadContextMap(final boolean useMap) {
        this.useMap = useMap;
        this.localMap = createThreadLocalMap(useMap);
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
    }

    @Override
    public void put(final String key, final String value) {
<<<<<<< HEAD
        final Object[] state = localState.get();
        localState.set(getMap(state).copyAndPut(key, value).getBackingArray());
    }

    public void putAll(final Map<String, String> m) {
        final Object[] state = localState.get();
        localState.set(getMap(state).copyAndPutAll(m).getBackingArray());
=======
        if (!useMap) {
            return;
        }
        Map<String, String> map = localMap.get();
        map = map == null ? new HashMap<>(1) : new HashMap<>(map);
        map.put(key, value);
        localMap.set(Collections.unmodifiableMap(map));
    }

    public void putAll(final Map<String, String> m) {
        if (!useMap) {
            return;
        }
        Map<String, String> map = localMap.get();
        map = map == null ? new HashMap<>(m.size()) : new HashMap<>(map);
        for (final Map.Entry<String, String> e : m.entrySet()) {
            map.put(e.getKey(), e.getValue());
        }
        localMap.set(Collections.unmodifiableMap(map));
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
    }

    @Override
    public String get(final String key) {
<<<<<<< HEAD
        final Object[] state = localState.get();
        return state == null ? null : getMap(state).get(key);
=======
        final Map<String, String> map = localMap.get();
        return map == null ? null : map.get(key);
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
    }

    @Override
    public void remove(final String key) {
<<<<<<< HEAD
        final Object[] state = localState.get();
        if (state != null) {
            localState.set(getMap(state).copyAndRemove(key).getBackingArray());
=======
        final Map<String, String> map = localMap.get();
        if (map != null) {
            final Map<String, String> copy = new HashMap<>(map);
            copy.remove(key);
            localMap.set(Collections.unmodifiableMap(copy));
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
        }
    }

    public void removeAll(final Iterable<String> keys) {
<<<<<<< HEAD
        final Object[] state = localState.get();
        if (state != null) {
            localState.set(getMap(state).copyAndRemoveAll(keys).getBackingArray());
=======
        final Map<String, String> map = localMap.get();
        if (map != null) {
            final Map<String, String> copy = new HashMap<>(map);
            for (final String key : keys) {
                copy.remove(key);
            }
            localMap.set(Collections.unmodifiableMap(copy));
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
        }
    }

    @Override
    public void clear() {
<<<<<<< HEAD
        localState.remove();
=======
        localMap.remove();
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
    }

    @Override
    public Map<String, String> toMap() {
        return getCopy();
    }

    @Override
    public boolean containsKey(final String key) {
<<<<<<< HEAD
        final Object[] state = localState.get();
        return state != null && getMap(state).containsKey(key);
=======
        final Map<String, String> map = localMap.get();
        return map != null && map.containsKey(key);
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
    }

    @Override
    public <V> void forEach(final BiConsumer<String, ? super V> action) {
<<<<<<< HEAD
        final Object[] state = localState.get();
        if (state == null) {
            return;
        }
        getMap(state).forEach(action);
=======
        final Map<String, String> map = localMap.get();
        if (map == null) {
            return;
        }
        for (final Map.Entry<String, String> entry : map.entrySet()) {
            // BiConsumer should be able to handle values of any type V. In our case the values are of type String.
            @SuppressWarnings("unchecked")
            final V value = (V) entry.getValue();
            action.accept(entry.getKey(), value);
        }
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
    }

    @Override
    public <V, S> void forEach(final TriConsumer<String, ? super V, S> action, final S state) {
<<<<<<< HEAD
        final Object[] localState = this.localState.get();
        if (localState == null) {
            return;
        }
        getMap(localState).forEach(action, state);
=======
        final Map<String, String> map = localMap.get();
        if (map == null) {
            return;
        }
        for (final Map.Entry<String, String> entry : map.entrySet()) {
            // TriConsumer should be able to handle values of any type V. In our case the values are of type String.
            @SuppressWarnings("unchecked")
            final V value = (V) entry.getValue();
            action.accept(entry.getKey(), value, state);
        }
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
    }

    @SuppressWarnings("unchecked")
    @Override
    public <V> V getValue(final String key) {
<<<<<<< HEAD
        return (V) get(key);
=======
        final Map<String, String> map = localMap.get();
        return (V) (map == null ? null : map.get(key));
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
    }

    @Override
    public Map<String, String> getCopy() {
<<<<<<< HEAD
        final Object[] state = localState.get();
        if (state == null) {
            return new HashMap<>(0);
        }
        return new HashMap<>(getMap(state));
=======
        final Map<String, String> map = localMap.get();
        return map == null ? new HashMap<>() : new HashMap<>(map);
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
    }

    @Override
    public Map<String, String> getImmutableMapOrNull() {
<<<<<<< HEAD
        final Object[] state = localState.get();
        return (state == null ? null : getMap(state));
=======
        return localMap.get();
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
    }

    @Override
    public boolean isEmpty() {
<<<<<<< HEAD
        return size() == 0;
=======
        final Map<String, String> map = localMap.get();
        return map == null || map.isEmpty();
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
    }

    @Override
    public int size() {
<<<<<<< HEAD
        final Object[] state = localState.get();
        return getMap(state).size();
=======
        final Map<String, String> map = localMap.get();
        return map == null ? 0 : map.size();
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
    }

    @Override
    public String toString() {
<<<<<<< HEAD
        final Object[] state = localState.get();
        return state == null ? "{}" : getMap(state).toString();
=======
        final Map<String, String> map = localMap.get();
        return map == null ? "{}" : map.toString();
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
<<<<<<< HEAD
        final Object[] state = localState.get();
        result = prime * result + ((state == null) ? 0 : getMap(state).hashCode());
=======
        final Map<String, String> map = this.localMap.get();
        result = prime * result + ((map == null) ? 0 : map.hashCode());
        result = prime * result + Boolean.valueOf(this.useMap).hashCode();
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
<<<<<<< HEAD
=======
        if (obj instanceof DefaultThreadContextMap) {
            final DefaultThreadContextMap other = (DefaultThreadContextMap) obj;
            if (this.useMap != other.useMap) {
                return false;
            }
        }
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
        if (!(obj instanceof ThreadContextMap)) {
            return false;
        }
        final ThreadContextMap other = (ThreadContextMap) obj;
<<<<<<< HEAD
        final Map<String, String> map = getMap(localState.get());
=======
        final Map<String, String> map = this.localMap.get();
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
        final Map<String, String> otherMap = other.getImmutableMapOrNull();
        return Objects.equals(map, otherMap);
    }
}
