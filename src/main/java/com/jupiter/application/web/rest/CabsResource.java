package com.jupiter.application.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.jupiter.application.Application;
import com.jupiter.application.domain.Cab;
import com.jupiter.application.service.CabService;
import com.jupiter.application.web.rest.dto.CabDTO;
import com.jupiter.application.web.rest.dto.CabDTOValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.xml.ws.Response;
import java.util.IllegalFormatException;
import java.util.List;

/**
 * Rest controller for managing cabs.
 */
@RestController
public class CabsResource {

    private final Logger log = LoggerFactory.getLogger(CabsResource.class);

    @Inject
    private CabService cabService;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(new CabDTOValidator());
    }

    /**
     * PUT  /cabs/(cab_id) -> register the user.
     */
    @RequestMapping(value = "/cabs/{cab_id}",
            method = RequestMethod.PUT)
    @Timed
    public ResponseEntity<?> createOrUpdateCabLocation( @PathVariable long cab_id, @Valid @RequestBody CabDTO cabDTO){
        cabService.createCab(cab_id, cabDTO.getLatitude(), cabDTO.getLongitude());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/cabs/{cab_id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Cab> getCabDetails(@Valid @PathVariable long cab_id) {

        log.debug(" Requested cab details");
        Cab cab = cabService.getCab(cab_id);
        return new ResponseEntity<Cab>(cab, HttpStatus.OK);

    }

    @RequestMapping(value = "/cabs",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<CabDTO>> search(@Valid @RequestParam float latitude,
                                         @Valid @RequestParam float longitude,
                                         @Valid @RequestParam (required = false, defaultValue = "8") float radius,
                                         @Valid @RequestParam (required = false) int limit) {

        //TODO: figure out a better way to validate primitive types

        // Validate inputs
        boolean isLatitudeValid = CabDTOValidator.isLatitudeValid(latitude);
        boolean isLongitudeValid = CabDTOValidator.isLongitudeValid(longitude);
        if (!(isLatitudeValid && isLongitudeValid)) {
            throw new IllegalArgumentException();
        }
        List<Cab> searchResults =  cabService.search(latitude, longitude, radius, limit);
        log.debug(" Search for nearest cab details");
        return new ResponseEntity(searchResults, HttpStatus.OK);

    }

    /**
     * DELETE  /cabs/(cab_id) -> delete the cab.
     */
    @RequestMapping(value = "/cabs/{cab_id}",
            method = RequestMethod.DELETE)
    @Timed
    public ResponseEntity<?> deleteCab(@Valid @PathVariable long cab_id){
        log.debug(" Requested to delete a cab");
        boolean status = cabService.deleteCab(cab_id);
        if (status) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }
}
