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
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
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
        if (!exchange.getRequestMethod().equalsIgnoreCase("GET")) {
            sendResponse(exchange, 405, "{\"error\": \"Method not allowed\"}");
            return;
        }

        List<Candidate> candidates = candidateRepo.findAll();
        List<Vote> votes = voteRepo.findAll();

        Map<String, Integer> results = new HashMap<>();
        for (Candidate c : candidates) {
            results.put(c.getName(), 0);
        }

        for (Vote vote : votes) {
            Candidate candidate = candidateRepo.findById(vote.getCandidateId());
            if (candidate != null) {
                results.put(candidate.getName(), results.get(candidate.getName()) + 1);
            }
        }

        String response = mapper.writeValueAsString(results);
        sendResponse(exchange, 200, response);
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