package com.mokhovav.justAGame.mongodb;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Component;


import java.util.Optional;

@Component
public interface GameSessionRepository extends MongoRepository<GameSession, String>{
    @Override
    Optional<GameSession> findById(String s);
}
