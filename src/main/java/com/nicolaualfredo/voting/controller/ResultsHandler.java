/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nicolaualfredo.voting.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nicolaualfredo.voting.model.Candidate;
import com.nicolaualfredo.voting.model.Vote;
import com.nicolaualfredo.voting.repository.CandidateRepository;
import com.nicolaualfredo.voting.repository.VoteRepository;
import com.nicolaualfredo.voting.service.CorsUtil;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets; 
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author nicolaualfredo
 */
public class ResultsHandler implements HttpHandler {

    private final CandidateRepository candidateRepo = new CandidateRepository();
    private final VoteRepository voteRepo = new VoteRepository();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        CorsUtil.enableCors(exchange);

        if (exchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
            exchange.sendResponseHeaders(204, -1);
            return;
        }

        if (!exchange.getRequestMethod().equalsIgnoreCase("GET")) {
            sendResponse(exchange, 405, "{\"error\": \"Method not allowed\"}");
            return;
        }

        // Resultado será um Map de nome do candidato -> total de votos
        Map<String, Integer> results = new LinkedHashMap<>();

        List<Candidate> candidates = candidateRepo.findAll();
        List<Vote> votes = voteRepo.findAll();

        // Inicializa todos com 0
        for (Candidate candidate : candidates) {
            results.put(candidate.getName(), 0);
        }

        for (Vote vote : votes) {
            Candidate votedCandidate = candidateRepo.findById(vote.getCandidateId());
            if (votedCandidate != null) {
                String name = votedCandidate.getName();
                results.put(name, results.get(name) + 1);
            }
        }

        String json = mapper.writeValueAsString(results);
        sendResponse(exchange, 200, json);
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
