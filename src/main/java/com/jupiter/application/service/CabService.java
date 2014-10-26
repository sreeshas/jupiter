package com.jupiter.application.service;

import com.jupiter.application.domain.*;
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

    /**
     * Creates cab if it does not exist. if it exists, updates it.
     * @param id
     * @param latitude
     * @param longitude
     * @return
     */
    public Cab createCab(long id, float latitude, float longitude) {

        //check if the cab exists. if not create it.
        if (!cabRepository.exists(id)) {

            Cab newCab = new Cab();
            newCab.setId(id);
            newCab.setLatitude(latitude);
            newCab.setLongitude(longitude);
            newCab.setLocation(new Location(latitude, longitude));

            log.debug("Creating new cab with id " + newCab.getId());
            return cabRepository.save(newCab);
        }
        //Cab already exists. Update it.
        Cab existingCab = cabRepository.findOne(id);
        existingCab.setLatitude(latitude);
        existingCab.setLongitude(longitude);
        log.debug("Updating cab with id " + existingCab.getId());
        return cabRepository.save(existingCab);
    }

    /**
     * returns false if cab not found, else returns true.
     * @param id of the cab to be deleted.
     */
    public boolean deleteCab(long id) {

        if (cabRepository.exists(id)) {
            log.debug("Deleting Cab with id "+id );
            cabRepository.delete(id);
            return true;
        }
        log.debug("Cannot Delete. Cab "+id+" not found");
        return false;
    }

    public Cab getCab(long id) {
        if (!cabRepository.exists(id)) {
            return null;
        }
        return cabRepository.findById(id);
    }

    public List<Cab> search(float latitude, float longitude, int radius, int limit) {
        //TODO: figure out how to use limit
        // figure out a way to get distance metrics.
        List<Cab> cabList = cabRepository.search(latitude, longitude, radius);
        return cabList;
    }

}
