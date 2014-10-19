package com.jupiter.application.web.rest;

import com.jupiter.application.Application;
import com.jupiter.application.domain.Cab;
import com.jupiter.application.service.CabService;
import com.jupiter.application.web.rest.dto.CabDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.xml.ws.Response;

/**
 * Rest controller for managing cabs.
 */


@RestController
public class CabsResource {

    private final Logger log = LoggerFactory.getLogger(CabsResource.class);

    @Inject
    private CabService cabService;

    /**
     * PUT  /cabs/(cab_id) -> register the user.
     */
    @RequestMapping(value = "/cabs/{cab_id}",
            method = RequestMethod.PUT)
    public ResponseEntity<?> createOrUpdateCabLocation( @PathVariable long cab_id, @Valid @RequestBody CabDTO cabDTO){

        log.debug(" Requested to create new Cab");
        cabService.createCab(cabDTO.getId(), cabDTO.getLatitude(), cabDTO.getLongitude());
        return new ResponseEntity<>(HttpStatus.OK);

    }

    @RequestMapping(value = "/cabs/{cab_id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Cab> getCabDetails(@Valid @PathVariable long cab_id) {

        log.debug(" Requested cab details");
        Cab cab = cabService.getCab(cab_id);
        return new ResponseEntity<Cab>(cab, HttpStatus.OK);

    }

    @RequestMapping(value = "/cabs",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CabDTO> search(@Valid @RequestParam float latitude,
                                         @Valid @RequestParam float longitude,
                                         @Valid @RequestParam (required = false, defaultValue = "8") int radius,
                                         @Valid @RequestParam (required = false) float limit) {

        log.debug(" Search for nearest cab details");
        return new ResponseEntity<CabDTO>(new CabDTO(), HttpStatus.OK);

    }

    /**
     * DELETE  /cabs/(cab_id) -> delete the cab.
     */
    @RequestMapping(value = "/cabs/{cab_id}",
            method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteCab(@Valid @PathVariable long cab_id){
        log.debug(" Requested to delete a cab");
        cabService.deleteCab(cab_id);
        return new ResponseEntity<>(HttpStatus.OK);

    }


}
