/*
 * Copyright 2014 EPFL. All rights reserved.
 */
package ch.epfl.sweng.quizapp.test;

import android.support.test.runner.AndroidJUnit4;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import ch.epfl.sweng.quizapp.DefaultNetworkProvider;

import static junit.framework.Assert.assertSame;

/** Tests the DefaultNetworkProvider */
@RunWith(AndroidJUnit4.class)
public class DefaultNetworkProviderTest {
    private DefaultNetworkProvider dnp;

    @Before
    public void setUp() throws Exception {
        dnp = new DefaultNetworkProvider();
    }


    /**
     * Test that getConnection() calls url.openConnection() and does not tamper
     * with it.
     */

    @Test
    public void testOpenConnectionCalled() throws IOException {
        final HttpURLConnection expected = Mockito.mock(HttpURLConnection.class);

        URL url = new URL("http", "example.com", -1, "/", new URLStreamHandler() {

            @Override
            protected URLConnection openConnection(URL u) throws IOException {
                return expected;
            }
        });

        HttpURLConnection result = dnp.getConnection(url);
        assertSame("Wrong URL method called", expected, result);
        Mockito.verifyZeroInteractions(expected);
    }
}
