/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package it.univaq.disim.connectorOTF.core.compoundterm;

import it.univaq.disim.connectorOTF.core.compoundterm.primitiveterm.Cons;
import it.univaq.disim.connectorOTF.core.compoundterm.primitiveterm.Merge;
import it.univaq.disim.connectorOTF.core.compoundterm.primitiveterm.Prod;
import it.univaq.disim.ips.data.action.Action;
import it.univaq.disim.ips.data.state.State;
import it.univaq.disim.ips.data.transition.Transition;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Giacomo
 */
public class PlugTest {

    public PlugTest() {
        
    }
    
    //@org.junit.Test
    public void testConsProd1() {
        
        try {
                Cons cons = new Cons("vm:local1", String.class);
                cons.setStart(new State("s0"));
                cons.addTransition(new Transition(new State("s0"), new Action("a"),new State("s1")));
               
                Prod prod = new Prod("vm:local1", String.class, "a");
                prod.setStart(new State("s0"));
                prod.addTransition(new Transition(new State("s0"), new Action("a"),new State("s1")));
                
                Plug plug = new Plug(cons, prod);
                plug.start();
                Thread.sleep(2000);
        } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
        }
    }
    
    //@org.junit.Test
    public void testConsProd2() {
        
        try {
                Cons cons = new Cons("vm:local1", String.class);
                cons.setStart(new State("s0"));
                cons.addTransition(new Transition(new State("s0"), new Action("a"),new State("s1")));
               
                Prod prod = new Prod("vm:local1", String.class, "a");
                prod.setStart(new State("s0"));
                prod.addTransition(new Transition(new State("s0"), new Action("a"),new State("s1")));
                
                Plug plug = new Plug(cons, prod);
                plug.start();
                Thread.sleep(2000);
                
                Prod external_prod = new Prod("vm:local1", String.class, "a");
                external_prod.setStart(new State("s0"));
                external_prod.addTransition(new Transition(new State("s0"), new Action("a"),new State("s1")));
                external_prod.start();
                
        } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
        }
    }
    
    @org.junit.Test
    public void testMerge() {
            
        try {
            Merge merge = new Merge("vm:local1,vm:local2,vm:local3",
                    String.class, "vm:port3", ArrayList.class);
            
            merge.setStart(new State("s0"));
            merge.addTransition(new Transition(new State("s0"), new Action("ciao"), new State("s1")));
            merge.addTransition(new Transition(new State("s1"), new Action("come"), new State("s2")));
            merge.addTransition(new Transition(new State("s2"), new Action("stai?"), new State("s3")));
            
            Prod prod1 = new Prod("vm:local1", String.class, "ciao");
            prod1.setStart(new State("s0"));
            prod1.addTransition(new Transition(new State("s0"), new Action("ciao"),new State("s1")));
            //try to substitute ciao with miao, and see the log 
            
            Prod prod2 = new Prod("vm:local2", String.class, "come");
            prod2.setStart(new State("s0"));
            prod2.addTransition(new Transition(new State("s0"), new Action("come"),new State("s1")));
            
            Prod prod3 = new Prod("vm:local3", String.class, "stai?");
            prod3.setStart(new State("s0"));
            prod3.addTransition(new Transition(new State("s0"), new Action("stai?"),new State("s1")));
            
            Plug plug1 = new Plug(merge, prod1);
            Plug plug2 = new Plug(plug1, prod2);
            Plug plug3 = new Plug(plug2, prod3);
            
            plug3.start();
            
            Thread.sleep(3000);
        } catch (InterruptedException ex) {
            Logger.getLogger(PlugTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
