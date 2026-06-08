package com.oxiane.formation.quarkus.essentiel.reporting;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;

@ApplicationScoped
public class GameActionListener {

    private static final Logger logger = LoggerFactory.getLogger(GameActionListener.class);

    @Incoming("creation")
    public void gameCreated(GameDTO gameDTO) {
        logger.info("Game Created : {}", gameDTO);
    }

    @Incoming("proposal")
    public void proposalReceived(ProposalDTO proposalDTO) {
        logger.info("Proposal received : {}", proposalDTO);
    }

    public record GameDTO(String id, int secret, Instant start) {
    }

    public record ProposalDTO(String gameId, Instant instant, int proposal, String response) {
    }
}
