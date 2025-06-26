/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nicolaualfredo.voting.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nicolaualfredo.voting.model.Candidate;
import com.nicolaualfredo.voting.repository.CandidateRepository;
import com.nicolaualfredo.voting.service.CorsUtil;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 *
 * @author nicolaualfredo
 */
public class CandidateHandler implements HttpHandler {

    private final CandidateRepository repository = new CandidateRepository();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        CorsUtil.enableCors(exchange); // <- adicionado aqui

        String method = exchange.getRequestMethod();

        if (method.equalsIgnoreCase("OPTIONS")) {
            exchange.sendResponseHeaders(204, -1);
            return;
        }

        if (exchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
            CorsUtil.enableCors(exchange);
            exchange.sendResponseHeaders(204, -1);
            return;
        }

        if (method.equalsIgnoreCase("GET")) {
            List<Candidate> candidates = repository.findAll();
            String response = mapper.writeValueAsString(candidates);
            sendResponse(exchange, 200, response);
        } else if (method.equalsIgnoreCase("POST")) {
            InputStream body = exchange.getRequestBody();
            Candidate candidate = mapper.readValue(body, Candidate.class);
            repository.addCandidate(candidate);
            sendResponse(exchange, 201, "{\"message\": \"Candidate added\"}");
        } else {
            sendResponse(exchange, 405, "{\"error\": \"Method not allowed\"}");
        }
    }

    private void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, bytes.length);
        OutputStream os = exchange.getResponseBody();
        os.write(bytes);
        os.close();
    }
}
