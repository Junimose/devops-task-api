package com.mycompany.app;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.*;

public class App {
    private static List<String> tasks = new ArrayList<>(Arrays.asList("Deploy to Minikube", "Learn Kubernetes"));

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        
        server.createContext("/health", exchange -> {
            String response = "{\"status\":\"UP\", \"tasks_count\":" + tasks.size() + "}";
            send(exchange, 200, response);
        });
        
        server.createContext("/tasks", exchange -> {
            if ("GET".equals(exchange.getRequestMethod())) {
                send(exchange, 200, tasks.toString());
            } else {
                send(exchange, 405, "Method not allowed");
            }
        });
        
        server.createContext("/", exchange -> {
            send(exchange, 200, "Task API Running. Try /health and /tasks");
        });
        
        server.setExecutor(null);
        server.start();
        System.out.println("Server started on http://localhost:8080 - Health: /health Tasks: /tasks");
    }

    private static void send(HttpExchange exchange, int code, String body) throws IOException {
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(code, body.length());
        OutputStream os = exchange.getResponseBody();
        os.write(body.getBytes());
        os.close();
    }

    public String getMessage() { return "Task API Running"; }
}
