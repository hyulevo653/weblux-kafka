package com.duchuy.accountservice.repository;

import com.duchuy.accountservice.data.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveCrudRepository<User,String> {
    Mono<User> findFirstByUsername(String username);
}