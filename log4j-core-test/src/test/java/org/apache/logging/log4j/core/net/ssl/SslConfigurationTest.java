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
package org.apache.logging.log4j.core.net.ssl;

<<<<<<< HEAD
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
=======
import static org.junit.jupiter.api.Assertions.*;
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c

import java.io.IOException;
import java.io.OutputStream;
import java.net.UnknownHostException;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import org.junit.jupiter.api.Test;

<<<<<<< HEAD
class SslConfigurationTest {

    private static final String TLS_TEST_HOST = "apache.org";
    private static final int TLS_TEST_PORT = 443;

    private static SslConfiguration createTestSslConfigurationResources() throws StoreConfigurationException {
=======
public class SslConfigurationTest {

    private static final String TLS_TEST_HOST = "login.yahoo.com";
    private static final int TLS_TEST_PORT = 443;

    @SuppressWarnings("deprecation")
    public static SslConfiguration createTestSslConfigurationResourcesDeprecated() throws StoreConfigurationException {
        final KeyStoreConfiguration ksc = new KeyStoreConfiguration(
                TestConstants.KEYSTORE_FILE_RESOURCE, TestConstants.KEYSTORE_PWD(), TestConstants.KEYSTORE_TYPE, null);
        final TrustStoreConfiguration tsc = new TrustStoreConfiguration(
                TestConstants.TRUSTSTORE_FILE_RESOURCE, TestConstants.TRUSTSTORE_PWD(), null, null);
        return SslConfiguration.createSSLConfiguration(null, ksc, tsc);
    }

    public static SslConfiguration createTestSslConfigurationResources() throws StoreConfigurationException {
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
        final KeyStoreConfiguration ksc = new KeyStoreConfiguration(
                TestConstants.KEYSTORE_FILE_RESOURCE,
                new MemoryPasswordProvider(TestConstants.KEYSTORE_PWD()),
                TestConstants.KEYSTORE_TYPE,
                null);
        final TrustStoreConfiguration tsc = new TrustStoreConfiguration(
                TestConstants.TRUSTSTORE_FILE_RESOURCE,
                new MemoryPasswordProvider(TestConstants.TRUSTSTORE_PWD()),
                null,
                null);
        return SslConfiguration.createSSLConfiguration(null, ksc, tsc);
    }

<<<<<<< HEAD
    private static SslConfiguration createTestSslConfigurationFiles() throws StoreConfigurationException {
=======
    @SuppressWarnings("deprecation")
    public static SslConfiguration createTestSslConfigurationFilesDeprecated() throws StoreConfigurationException {
        final KeyStoreConfiguration ksc = new KeyStoreConfiguration(
                TestConstants.KEYSTORE_FILE, TestConstants.KEYSTORE_PWD(), TestConstants.KEYSTORE_TYPE, null);
        final TrustStoreConfiguration tsc =
                new TrustStoreConfiguration(TestConstants.TRUSTSTORE_FILE, TestConstants.TRUSTSTORE_PWD(), null, null);
        return SslConfiguration.createSSLConfiguration(null, ksc, tsc);
    }

    public static SslConfiguration createTestSslConfigurationFiles() throws StoreConfigurationException {
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
        final KeyStoreConfiguration ksc = new KeyStoreConfiguration(
                TestConstants.KEYSTORE_FILE,
                new MemoryPasswordProvider(TestConstants.KEYSTORE_PWD()),
                TestConstants.KEYSTORE_TYPE,
                null);
        final TrustStoreConfiguration tsc = new TrustStoreConfiguration(
                TestConstants.TRUSTSTORE_FILE, new MemoryPasswordProvider(TestConstants.TRUSTSTORE_PWD()), null, null);
        return SslConfiguration.createSSLConfiguration(null, ksc, tsc);
    }

    @Test
<<<<<<< HEAD
    void testGettersFromScratchFiles() throws StoreConfigurationException {
=======
    public void testGettersFromScratchFiles() throws StoreConfigurationException {
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
        assertNotNull(createTestSslConfigurationFiles().getProtocol());
        assertNotNull(createTestSslConfigurationFiles().getKeyStoreConfig());
        assertNotNull(createTestSslConfigurationFiles().getSslContext());
        assertNotNull(createTestSslConfigurationFiles().getSslSocketFactory());
        assertNotNull(createTestSslConfigurationFiles().getTrustStoreConfig());
    }

    @Test
<<<<<<< HEAD
    void testGettersFromScratchResources() throws StoreConfigurationException {
=======
    public void testGettersFromScratchResources() throws StoreConfigurationException {
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
        assertNotNull(createTestSslConfigurationResources().getProtocol());
        assertNotNull(createTestSslConfigurationResources().getKeyStoreConfig());
        assertNotNull(createTestSslConfigurationResources().getSslContext());
        assertNotNull(createTestSslConfigurationResources().getSslSocketFactory());
        assertNotNull(createTestSslConfigurationResources().getTrustStoreConfig());
    }

    @Test
<<<<<<< HEAD
    void testEquals() {
=======
    public void equals() {
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
        assertEquals(
                SslConfiguration.createSSLConfiguration(null, null, null),
                SslConfiguration.createSSLConfiguration(null, null, null));
    }

    @Test
<<<<<<< HEAD
    void emptyConfigurationDoesNotCauseNullSSLSocketFactory() {
=======
    public void emptyConfigurationDoesntCauseNullSSLSocketFactory() {
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
        final SslConfiguration sc = SslConfiguration.createSSLConfiguration(null, null, null);
        final SSLSocketFactory factory = sc.getSslSocketFactory();
        assertNotNull(factory);
    }

    @Test
<<<<<<< HEAD
    void emptyConfigurationHasDefaultTrustStore() throws IOException {
=======
    public void emptyConfigurationHasDefaultTrustStore() throws IOException {
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
        final SslConfiguration sc = SslConfiguration.createSSLConfiguration(null, null, null);
        final SSLSocketFactory factory = sc.getSslSocketFactory();
        try {
            try (final SSLSocket clientSocket = (SSLSocket) factory.createSocket(TLS_TEST_HOST, TLS_TEST_PORT)) {
                assertNotNull(clientSocket);
            }
        } catch (final UnknownHostException offline) {
            // this exception is thrown on Windows when offline
        }
    }

    @Test
<<<<<<< HEAD
    void connectionFailsWithoutValidServerCertificate() throws IOException, StoreConfigurationException {
=======
    public void connectionFailsWithoutValidServerCertificate() throws IOException, StoreConfigurationException {
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
        final TrustStoreConfiguration tsc = new TrustStoreConfiguration(
                TestConstants.TRUSTSTORE_FILE, new MemoryPasswordProvider(TestConstants.NULL_PWD), null, null);
        final SslConfiguration sc = SslConfiguration.createSSLConfiguration(null, null, tsc);
        final SSLSocketFactory factory = sc.getSslSocketFactory();
        try {
            try (final SSLSocket clientSocket = (SSLSocket) factory.createSocket(TLS_TEST_HOST, TLS_TEST_PORT)) {
                try (final OutputStream os = clientSocket.getOutputStream()) {
                    assertThrows(IOException.class, () -> os.write("GET config/login_verify2?".getBytes()));
                }
            }
        } catch (final UnknownHostException offline) {
            // this exception is thrown on Windows when offline
        }
    }

    @Test
<<<<<<< HEAD
    void loadKeyStoreWithoutPassword() throws StoreConfigurationException {
=======
    public void loadKeyStoreWithoutPassword() throws StoreConfigurationException {
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
        final KeyStoreConfiguration ksc = new KeyStoreConfiguration(
                TestConstants.KEYSTORE_FILE, new MemoryPasswordProvider(TestConstants.NULL_PWD), null, null);
        final SslConfiguration sslConf = SslConfiguration.createSSLConfiguration(null, ksc, null);
        final SSLSocketFactory factory = sslConf.getSslSocketFactory();
        assertNotNull(factory);
    }
}
