package io.github.cosminseceleanu.tutorials.sampleapp.user.service;

import co.elastic.apm.api.CaptureSpan;
import co.elastic.apm.api.CaptureTransaction;
import io.github.cosminseceleanu.tutorials.sampleapp.user.client.Weather;
import io.github.cosminseceleanu.tutorials.sampleapp.user.exception.UserNotFoundException;
import io.github.cosminseceleanu.tutorials.sampleapp.user.model.User;
import io.github.cosminseceleanu.tutorials.sampleapp.user.repository.UserRepository;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@Component
@Slf4j
public class UserService {

    @Autowired
    private Weather weather;

    private final UserRepository userRepository;

    @CaptureTransaction(type = "UserService", value = "get")
    public Mono<User> get(String id) {
        var dbmono= userRepository.findById(id)
                .switchIfEmpty(Mono.error(new UserNotFoundException()))
                .delayElement(Duration.ofSeconds(random(1,5)));

        return weather.getCityWeather().doOnNext(we->log.info("{}", Objects.toString(we))).then(
            Mono.defer(()->dbmono)
        );
    }

    @CaptureTransaction(type = "UserService", value = "create")
    public Mono<User> create(User user) {
        return userRepository.save(user.toBuilder().id(UUID.randomUUID().toString()).build())
                .delayElement(Duration.ofSeconds(random(1,2)));
    }

    @CaptureTransaction(type = "UserService", value = "all")
    public Flux<User> all(){
        return userRepository.findAll().flatMap(x->transfer(x)).delayElements(Duration.ofMillis(random(100,300)));
    }

    @CaptureSpan("transfer")
    public Mono<User> transfer(User user) {
        user.setName(user.getName().toUpperCase());
        user.setEmail(user.getEmail().toLowerCase());
        return Mono.just(user).delayElement(Duration.ofSeconds(1));
    }

    @CaptureSpan("random")
    public int random(int min, int max) {

        return new Random().nextInt(max - min) + min;
    }
}
