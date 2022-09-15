package com.cosmin.tutorials.apm.tasks;

import co.elastic.apm.api.CaptureSpan;
import co.elastic.apm.api.CaptureTransaction;
import com.cosmin.tutorials.apm.database.User;
import com.cosmin.tutorials.apm.database.UserRepository;
import java.util.Random;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PrintUsersTask {

    private UserRepository userRepository;

    @Autowired
    public PrintUsersTask(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Scheduled(fixedDelayString = "5000")
    public void execute() {
        log.info("run scheduled test");
        doExecute();
    }

    @CaptureTransaction(type = "Task", value = "PrintUsers")
    private void doExecute() {
        var list =userRepository.findAll();
        for (User user : list) {
            convert(user);
        }
        list.forEach(user -> log.debug(user.getEmail()));
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

    @CaptureTransaction("convert")
    private User convert(User user) {
        user.setEmail(user.getEmail().toLowerCase());
        user.setName(user.getName().toUpperCase());
        sleep();
        return user;
    }
}
