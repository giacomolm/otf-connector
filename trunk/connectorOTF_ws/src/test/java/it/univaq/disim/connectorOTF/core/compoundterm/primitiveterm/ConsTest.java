/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package it.univaq.disim.connectorOTF.core.compoundterm.primitiveterm;

import it.univaq.disim.connectorOTF.core.compoundterm.primitiveterm.Cons;
import it.univaq.disim.connectorOTF.core.compoundterm.primitiveterm.Prod;
import it.univaq.disim.ips.data.action.Action;
import it.univaq.disim.ips.data.state.State;
import it.univaq.disim.ips.data.transition.Transition;

/**
 *
 * @author Giacomo
 */
public class ConsTest {

    public ConsTest() {
    }
   
    @org.junit.Test
    public void testStart() {
        
        try {
                Cons cons = new Cons("mina:tcp://localhost:6000?textline=true&sync=false", String.class);
                cons.setStart(new State("s0"));
                cons.addTransition(new Transition(new State("s0"), new Action("a"),new State("s1")));
                cons.start();
                Thread.sleep(2000);
                Prod instance = new Prod("mina:tcp://localhost:6000?textline=true&sync=false", String.class, "a");
                instance.start();
                Thread.sleep(2000);
        } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
        }
    }
}
