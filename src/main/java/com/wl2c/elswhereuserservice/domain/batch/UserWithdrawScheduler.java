package com.wl2c.elswhereuserservice.domain.batch;

import com.wl2c.elswhereuserservice.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@EnableScheduling
@Slf4j
public class UserWithdrawScheduler {

    private final UserRepository userRepository;

    @Value("${app.user.delete-period}")
    private Long deletePeriod;

    @Scheduled(cron = "0 0 * * * *")
    @Transactional
    public void deleteInactiveUser() {
        userRepository.deleteAllByWithdrawnUser(deletePeriod);
    }

}
