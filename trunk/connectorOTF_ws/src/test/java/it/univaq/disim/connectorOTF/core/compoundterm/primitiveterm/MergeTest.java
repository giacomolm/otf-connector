/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package it.univaq.disim.connectorOTF.core.compoundterm.primitiveterm;

import it.univaq.disim.connectorOTF.controller.xmlImport;
import it.univaq.disim.connectorOTF.core.compoundterm.primitiveterm.Merge;
import it.univaq.disim.connectorOTF.core.compoundterm.primitiveterm.Prod;
import it.univaq.disim.ips.data.action.Action;
import it.univaq.disim.ips.data.state.State;
import it.univaq.disim.ips.data.transition.Transition;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

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
    public void testMergeImport() throws InterruptedException {

            Merge merge = new Merge("mina:tcp://localhost:6897?textline=true&sync=false,mina:tcp://localhost:6898?textline=true&sync=false", 
                                    String.class, "vm:port3", ArrayList.class);
            
            merge.setStart(new State("s0"));
            merge.addTransition(new Transition(new State("s0"), new Action("a"), new State("s1")));
            merge.addTransition(new Transition(new State("s1"), new Action("b"), new State("s2")));
            merge.addTransition(new Transition(new State("s2"), new Action("c"), new State("s3")));
            
            merge.start();
            
            Prod instance1 = new Prod("mina:tcp://localhost:6897?textline=true&sync=false", String.class, "a");
            instance1.start();
            Prod instance2 = new Prod("mina:tcp://localhost:6898?textline=true&sync=false", String.class, "b");
            instance2.start();
            
            Thread.sleep(2000);

    }
    
}
