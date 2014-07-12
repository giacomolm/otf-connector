/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package it.univaq.disim.connectorOTF.core.compoundterm;

import it.univaq.disim.connectorOTF.core.compoundterm.primitiveterm.Cons;
import it.univaq.disim.connectorOTF.core.compoundterm.primitiveterm.Prod;
import it.univaq.disim.ips.data.action.Action;
import it.univaq.disim.ips.data.state.State;
import it.univaq.disim.ips.data.transition.Transition;

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
    
    @org.junit.Test
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
}
