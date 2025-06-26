/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nicolaualfredo.voting.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nicolaualfredo.voting.model.Vote;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author nicolaualfredo
 */
public class VoteRepository {

    private final File file = new File("data/votes.json");
    private final ObjectMapper mapper = new ObjectMapper();
    private List<Vote> votes;

    public VoteRepository() {
        load();
    }

    private void load() {
        if (!file.exists()) {
            votes = new ArrayList<>();
            save();
            return;
        }
        try {
            votes = mapper.readValue(file, new TypeReference<List<Vote>>() {
            });
        } catch (IOException e) {
            votes = new ArrayList<>();
        }
    }

    private void save() {
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, votes);
        } catch (IOException e) {
        }
    }

    public List<Vote> findAll() {
        loadVotes();
        return votes;
    }

    public boolean hasVoted(String voterId) {
        return votes.stream().anyMatch(v -> v.getVoterId().equals(voterId));
    }

    public void addVote(Vote vote) {
        votes.add(vote);
        save();
    }

    private void loadVotes() {
        try {
            File file = new File("data/votes.json");
            if (file.exists()) {
                votes = mapper.readValue(file, new TypeReference<List<Vote>>() {
                });
            } else {
                votes = new ArrayList<>();
            }
        } catch (IOException e) {
            votes = new ArrayList<>();
            System.err.println("Failed to load votes: " + e.getMessage());
        }
    }
}
