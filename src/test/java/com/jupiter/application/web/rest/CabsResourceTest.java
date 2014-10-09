package com.jupiter.application.web.rest;

import com.jupiter.application.Application;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * Test class for Cabs Resource Test Controller.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@DirtiesContext(classMode= DirtiesContext.ClassMode.AFTER_CLASS)
@ActiveProfiles("dev")
public class CabsResourceTest {



    public void testCreatingCab() throws Exception {

    }

    public void testUpdatingCab() throws Exception {

    }

    public void testExistingCabDetails() throws Exception {

    }

    public void testUnknownCabDetails() throws Exception {

    }

    public void testSearch() throws Exception {

    }

    public void testDeleteCab() throws Exception {

    }
}
