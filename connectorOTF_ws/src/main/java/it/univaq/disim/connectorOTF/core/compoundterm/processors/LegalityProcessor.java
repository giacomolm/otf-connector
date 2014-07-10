/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package it.univaq.disim.connectorOTF.core.compoundterm.processors;

import it.univaq.disim.connectorOTF.core.compoundterm.CompoundTerm;
import it.univaq.disim.connectorOTF.core.exceptions.DefaultIllegalStateException;
import it.univaq.disim.ips.data.transition.Transition;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;


public class LegalityProcessor implements Processor{

    protected CompoundTerm term;
    
    public LegalityProcessor(CompoundTerm term) {
        this.term = term;
    }

    
    @Override
    public void process(Exchange exchng) throws Exception {
        
        if(!term.isComposed()){
            if(!checkLegality(exchng.getIn().getBody(String.class))){
                throw new DefaultIllegalStateException("Cannot match input action with the one expected in the IPS");
            }
        }
    }
    
    /**
     * Checking if the input action respect the IPS. Check is based on action name
     * @param actionName name of the action we're checking
     * @return 
     */
    public boolean checkLegality(String actionName){
        int i;
        for(i=0; i<term.getTransitions().size(); i++){
            //consider only transition with source equal to current state
            Transition t = term.getTransitions().get(i);
            
            if(t.getSource().equals(term.getCurrentState())){
                if(t.getAction().getLabel().equals(actionName)){
                    //updating current state to the target state
                    term.setCurrentState(t.getTarget());
                    
                    return true;
                }
            }
        }
        
        return false;
    }    
    
}
