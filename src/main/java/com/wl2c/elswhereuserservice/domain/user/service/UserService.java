package com.wl2c.elswhereuserservice.domain.user.service;

import com.wl2c.elswhereuserservice.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    public String createRandomNickname() {
        List<String> adjectives = new ArrayList<>();
        List<String> investors = new ArrayList<>();

        // 파일에서 형용사들을 읽어와 리스트에 저장
        try (InputStream inputStream = getClass().getResourceAsStream("/adjectives.txt")) {
            assert inputStream != null;
            try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
                String line;
                while ((line = br.readLine()) != null) {
                    adjectives.add(line.trim());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 파일에서 유명인들을 읽어와 리스트에 저장
        try (InputStream inputStream = getClass().getResourceAsStream("/investors.txt")) {
            assert inputStream != null;
            try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
                String line;
                while ((line = br.readLine()) != null) {
                    investors.add(line.trim());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 랜덤 객체 생성
        Random random = new Random();

        String result;
        while(true) {
            String randomAdjective = adjectives.get(random.nextInt(adjectives.size()));
            String randomInvestor = investors.get(random.nextInt(investors.size()));

            result = randomAdjective + " " + randomInvestor + (random.nextInt(9999) + 1);

            if (userRepository.findByNickname(result).isEmpty()) break;
        }

        return result;
    }
}
