package io.github.cosminseceleanu.tutorials.sampleapp.user.tasks;

import co.elastic.apm.api.CaptureSpan;
import co.elastic.apm.api.CaptureTransaction;
import io.github.cosminseceleanu.tutorials.sampleapp.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.Random;

@Service
@Slf4j
public class PrintUsersTask {

    private UserService userRepository;

    @Autowired
    public PrintUsersTask(UserService userRepository) {
        this.userRepository = userRepository;
    }

    @Scheduled(fixedDelayString = "5000")
    public void execute() {
        log.info("run scheduled UserService test");
        doExecute();
    }

    @CaptureTransaction(type = "Task", value = "PrintWebFluxUsers")
    private void doExecute() {
        userRepository.all()
                .flatMap(u-> this.check(u.getName()))
                .subscribe(item->log.info("user name:{}",item));


        sleep();
    }

    @SuppressWarnings("checkstyle:MagicNumber")
    @CaptureSpan("someCustomOperation")
    private void sleep() {
        try {
            Random random = new Random();
            int milis = random.nextInt(120 - 20 + 1) + 20;
            Thread.sleep(milis);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @CaptureSpan("someHeavyWork")
    private Mono<List<Integer>> heavyDelay() {
        return Flux.range(1,10).delayElements(Duration.ofSeconds(1)).collectList();
    }

    @CaptureSpan("convertToString")
    private Mono<List<String>> check(String name) {
        return Flux.range(1,2).delayElements(Duration.ofSeconds(1))
                .flatMap(x->Mono.just("checked: "+x))
                .collectList();
    }
}
