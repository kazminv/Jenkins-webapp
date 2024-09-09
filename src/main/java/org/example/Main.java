package org.example;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class Main {
    public static void main(String[] args) throws IOException {
        // Устанавливаем порт 3000 для веб-сервера
        int port = 3000;

        // Создаем HTTP-сервер на указанном порту
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

        // Создаем обработчик для главной страницы "/"
        server.createContext("/", new HttpHandler() {
            @Override
            public void handle(com.sun.net.httpserver.HttpExchange exchange) throws IOException {
                String response = "Hello, World! Welcome to my Simple Web Server.";
                exchange.sendResponseHeaders(200, response.length());
                OutputStream outputStream = exchange.getResponseBody();
                outputStream.write(response.getBytes());
                outputStream.close();
            }
        });

        // Начинаем прослушивание входящих соединений
        server.start();

        System.out.println("Server started on port " + port);
    }
}
