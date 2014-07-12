/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package it.univaq.disim.connectorOTF.controller;

import it.univaq.disim.connectorOTF.core.compoundterm.CompoundTerm;
import it.univaq.disim.ips.data.action.Action;
import it.univaq.disim.ips.data.action.InputAction;
import it.univaq.disim.ips.data.action.OutputAction;
import it.univaq.disim.ips.data.state.State;
import it.univaq.disim.ips.data.transition.Transition;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

@Controller
public class xmlImport {
    
    @RequestMapping(value="/import", method = RequestMethod.POST)
    public ModelAndView requestImport(@RequestParam(value = "xmlString") String xmlString){
        
        //formatting the xml string before parsing it
        xmlImport(prettyFormat(xmlString, 2));
        
        return new ModelAndView("import");
    }
    
    public void xmlImport(String xmlString){
        try{
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(xmlString));
            
            Document doc = dBuilder.parse(is);                       
            
            doc.getDocumentElement().normalize();
 
            Element rootEl = doc.getDocumentElement();
            
            ArrayList<Node> elements = new ArrayList();
            int i;
            for(i=0; i<rootEl.getChildNodes().getLength(); i++){
               if(rootEl.getChildNodes().item(i).getNodeName()=="term"){
                   elements.add(rootEl.getChildNodes().item(i));
               }
            }
                     
            for(i=0; i<elements.size(); i++){
                if(elements.get(i).getAttributes().getNamedItem("xsi:type")!=null){
                    
                    String className = elements.get(i).getAttributes().getNamedItem("xsi:type").getNodeValue();
                    className = Character.toUpperCase(className.charAt(0)) + className.substring(1);
                                        
                    try{
                        //compound case
                        CompoundTerm object = getCompoundObject(className, elements.get(i));
                        object.start();                       
                  
                    }
                    catch(ClassNotFoundException e){
                        CompoundTerm object = getPrimitiveObject(className, elements.get(i));
                        object.start();
                        
                    }                   
                    
                    Thread.sleep(5000);
                }
            }
        }
        catch(Exception e){
            System.err.println("Main Error "+e);
        }        
       
    }
    
    public static ArrayList<CompoundTerm> getChild(Node node){
        
        int i=0;
        ArrayList<CompoundTerm> result = new ArrayList<CompoundTerm>();
        ArrayList<Node> elements = new ArrayList();
        
        //adding only element with term tag
        for(i=0; i<node.getChildNodes().getLength(); i++){
               if(node.getChildNodes().item(i).getNodeName()=="term"){
                   elements.add(node.getChildNodes().item(i));
               }
            }
        
        for(i=0; i<elements.size(); i++){
            
            for(i=0; i<elements.size(); i++){
                if(elements.get(i).getAttributes().getNamedItem("xsi:type")!=null){
                    
                    String className = elements.get(i).getAttributes().getNamedItem("xsi:type").getNodeValue();
                    className = Character.toUpperCase(className.charAt(0)) + className.substring(1);
                    
                    Class<?> term_class;
                    try{
                        //compound case
                        term_class = Class.forName("it.univaq.disim.connectorOTF.core.compoundterm."+className);
                        
                        List childTerms = getChild(elements.get(i));
                        
                        Constructor<?> ctor = term_class.getConstructors()[0];
                        
                        System.out.println(childTerms);
                        
                        CompoundTerm object = (CompoundTerm) ctor.newInstance(new Object[] { childTerms.get(0), childTerms.get(1) });
                        
                        result.add(object);
                        
                    }
                    catch(ClassNotFoundException e){
                        try{

                            CompoundTerm object = getPrimitiveObject(className, elements.get(i));
                            result.add(object);
                        }
                        catch (Exception ex) {
                            Logger.getLogger(xmlImport.class.getName()).log(Level.SEVERE, null, ex);
                        } 
                        
                    } catch (Exception ex) {
                        Logger.getLogger(xmlImport.class.getName()).log(Level.SEVERE, null, ex);
                    } 
                    
                    
                }
            }
        }
        
        
        return result;
    }
    
    public static Map setAttributes(NodeList nlist){
        int i;
        Map result = new HashMap();
        
        for(i=0; i<nlist.getLength(); i++){
                     
            if(nlist.item(i).getChildNodes().getLength()>1){
                //we have to check if the node is alredy in the map
                if(result.get(nlist.item(i).getNodeName()) != null){
                    ArrayList al = new ArrayList();
                    if(result.get(nlist.item(i).getNodeName()) instanceof ArrayList) al.addAll((Collection) result.get(nlist.item(i).getNodeName()));
                    else al.add(result.get(nlist.item(i).getNodeName()));
                    al.add(setAttributes(nlist.item(i).getChildNodes()));
                    result.put(nlist.item(i).getNodeName(), al);
                }
                else result.put(nlist.item(i).getNodeName(), setAttributes(nlist.item(i).getChildNodes()));
            }
            else if(nlist.item(i).getNodeName()!="#text"){ // we want to avoid #text tag
                //base type
                result.put(nlist.item(i).getNodeName(), nlist.item(i).getTextContent().trim());
            }
                
        }
        
        return result;
    }
    
    public static CompoundTerm getPrimitiveObject(String className,Node element){
                            
        Class term_class = null;
        CompoundTerm object=null;
        try {
            term_class = Class.forName("it.univaq.disim.connectorOTF.core.compoundterm.primitiveterm."+className);
            //getting the parameters

            Map arguments = setAttributes(element.getChildNodes());
            //System.out.println(arguments);
            //we are assuming the first constructor is the right for reflexion
            Constructor<?> ctor = term_class.getConstructors()[0]; 

          
            if(arguments.get("actions") != null){
                Map actions = (Map) arguments.get("actions");
                if(actions.get("input")!=null && actions.get("output") !=null){

                    String input_uri;
                    Class input_type;

                    if((actions.get("input")) instanceof List){
                        int i;
                        List inputs = (List) actions.get("input");
                        input_uri = (String) ((Map)inputs.get(0)).get("uri");
                        for(i=1; i<inputs.size(); i++){
                             input_uri += ","+(String) ((Map)inputs.get(i)).get("uri");                             
                        }
                        input_type = Class.forName((String)((Map)inputs.get(0)).get("type"));
                       
                    }
                    else{
                        input_uri = (String) ((Map) actions.get("input")).get("uri");
                        input_type = Class.forName((String)((Map) actions.get("input")).get("type"));   
                    }  

                    String output_uri;
                    Class output_type;
                    if((actions.get("output")) instanceof List){
                        int i; 
                        List outputs = (List) actions.get("output");
                        output_uri = (String) ((Map)outputs.get(0)).get("uri");
                        for(i=1; i<outputs.size(); i++){
                            output_uri = ","+(String) ((Map)outputs.get(i)).get("uri");
                        }
                        output_type = Class.forName((String)((Map)outputs.get(0)).get("type"));
                    }
                    else{
                        output_uri = (String) ((Map) actions.get("output")).get("uri");
                        output_type = Class.forName((String)((Map) actions.get("output")).get("type"));
                    }
                    
                    object = (CompoundTerm) ctor.newInstance(new Object[] {input_uri, input_type, output_uri,output_type});
                }
                else if(actions.get("input")!=null){
                    //consumer case
                    String uri = (String) ((Map) actions.get("input")).get("uri");
                    Class type = Class.forName((String)((Map) actions.get("input")).get("type"));
                    String message = (String) ((Map) actions.get("input")).get("label");

                    object = (CompoundTerm) ctor.newInstance(new Object[] {uri, type, message});
                }
                else if(actions.get("output")!=null){
                    String uri = (String) ((Map) actions.get("output")).get("uri");
                    Class type = Class.forName((String)((Map) actions.get("output")).get("type"));
                    String message = (String) ((Map) actions.get("output")).get("label");

                    object = (CompoundTerm) ctor.newInstance(new Object[] {uri, type, message});
                }

            }
            
            if(arguments.get("protocol") != null && object!=null){
                Map protocol = (Map) arguments.get("protocol");
               if(protocol.get("transitions")  instanceof List){
                   List transitions = (List) protocol.get("transitions");
                   int i;
                   for(i=0; i<transitions.size(); i++){
                       //getting the source of the transition
                       State source = new State((String) ((Map) transitions.get(i)).get("source"));
                       Action action = null;
                       //getting the action, based on its type
                       if(((String) ((Map) transitions.get(i)).get("actionType")).equals("input")){
                        action = new InputAction((String) ((Map) transitions.get(i)).get("label"));
                       }
                       else if(((String) ((Map) transitions.get(i)).get("actionType")).equals("output")){
                        action = new OutputAction((String) ((Map) transitions.get(i)).get("label"));
                       }
                       //getting the target of the transition
                       State target = new State((String) ((Map) transitions.get(i)).get("target"));
                       object.addTransition(new Transition(source, action, target));
                   }
               }
               
               //Setting the starting state
                if(arguments.get("states") != null && object != null){
                    //getting the starting state from the xml                    
                    object.setStart(new State((String)((Map)arguments.get("states")).get("start")));                    
                }
            }            
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(xmlImport.class.getName()).log(Level.SEVERE, null, ex);    
        } catch (InstantiationException ex) {
            Logger.getLogger(xmlImport.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(xmlImport.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(xmlImport.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(xmlImport.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return object;
    }
    
    public static CompoundTerm getCompoundObject(String className,Node element) throws ClassNotFoundException{
        
        CompoundTerm object = null;
        try {
            Class term_class = Class.forName("it.univaq.disim.connectorOTF.core.compoundterm."+className);
            
            List childTerms = getChild(element);
                       
            Constructor<?> ctor = term_class.getConstructors()[0];
            
            
            object = (CompoundTerm) ctor.newInstance(new Object[] { childTerms.get(0), childTerms.get(1) });
            
        } catch (InstantiationException ex) {
            Logger.getLogger(xmlImport.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(xmlImport.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(xmlImport.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(xmlImport.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println(object);
        return object;
    }
    
    public static String prettyFormat(String input, int indent) {
        try
        {
            Source xmlInput = new StreamSource(new StringReader(input));
            StringWriter stringWriter = new StringWriter();
            StreamResult xmlOutput = new StreamResult(stringWriter);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            // This statement works with JDK 6
            transformerFactory.setAttribute("indent-number", indent);
             
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(xmlInput, xmlOutput);
            return xmlOutput.getWriter().toString();
        }
        catch (Throwable e)
        {
            // You'll come here if you are using JDK 1.5
            // you are getting an the following exeption
            // java.lang.IllegalArgumentException: Not supported: indent-number
            // Use this code (Set the output property in transformer.
            try
            {
                Source xmlInput = new StreamSource(new StringReader(input));
                StringWriter stringWriter = new StringWriter();
                StreamResult xmlOutput = new StreamResult(stringWriter);
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", String.valueOf(indent));
                transformer.transform(xmlInput, xmlOutput);
                return xmlOutput.getWriter().toString();
            }
            catch(Throwable t)
            {
                return input;
            }
        }
    }
    
    
}
