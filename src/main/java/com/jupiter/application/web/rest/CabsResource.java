package com.jupiter.application.web.rest;

import com.jupiter.application.service.CabService;
import com.jupiter.application.web.rest.dto.CabDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
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
    public ResponseEntity<?> createOrUpdateCabLocation( CabDTO cabDTO){

        //cabService.createCab(cab_id, latitude, longitude);
        log.debug(" Requested to create new Cab");
        return new ResponseEntity<>(HttpStatus.OK);

    }

    @RequestMapping(value = "/cabs/{cab_id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CabDTO> getCabDetails(@PathVariable float cab_id) {

        log.debug(" Requested cab details");
        CabDTO cabDTO = new CabDTO();
        cabDTO.setId(12345L);
        cabDTO.setLongitude(33.23F);
        cabDTO.setLatitude(34.23F);
        return new ResponseEntity<CabDTO>(cabDTO, HttpStatus.OK);

    }

    @RequestMapping(value = "/cabs",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CabDTO> search(@RequestParam float latitude,
                                         @RequestParam float longitude,
                                         @RequestParam (required = false, defaultValue = "8") int radius,
                                         @RequestParam (required = false) float limit) {

        log.debug(" Search for nearest cab details");
        return new ResponseEntity<CabDTO>(new CabDTO(), HttpStatus.OK);

    }

    /**
     * DELETE  /cabs/(cab_id) -> delete the cab.
     */
    @RequestMapping(value = "/cabs/{cab_id}",
            method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteCab(@PathVariable float cab_id){
        log.debug(" Requested to delete a cab");
        return new ResponseEntity<>(HttpStatus.OK);

    }


}
