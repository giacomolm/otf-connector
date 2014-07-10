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

package it.univaq.disim.ips.data.action;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Giacomo
 */
public class Action {
    
    /**
     * label refers to the name of an action related to a transition step
     */
    private String label;
    
    private List<Action> equivalents;
    
    public Action(String label) {
        this.label = label;
        equivalents = new ArrayList();
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
    
    /**
     * Equivalance in this case is the equals function
     * @param a
     * @return 
     */
    public boolean equivalent(Action a){
        if(this.equivalents.contains(a)) return true;
        else return false;
    }
    
    public void addEquivalent(Action a){
        this.equivalents.add(a);
    }
            

    public boolean equals(Action a) {
        if(this.label == a.label) return true;
        else return false;
    }

    @Override
    public String toString() {
        return this.getLabel(); //To change body of generated methods, choose Tools | Templates.
    }
    
}
