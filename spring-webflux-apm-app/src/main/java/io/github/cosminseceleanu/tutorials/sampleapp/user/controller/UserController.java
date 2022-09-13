package io.github.cosminseceleanu.tutorials.sampleapp.user.controller;

import co.elastic.apm.api.CaptureTransaction;
import io.github.cosminseceleanu.tutorials.sampleapp.user.representation.UserRepresentation;
import io.github.cosminseceleanu.tutorials.sampleapp.user.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;


@Slf4j
@RestController
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/users/{id}")
    public Mono<UserRepresentation> get(@PathVariable("id") String id) {
        return userService.get(id).map(UserRepresentation::from);
    }

    @GetMapping("/users")
    @CaptureTransaction(type = "UserController", value = "all")
    public Flux<UserRepresentation> all() {
        return userService.all().flatMap(u->Mono.just(UserRepresentation.from(u)));
    }

    @PostMapping("/users")
    @ResponseStatus(code = HttpStatus.CREATED)
    @CaptureTransaction(type = "UserController", value = "create")
    public Mono<UserRepresentation> create(@RequestBody @Valid UserRepresentation user) {
        return userService.create(user.toModel()).map(UserRepresentation::from).doFinally(x->log.info("saved the user:{}",x));
    }
}
