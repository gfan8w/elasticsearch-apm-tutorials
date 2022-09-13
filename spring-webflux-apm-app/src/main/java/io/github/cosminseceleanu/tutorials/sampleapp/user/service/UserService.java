package io.github.cosminseceleanu.tutorials.sampleapp.user.service;

import co.elastic.apm.api.CaptureSpan;
import co.elastic.apm.api.CaptureTransaction;
import io.github.cosminseceleanu.tutorials.sampleapp.user.exception.UserNotFoundException;
import io.github.cosminseceleanu.tutorials.sampleapp.user.model.User;
import io.github.cosminseceleanu.tutorials.sampleapp.user.repository.UserRepository;

import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@Component
public class UserService {

    private final UserRepository userRepository;

    @CaptureTransaction(type = "UserService", value = "get")
    public Mono<User> get(String id) {
        return userRepository.findById(id)
                .switchIfEmpty(Mono.error(new UserNotFoundException()))
                .delayElement(Duration.ofSeconds(random(1,5)));
    }

    @CaptureTransaction(type = "UserService", value = "create")
    public Mono<User> create(User user) {
        return userRepository.save(user.toBuilder().id(UUID.randomUUID().toString()).build())
                .delayElement(Duration.ofSeconds(random(1,10)));
    }

    @CaptureTransaction(type = "UserService", value = "all")
    public Flux<User> all(){
        return userRepository.findAll().delayElements(Duration.ofSeconds(random(1,10)));
    }

    @CaptureSpan("random")
    public int random(int min, int max) {

        return new Random().nextInt(max - min) + min;
    }
}
