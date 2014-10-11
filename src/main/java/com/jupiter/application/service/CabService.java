package com.jupiter.application.service;

import com.jupiter.application.domain.Authority;
import com.jupiter.application.domain.Cab;
import com.jupiter.application.domain.PersistentToken;
import com.jupiter.application.domain.User;
import com.jupiter.application.repository.AuthorityRepository;
import com.jupiter.application.repository.CabRepository;
import com.jupiter.application.repository.PersistentTokenRepository;
import com.jupiter.application.repository.UserRepository;
import com.jupiter.application.security.SecurityUtils;
import com.jupiter.application.service.util.RandomUtil;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * A service class for managing cabs.
 */
@Service
public class CabService {

    private final Logger log = LoggerFactory.getLogger(CabService.class);

    @Inject
    private CabRepository cabRepository;

    public Cab createCab(long id, float latitude, float longitude) {
        Cab newCab = new Cab();
        newCab.setId(id);
        newCab.setLatitude(latitude);
        newCab.setLongitude(longitude);
        cabRepository.save(newCab);
        log.debug("Created Information for Cab: {}", newCab);
        return newCab;
    }

    public void updateCab(long id, float latitude, float longitude) {

        Cab existingCab = cabRepository.findOne(id);
        existingCab.setLatitude(latitude);
        existingCab.setLongitude(longitude);
        cabRepository.save(existingCab);
        log.debug("Changed Information for Cab: {}", existingCab);
    }






}
