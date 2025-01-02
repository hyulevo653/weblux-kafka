package com.duchuy.profileservice.service;


import com.duchuy.profileservice.model.ProfileDTO;
import com.duchuy.profileservice.repository.ProfileRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j //giup ghi log
public class ProfileService {
    @Autowired
    ProfileRepository profileRepository;

    public Flux<ProfileDTO> getAllProfile(){
        return profileRepository.findAll() // trả về 1 flux<Profile>
                                    .map(ProfileDTO::entityToDto)
                .switchIfEmpty(Mono.error(new Exception("Profile list empty!")));
    }

    public Mono<Boolean> checkDuplicate(String email){
        return profileRepository.findByEmail(email)
                .flatMap(profile -> Mono.just(true)) //Mono<true>
                .switchIfEmpty(Mono.just(false));
    }

}
