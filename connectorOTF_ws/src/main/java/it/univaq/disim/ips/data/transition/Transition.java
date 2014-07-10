/*
* Licensed to the Apache Software Foundation (ASF) under one
* or more contributor license agreements. See the NOTICE file
* distributed with this work for additional information
* regarding copyright ownership. The ASF licenses this file
* to you under the Apache License, Version 2.0 (the
* "License"); you may not use this file except in compliance
* with the License. You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing,
* software distributed under the License is distributed on an
* "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
* KIND, either express or implied. See the License for the
* specific language governing permissions and limitations
* under the License.
*/

package it.univaq.disim.ips.data.transition;

import it.univaq.disim.ips.data.state.State;
import it.univaq.disim.ips.data.action.Action;

/**
 *
 * @author Giacomo
 */
public class Transition {
    
    private State source,target;
    
    private Action action;
    
    private boolean loopTransition;

    public Transition(State source, Action action, State target) {
        this.source = source;
        this.action = action;
        this.target = target;
        
        if(source.equals(target)) setAsLoopTransition();
    }

    public State getSource() {
        return source;
    }

    public void setSource(State source) {
        this.source = source;
    }

    public State getTarget() {
        return target;
    }

    public void setTarget(State target) {
        this.target = target;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    private void setAsLoopTransition(){
        loopTransition = true;                
    }
    
    public boolean isALoopTransition(){
        return loopTransition;
    }
    
    @Override
    public String toString() {
        return this.source.getAlias()+"->"+this.action.getLabel()+"->"+this.target.getAlias()+"\n";
    }
    
    
    
    
}
