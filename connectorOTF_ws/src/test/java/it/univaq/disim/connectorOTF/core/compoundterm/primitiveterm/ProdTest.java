/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package it.univaq.disim.connectorOTF.core.compoundterm.primitiveterm;

import org.apache.camel.Exchange;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Giacomo
 */
public class ProdTest {
    
    public ProdTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of start method, of class Prod.
     */
    @org.junit.Test
    public void testStart() {
        
        
        try {
                Prod instance = new Prod("mina:tcp://localhost:6898?textline=true&sync=false", String.class, "b");
                instance.start();
                Thread.sleep(1000);
        } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
        }
        catch(Exception e){
            
    }
    }

    
}
