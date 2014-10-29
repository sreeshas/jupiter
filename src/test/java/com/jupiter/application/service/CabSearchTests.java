package com.jupiter.application.service;

import com.jupiter.application.domain.Cab;
import com.jupiter.application.web.rest.CabsResource;
import com.jupiter.application.web.rest.TestUtil;
import com.jupiter.application.web.rest.dto.CabDTO;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.IOException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by sreenidhisreesha on 10/29/14.
 * High level search test cases.
 */
public class CabSearchTests {

//    private CabService  cabService;
//
//    private MockMvc restCabMockMvc;
//
//
//
//    public void setup() {
//
//        CabsResource cabsResource = new CabsResource();
//        ReflectionTestUtils.setField(cabsResource, "cabService", cabService);
//        this.restCabMockMvc = MockMvcBuilders.standaloneSetup(cabsResource).build();
//    }
//
//
//
//
//    public void testSearchResultsLimit() throws Exception{
//        //37.3971614,-122.0241209 HackerDojo
//        CabDTO cab = new CabDTO();
//        cab.setLongitude(37.3971614f);
//        cab.setLatitude(-122.0241209f);
//
//        restCabMockMvc.perform(put("/cabs/{id}", "1")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(TestUtil.convertObjectToJsonBytes(cab)))
//                .andExpect(status().isOk());
//        // 37.3855004,-122.0188853 Mtn View Cal Train
//        cab = new CabDTO();
//        cab.setLongitude(37.3855004f);
//        cab.setLatitude(-122.0188853f);
//        restCabMockMvc.perform(put("/cabs/{id}", "2")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(TestUtil.convertObjectToJsonBytes(cab)))
//                .andExpect(status().isOk());
//        //Golf Club Moffet Feild
//        //37.3855004,-122.0188853
//        cab = new CabDTO();
//        cab.setLongitude(37.3855004f);
//        cab.setLatitude(-122.0188853f);
//        restCabMockMvc.perform(put("/cabs/{id}", "3")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(TestUtil.convertObjectToJsonBytes(cab)))
//                .andExpect(status().isOk());
//        //Moffett Federal Air field
//        //37.3855004,-122.0188853
//        cab = new CabDTO();
//        cab.setLongitude(37.3855004f);
//        cab.setLatitude(-122.0188853f);
//
//        restCabMockMvc.perform(put("/cabs/{id}", "4")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(TestUtil.convertObjectToJsonBytes(cab)))
//                .andExpect(status().isOk());
//        //El camino Hospital
//        //37.3855004,-122.0188853
//        cab = new CabDTO();
//        cab.setLongitude(37.3855004f);
//        cab.setLatitude(-122.0188853f);
//        cab.setId(Long.parseLong("1"));
//        restCabMockMvc.perform(put("/cabs/{id}", "5")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(TestUtil.convertObjectToJsonBytes(cab)))
//                .andExpect(status().isOk());
//
//        // 37.3855004,-122.0188853
//        // Las Palmas Park
//        cab = new CabDTO();
//        cab.setLongitude(37.3855004f);
//        cab.setLatitude(-122.0188853f);
//        cab.setId(Long.parseLong("1"));
//        restCabMockMvc.perform(put("/cabs/{id}", "6")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(TestUtil.convertObjectToJsonBytes(cab)))
//                .andExpect(status().isOk());
//        //Santa Clara DMV
//        //37.3855004,-122.0188853
//        cab = new CabDTO();
//        cab.setLongitude(37.3855004f);
//        cab.setLatitude(-122.0188853f);
//        cab.setId(Long.parseLong("1"));
//        restCabMockMvc.perform(put("/cabs/{id}", "7")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(TestUtil.convertObjectToJsonBytes(cab)))
//                .andExpect(status().isOk());
//
//        //San Jose Airport
//        //37.3855004,-122.0188853
//        cab = new CabDTO();
//        cab.setLongitude(37.3855004f);
//        cab.setLatitude(-122.0188853f);
//        cab.setId(Long.parseLong("1"));
//        restCabMockMvc.perform(put("/cabs/{id}", "8")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(TestUtil.convertObjectToJsonBytes(cab)))
//                .andExpect(status().isOk());
//        //Mountain View High School
//        //37.3611838,-122.030687
//        cab = new CabDTO();
//        cab.setLongitude(37.3611838f);
//        cab.setLatitude(-122.030687f);
//        restCabMockMvc.perform(put("/cabs/{id}", "9")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(TestUtil.convertObjectToJsonBytes(cab)))
//                .andExpect(status().isOk());
//        //37.325654,-121.94562
//        //West Field Valley Fair
//        cab = new CabDTO();
//        cab.setLongitude(37.325654f);
//        cab.setLatitude(-121.94562f);
//        restCabMockMvc.perform(put("/cabs/{id}", "10")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(TestUtil.convertObjectToJsonBytes(cab)))
//                .andExpect(status().isOk());
//
//    }
//maps API key
    //AIzaSyBzhs0NvH7Q40arZswHkBBNMkvd43hUW0M
}
