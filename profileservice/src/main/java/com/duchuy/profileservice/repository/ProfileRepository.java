package com.duchuy.profileservice.repository;

import com.duchuy.profileservice.data.Profile;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface ProfileRepository extends ReactiveCrudRepository<Profile,Long> {
}
