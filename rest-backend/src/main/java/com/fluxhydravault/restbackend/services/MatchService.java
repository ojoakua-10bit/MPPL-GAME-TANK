package com.fluxhydravault.restbackend.services;

import com.fluxhydravault.restbackend.model.Match;

import java.util.List;

public interface MatchService {
    Match newMatch(String playerID, boolean matchStatus, int score, int totalDamage, int goldGained, String itemGained);

    Match getMatch(String matchID);

    List<Match> getUserMatches(String playerID);

    List<Match> getUserMatches(String playerID, int start, int limit);

    Integer getUserMatchesCount(String playerID);
}
