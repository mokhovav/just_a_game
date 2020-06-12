package com.mokhovav.justAGame.mongodb;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableMongoRepositories
public interface GameSessionRepository extends MongoRepository<GameSession, String> {
    public GameSession findById(long id);
}
