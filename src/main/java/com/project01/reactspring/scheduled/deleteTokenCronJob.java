package com.project01.reactspring.scheduled;

import com.project01.reactspring.respository.InvalidateTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class deleteTokenCronJob {

    @Autowired
    private InvalidateTokenRepository invalidateTokenRepository;

    @Scheduled(fixedDelay = 1800000,initialDelay = 5000)
    public void scheduleFixedDelayTask() {
        invalidateTokenRepository.deleteExpiryTime();
        System.out.println("Delete token expiry time Success!");
    }
}
