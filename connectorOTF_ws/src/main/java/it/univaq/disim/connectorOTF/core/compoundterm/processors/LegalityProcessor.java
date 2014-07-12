/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package it.univaq.disim.connectorOTF.core.compoundterm.processors;

import it.univaq.disim.connectorOTF.core.compoundterm.CompoundTerm;
import it.univaq.disim.connectorOTF.core.compoundterm.primitiveterm.PrimitiveTerm;
import it.univaq.disim.connectorOTF.core.exceptions.DefaultIllegalStateException;
import it.univaq.disim.ips.data.state.State;
import it.univaq.disim.ips.data.transition.Transition;
import java.util.Iterator;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;


public class LegalityProcessor implements Processor{

    protected CompoundTerm term;
    
    public LegalityProcessor(CompoundTerm term) {
        this.term = term;
    }

    
    @Override
    public void process(Exchange exchng) throws Exception {
        //Logging
        term.out.append("Received action "+exchng.getIn().getBody(String.class)+" in "+term+"\n");
        
        if(!checkLegality(term, exchng.getIn().getBody(String.class))){
            throw new DefaultIllegalStateException("Cannot match input action with the one expected in the IPS");
        }
    }
    
    /**
     * Checking if the input action respect the IPS. Check is based on action name
     * @param actionName name of the action we're checking
     * @return 
     */
    public boolean checkLegality(CompoundTerm term, String actionName){

        boolean result = true;
        
        if(! (term instanceof PrimitiveTerm)){
            for(Iterator<CompoundTerm> it = term.getComponents().iterator(); it.hasNext();){
                CompoundTerm ct = it.next();
                result = checkLegality(ct, actionName);
                if(!result) return false;
            }
        }
        else{
            result = checkTermLegality(term, actionName);
        }
        
        return result;
    }
    
    public boolean checkTermLegality(CompoundTerm term, String actionName){
        boolean result = true;
        State nextState = null;
        int i;
        for(i=0; i<term.getTransitions().size(); i++){ 
            //consider only transition with source equal to current state
            Transition t = term.getTransitions().get(i);
            
            if(t.getSource().equals(term.getCurrentState())){
                
                if(!t.getAction().getLabel().equals(actionName)){
                    result = false;
                }
                else{
                    //updating current state to the target state
                    nextState = t.getTarget();                    
                }
            }
        }
        
        if(result) term.setCurrentState(nextState); 
        term.out.append(" legal: "+result+"\n");
        return result;
    }
    
}
