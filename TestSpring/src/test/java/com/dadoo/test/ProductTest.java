package com.dadoo.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.pickup.service.KidProfileService;

@ContextConfiguration(
		inheritLocations = true,
		locations = {
            "classpath:/root-context.xml",
            "file:src/main/resources/root-context.xml",
            "file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context.xml"
		})
public class ProductTest {
    //private Product product;

	@Autowired
	private KidProfileService kidProfileService;
	
	
    @Before
    public void setUp() throws Exception {
        System.out.println("In the before method ");
    }

    @Test
    public void testSetAndGetDescription() {
      System.out.println("**** doing real tests");
      
      if ( kidProfileService == null ) {
    	  System.out.println("*** kid profile service null ");
      }else {
    	  System.out.println("** kid profile service not null ");
      }
    }

    @Test
    public void testSetAndGetPrice() {
       System.out.println("doing second test ");
    }
    
    @After
    public void tearDown() {
    	
    	System.out.println("Tear down happened ");
    }
}
