package com.hospital.service;

import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Service to manage blacklisted JWT tokens.
 * In a production environment, this should use Redis or a Database with TTL
 * (Time To Live).
 * For this lab, we use an in-memory ConcurrentHashMap.Sa
 */
@Service
public class TokenBlacklistService {

    // Using ConcurrentHashMap.newKeySet() to check for thread safety
    private final Set<String> blacklistedTokens = ConcurrentHashMap.newKeySet();

    /**
     * Adds a token to the blacklist.
     * 
     * @param token The JWT token to invalidate.
     */
    public void blacklistToken(String token) {
        blacklistedTokens.add(token);
    }

    /**
     * Checks if a token is blacklisted.
     * 
     * @param token The JWT token to check.
     * @return true if the token is blacklisted, false otherwise.
     */
    public boolean isBlacklisted(String token) {
        return blacklistedTokens.contains(token);
    }
}
