/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package it.univaq.disim.connectorOTF.core.compoundterm.primitiveterm;

import it.univaq.disim.connectorOTF.controller.xmlImport;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;
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
public class MergeTest {
    
    public MergeTest() {
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
     * Test of start method, of class Merge.
     */
    @Test
    public void testMergeImport() {
        BufferedReader br = null;
        try {
            System.out.println("Starting xml import");
            br = new BufferedReader(new FileReader("C:\\Users\\Giacomo\\Desktop\\connectorOTF_ws\\xml\\merge.xml"));
            String sCurrentLine, result="";
            while ((sCurrentLine = br.readLine()) != null) {
                result += sCurrentLine+"\n";
            }
            xmlImport xi = new xmlImport();
            xi.requestImport(result);
            
            //xmlImport.xmlImport(xmlImport.prettyFormat(result, 2));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MergeTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MergeTest.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                br.close();
            } catch (IOException ex) {
                Logger.getLogger(MergeTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }
    
}
