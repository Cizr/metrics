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
package org.apache.logging.log4j.core.config;

<<<<<<< HEAD
import static java.util.Objects.requireNonNull;
=======
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import com.sun.management.UnixOperatingSystemMXBean;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
<<<<<<< HEAD
=======
import java.nio.file.Paths;
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
<<<<<<< HEAD
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.core.net.UrlConnectionFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class ConfigurationSourceTest {
    /**
     * The path inside the jar created by {@link #prepareJarConfigURL} containing the configuration.
     */
    public static final String PATH_IN_JAR = "/config/console.xml";

    private static final String CONFIG_FILE = "/config/ConfigurationSourceTest.xml";

    @TempDir
    private Path tempDir;

    @Test
    void testJira_LOG4J2_2770_byteArray() throws Exception {
=======
import org.apache.logging.log4j.core.net.UrlConnectionFactory;
import org.junit.jupiter.api.Test;

public class ConfigurationSourceTest {
    private static final Path JAR_FILE = Paths.get("target", "test-classes", "jarfile.jar");
    private static final Path CONFIG_FILE = Paths.get("target", "test-classes", "log4j2-console.xml");
    private static final byte[] buffer = new byte[1024];

    @Test
    public void testJira_LOG4J2_2770_byteArray() throws Exception {
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
        final ConfigurationSource configurationSource =
                new ConfigurationSource(new ByteArrayInputStream(new byte[] {'a', 'b'}));
        assertNotNull(configurationSource.resetInputStream());
    }

    /**
     * Checks if the usage of 'jar:' URLs does not increase the file descriptor
<<<<<<< HEAD
     * count, and the jar file can be deleted.
     */
    @Test
    void testNoJarFileLeak() throws Exception {
        final Path jarFile = prepareJarConfigURL(tempDir);
        final URL jarConfigURL = new URL("jar:" + jarFile.toUri().toURL() + "!" + PATH_IN_JAR);
=======
     * count and the jar file can be deleted.
     *
     * @throws Exception
     */
    @Test
    public void testNoJarFileLeak() throws Exception {
        final URL jarConfigURL = prepareJarConfigURL();
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
        final long expected = getOpenFileDescriptorCount();
        UrlConnectionFactory.createConnection(jarConfigURL).getInputStream().close();
        // This can only fail on UNIX
        assertEquals(expected, getOpenFileDescriptorCount());
        // This can only fail on Windows
        try {
<<<<<<< HEAD
            Files.delete(jarFile);
=======
            Files.delete(JAR_FILE);
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
        } catch (IOException e) {
            fail(e);
        }
    }

    @Test
    public void testLoadConfigurationSourceFromJarFile() throws Exception {
<<<<<<< HEAD
        final Path jarFile = prepareJarConfigURL(tempDir);
        final URL jarConfigURL = new URL("jar:" + jarFile.toUri().toURL() + "!" + PATH_IN_JAR);
=======
        final URL jarConfigURL = prepareJarConfigURL();
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
        final long expectedFdCount = getOpenFileDescriptorCount();
        ConfigurationSource configSource = ConfigurationSource.fromUri(jarConfigURL.toURI());
        assertNotNull(configSource);
        assertEquals(jarConfigURL.toString(), configSource.getLocation());
        assertNull(configSource.getFile());
        assertTrue(configSource.getLastModified() > 0);
        assertEquals(jarConfigURL, configSource.getURL());
        assertNotNull(configSource.getInputStream());
        configSource.getInputStream().close();

        // This can only fail on UNIX
        assertEquals(expectedFdCount, getOpenFileDescriptorCount());
        // This can only fail on Windows
        try {
<<<<<<< HEAD
            Files.delete(jarFile);
=======
            Files.delete(JAR_FILE);
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
        } catch (IOException e) {
            fail(e);
        }
    }

    private long getOpenFileDescriptorCount() {
        final OperatingSystemMXBean os = ManagementFactory.getOperatingSystemMXBean();
        if (os instanceof UnixOperatingSystemMXBean) {
            return ((UnixOperatingSystemMXBean) os).getOpenFileDescriptorCount();
        }
        return 0L;
    }

<<<<<<< HEAD
    public static Path prepareJarConfigURL(Path dir) throws IOException {
        Path jarFile = dir.resolve("jarFile.jar");
        final Manifest manifest = new Manifest();
        manifest.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, "1.0");
        try (final OutputStream os = Files.newOutputStream(jarFile);
                final JarOutputStream jar = new JarOutputStream(os, manifest);
                final InputStream config =
                        requireNonNull(ConfigurationSourceTest.class.getResourceAsStream(CONFIG_FILE))) {
            final JarEntry jarEntry = new JarEntry("config/console.xml");
            jar.putNextEntry(jarEntry);
            IOUtils.copy(config, os);
        }
        return jarFile;
=======
    public static URL prepareJarConfigURL() throws IOException {
        if (!Files.exists(JAR_FILE)) {
            final Manifest manifest = new Manifest();
            manifest.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, "1.0");
            try (final OutputStream os = Files.newOutputStream(JAR_FILE);
                    final JarOutputStream jar = new JarOutputStream(os, manifest);
                    final InputStream config = Files.newInputStream(CONFIG_FILE)) {
                final JarEntry jarEntry = new JarEntry("config/console.xml");
                jar.putNextEntry(jarEntry);
                int len;
                while ((len = config.read(buffer)) != -1) {
                    jar.write(buffer, 0, len);
                }
                jar.closeEntry();
            }
        }
        return new URL("jar:" + JAR_FILE.toUri().toURL() + "!/config/console.xml");
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
    }
}
