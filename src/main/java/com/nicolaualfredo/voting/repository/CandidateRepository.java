/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nicolaualfredo.voting.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nicolaualfredo.voting.model.Candidate;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author nicolaualfredo
 */
public class CandidateRepository {

    private final File file = new File("data/candidates.json");
    private final ObjectMapper mapper = new ObjectMapper();
    private List<Candidate> candidates;

    public CandidateRepository() {
        load();
    }

    private void load() {
        if (!file.exists()) {
            candidates = new ArrayList<>();
            save();
            return;
        }
        try {
            candidates = mapper.readValue(file, new TypeReference<List<Candidate>>() {
            });
        } catch (IOException e) {
            candidates = new ArrayList<>();
        }
    }

    private void save() {
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, candidates);
        } catch (IOException e) {
        }
    }

    public List<Candidate> findAll() {
        return new ArrayList<>(candidates);
    }

    public void addCandidate(Candidate candidate) {
        candidates.add(candidate);
        save();
    }

    public Candidate findById(int id) {
        return candidates.stream()
                .filter(c -> c.getId() == id)
                .findFirst()
                .orElse(null);
    }
}
