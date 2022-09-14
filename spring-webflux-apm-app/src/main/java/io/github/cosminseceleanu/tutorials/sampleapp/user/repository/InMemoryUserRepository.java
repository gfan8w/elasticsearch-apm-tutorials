package io.github.cosminseceleanu.tutorials.sampleapp.user.repository;

import co.elastic.apm.api.CaptureTransaction;
import co.elastic.apm.api.ElasticApm;
import co.elastic.apm.api.Transaction;
import io.github.cosminseceleanu.tutorials.sampleapp.user.model.User;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class InMemoryUserRepository implements UserRepository {
    private static final Map<String, User> USERS = new ConcurrentHashMap<>();

    @Override
    @CaptureTransaction(type = "InMemoryUserRepository", value = "findById")
    public Mono<User> findById(String id) {
        return Mono.justOrEmpty(USERS.get(id));
    }

    @Override
    @CaptureTransaction(type = "InMemoryUserRepository", value = "save")
    public Mono<User> save(User user) {
        if (user.getId() == null) {
            return Mono.error(new IllegalArgumentException("User id can not be null"));
        }
        return Mono.fromSupplier(() -> {
            USERS.put(user.getId(), user);
            return user;
        });
    }

    @Override
    @CaptureTransaction(type = "InMemoryUserRepository", value = "findAll")
    public Flux<User> findAll() {
        return Flux.fromIterable(USERS.values());
//        return Mono.fromCallable(this::start).flatMapMany(tx->{
//            return Flux.fromIterable(USERS.values())
//                    .doOnError(x->{
//                        tx.captureException(x);
//                        log.info("log by user code: a customised Transaction catch an error");
//                    })
//                    .doFinally(f->{
//                        tx.end();
//                        log.info("log by user code: a customised Transaction End");
//                    });
//        });
    }

    private Transaction start(){
        Transaction transaction = ElasticApm.startTransaction();
        transaction.setName("UserServiceCodeTrx#findAll");
        transaction.setType(Transaction.TYPE_REQUEST);
        log.info("log by user code: a customised Transaction start");
        return transaction;
    }
}
