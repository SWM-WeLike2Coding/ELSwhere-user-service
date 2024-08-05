package com.wl2c.elswhereuserservice.domain.user.service;

import com.wl2c.elswhereuserservice.domain.user.exception.UserNotFoundException;
import com.wl2c.elswhereuserservice.domain.user.model.dto.request.RequestCreateInvestmentPropensityDto;
import com.wl2c.elswhereuserservice.domain.user.model.entity.InvestmentPropensity;
import com.wl2c.elswhereuserservice.domain.user.model.entity.User;
import com.wl2c.elswhereuserservice.domain.user.repository.UserRepository;
import com.wl2c.elswhereuserservice.domain.user.repository.UserInvestmentPropensityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class UserInvestmentPropensityService {

    private final UserInvestmentPropensityRepository userInvestmentPropensityRepository;
    private final UserRepository userRepository;

    @Transactional
    public void create(Long userId, RequestCreateInvestmentPropensityDto dto) {
        if (userInvestmentPropensityRepository.findByUserId(userId).isPresent()) {
            InvestmentPropensity investmentPropensity = userInvestmentPropensityRepository.findByUserId(userId).get();

            investmentPropensity.changeInvestmentExperience(dto.getInvestmentExperience());
            investmentPropensity.changeInvestmentPreferredPeriod(dto.getInvestmentPreferredPeriod());
            investmentPropensity.changeRiskTakingAbility(dto.getRiskTakingAbility());

        } else {
            User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

            InvestmentPropensity investmentPropensity = InvestmentPropensity.builder()
                    .user(user)
                    .investmentExperience(dto.getInvestmentExperience())
                    .investmentPreferredPeriod(dto.getInvestmentPreferredPeriod())
                    .riskTakingAbility(dto.getRiskTakingAbility())
                    .build();
            userInvestmentPropensityRepository.save(investmentPropensity);

        }
    }
}
