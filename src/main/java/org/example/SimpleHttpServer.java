package org.example;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class SimpleHttpServer {
    public static void main(String[] args) throws IOException {
        // Create an HTTP server on port 8080
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        // Define the "/hello" context (endpoint)
        server.createContext("/hello", new HelloHandler());

        // Start the server
        server.setExecutor(null); // Default executor
        server.start();
        System.out.println("Server is running on http://localhost:8080/hello?name=YourName");
    }

    // Define a handler to process HTTP requests
    static class HelloHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            // Extract the "name" parameter from the query string
            String query = exchange.getRequestURI().getQuery();
            String name = "World"; // Default value if no name is provided

            if (query != null && query.startsWith("name=")) {
                name = query.split("=")[1];
            }

            // Create the response message
            String response = "Hello " + name;

            // Send the response back
            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
}
