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
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 *
 * @author nicolaualfredo
 */
public class VoteHandler implements HttpHandler {

    private final VoteRepository voteRepo = new VoteRepository();
    private final CandidateRepository candidateRepo = new CandidateRepository();
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

        if (!exchange.getRequestMethod().equalsIgnoreCase("POST")) {
            sendResponse(exchange, 405, "{\"error\": \"Method not allowed\"}");
            return;
        }

        InputStream body = exchange.getRequestBody();
        Vote vote = mapper.readValue(body, Vote.class);

        if (voteRepo.hasVoted(vote.getVoterId())) {
            sendResponse(exchange, 403, "{\"error\": \"This voter has already voted\"}");
            return;
        }

        Candidate candidate = candidateRepo.findById(vote.getCandidateId());
        if (candidate == null) {
            sendResponse(exchange, 404, "{\"error\": \"Candidate not found\"}");
            return;
        }

        voteRepo.addVote(vote);
        sendResponse(exchange, 200, "{\"message\": \"Vote registered successfully\"}");
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
