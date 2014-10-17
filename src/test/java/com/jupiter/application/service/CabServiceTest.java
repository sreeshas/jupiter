package com.jupiter.application.service;

import com.jupiter.application.Application;
import com.jupiter.application.config.MongoConfiguration;
import com.jupiter.application.domain.Cab;
import com.jupiter.application.repository.CabRepository;
import com.jupiter.application.repository.UserRepository;
import com.jupiter.application.web.rest.CabsResource;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;


import javax.inject.Inject;

import static junit.framework.Assert.assertFalse;
import static junit.framework.TestCase.assertEquals;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyFloat;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Test class for CabService
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@ActiveProfiles("dev")
public class CabServiceTest {

    @Mock
    private CabRepository cabRepository;

    private CabService cabService;


    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        cabService = new CabService();
        ReflectionTestUtils.setField(cabService, "cabRepository", cabRepository);
    }

    @Test
    public void testCreateCab() {
        when(cabRepository.exists(1234L)).thenReturn(Boolean.FALSE);
        Cab testCab = new Cab();
        testCab.setId(1234L);
        testCab.setLatitude(32.22F);
        testCab.setLongitude(12.22F);
        when(cabRepository.save(testCab)).thenReturn(testCab);
        Cab newCab = cabService.createCab(1234L, 32.22F, 12.22F);
        verify(cabRepository).save(any(Cab.class));
        assert(newCab.equals(testCab));
    }

    @Test
    public void testUpdateCab() {
        when(cabRepository.exists(anyLong())).thenReturn(Boolean.TRUE);
        Cab testCab = new Cab();
        testCab.setId(765L);
        testCab.setLatitude(32.22F);
        testCab.setLongitude(12.22F);
        when(cabRepository.findOne(anyLong())).thenReturn(testCab);
        Cab updateCab = new Cab();
        updateCab.setId(765L);
        updateCab.setLatitude(32.55F);
        updateCab.setLongitude(12.66F);
        when(cabRepository.save(updateCab)).thenReturn(updateCab);
        Cab newCab = cabService.createCab(765L, 32.55F, 12.66F);
        assert(newCab.equals(updateCab));
        verify(cabRepository).exists(anyLong());
    }

    @Test
    public void testDeleteExistingCab() {
        when(cabRepository.exists(765L)).thenReturn(true);
        assertTrue(cabService.deleteCab(765L));
        verify(cabRepository).exists(anyLong());
    }

    @Test
    public void testDeleteNonExistingCab() {
        when(cabRepository.exists(1234L)).thenReturn(false);
        assertFalse(cabService.deleteCab(1234L));
        verify(cabRepository).exists(anyLong());
    }

    public void testFindCab(){

    }

    public void testSearchCabs() {

    }
}
