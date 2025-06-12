package com.jenkov.undertowexamples;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.handlers.resource.FileResourceManager;
import io.undertow.server.handlers.resource.ResourceHandler;
import io.undertow.util.Headers;

public class UndertowHttpServer {

    // In-memory to-do list and ID generator
    private static final List<Todo> todos = Collections.synchronizedList(new ArrayList<>());
    private static final AtomicInteger idGen = new AtomicInteger(1);

    public static void main(String[] args) throws IOException {
        String ipAddress = "0.0.0.0";
        int httpPort = 9090;
        if (args.length >= 1) ipAddress = args[0];

        // REST API for to-dos
        HttpHandler apiHandler = Handlers.routing()
            .get("/api/todos", exchange -> {
                exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
                StringBuilder sb = new StringBuilder("[");
                synchronized (todos) {
                    for (int i = 0; i < todos.size(); i++) {
                        if (i > 0) sb.append(",");
                        sb.append(todos.get(i).toJson());
                    }
                }
                sb.append("]");
                exchange.getResponseSender().send(sb.toString());
            })
            .post("/api/todos", exchange -> {
                exchange.getRequestReceiver().receiveFullString((ex, data) -> {
                    String text = data.replaceAll(".*\"text\"\s*:\s*\"(.*?)\".*", "$1");
                    Todo todo = new Todo(idGen.getAndIncrement(), text, false);
                    todos.add(todo);
                    ex.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
                    ex.getResponseSender().send(todo.toJson());
                });
            })
            .put("/api/todos/{id}", exchange -> {
                int id = Integer.parseInt(exchange.getQueryParameters().get("id").getFirst());
                exchange.getRequestReceiver().receiveFullString((ex, data) -> {
                    boolean completed = data.contains("true");
                    synchronized (todos) {
                        for (Todo t : todos) {
                            if (t.id == id) t.completed = completed;
                        }
                    }
                    ex.setStatusCode(204);
                    ex.endExchange();
                });
            })
            .delete("/api/todos/{id}", exchange -> {
                int id = Integer.parseInt(exchange.getQueryParameters().get("id").getFirst());
                todos.removeIf(t -> t.id == id);
                exchange.setStatusCode(204);
                exchange.endExchange();
            });

        // Serve static files from React build
        String reactBuildPath = "frontend/build";
        ResourceHandler resourceHandler = new ResourceHandler(
            new FileResourceManager(Paths.get(reactBuildPath).toFile(), 100))
            .addWelcomeFiles("index.html");

        // Main handler: /api handled by API, all other routes serve React index.html
        HttpHandler mainHandler = exchange -> {
            String path = exchange.getRequestPath();
            if (path.startsWith("/api/")) {
                apiHandler.handleRequest(exchange);
            } else {
                // Always serve index.html for React Router (client-side routing)
                resourceHandler.handleRequest(exchange);
            }
        };

        Undertow server = Undertow.builder()
            .addHttpListener(httpPort, ipAddress)
            .setHandler(mainHandler)
            .build();

        System.out.println("Undertow To-Do server started on http://" + ipAddress + ":" + httpPort);
        server.start();
    }

    // Simple to-do POJO
    static class Todo {
        int id;
        String text;
        boolean completed;
        Todo(int id, String text, boolean completed) {
            this.id = id;
            this.text = text;
            this.completed = completed;
        }
        String toJson() {
            return String.format("{\"id\":%d,\"text\":\"%s\",\"completed\":%s}", id, text.replace("\"", "\\\""), completed);
        }
    }
}