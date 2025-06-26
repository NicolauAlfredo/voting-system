/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nicolaualfredo.voting.server;

import com.nicolaualfredo.voting.controller.CandidateHandler;
import com.nicolaualfredo.voting.controller.ResultsHandler;
import com.nicolaualfredo.voting.controller.VoteHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;

/**
 *
 * @author nicolaualfredo
 */
public class Server {

    public void start() {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);

            server.createContext("/candidates", new CandidateHandler());
            server.createContext("/vote", new VoteHandler());
            server.createContext("/results", new ResultsHandler()); 

            server.setExecutor(null);
            server.start();
            System.out.println("🟢 Server running at http://localhost:8000");
        } catch (IOException e) {
        }
    }
}
