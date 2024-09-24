package org.example;

import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SimpleHttpServerTest {

    private static HttpServer server;

    @BeforeAll
    public static void startServer() throws Exception {
        // Start the SimpleHttpServer before running tests
        Thread serverThread = new Thread(() -> {
            try {
                SimpleHttpServer.main(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        serverThread.start();
        // Give the server some time to start up
        Thread.sleep(1000);
    }

    @AfterAll
    public static void stopServer() {
        // Stop the server after all tests
        if (server != null) {
            server.stop(0);
            System.out.println("Server stopped.");
        }
    }

    @Test
    public void testHelloWithName() throws Exception {
        // Test with specific name "John"
        String name = "John";
        URL url = new URL("http://localhost:8080/hello?name=" + name);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        assertEquals(200, responseCode);

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }

        in.close();
        connection.disconnect();

        assertEquals("Hello " + name, content.toString());
    }
}
